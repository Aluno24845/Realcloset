package pt.ipt.dam.realcloset

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipt.dam.realcloset.model.User
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.retrofit.service.ApiService
import retrofit2.Response

/**
 * Fragmento responsável por mostrar o perfil do utilizador logado.
 */
class ProfileFragment : Fragment() {


    private var userId: Int? = null  // Armazenar o ID do utilizador logado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento a partir do XML.
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Recupera o token de autenticação armazenado nas SharedPreferences.
        val sharedPreferences = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token",null)

        // Se o token for nulo, apresentar mensagem de erro
        if (token == null) {
            Toast.makeText(requireContext(), "Utilizador não autenticado", Toast.LENGTH_SHORT)
                .show()
            return view
        }

        // Inicializar o cliente Retrofit para requisições à API
        val apiService = RetrofitInitializer().apiService()

        // Recupera as referências aos elementos da interface gráfica.
        val profileName = view.findViewById<TextView>(R.id.profile_name)
        val profileEmail = view.findViewById<TextView>(R.id.profile_email)
        val profileDetails = view.findViewById<TextView>(R.id.profile_details)
        val publicProfile = view.findViewById<RadioButton>(R.id.public_profile)
        val privateProfile = view.findViewById<RadioButton>(R.id.private_profile)


        // Obter o utilizador logado e o seu ID
        lifecycleScope.launch {
            try {
                val response = apiService.getLoggedUser("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        userId = user.id  // Armazena o ID do utilizador logado

                        // Preenche os campos do perfil com as informações do utilizador.
                        profileName.text = "${user.nome ?: "Sem nome"} ${user.apelido ?: ""}"
                        profileEmail.text = user.email ?: "Email não disponível"
                        profileDetails.text = "Data de nascimento: ${user.dataNascimento ?: "N/D"}\nTipo de perfil: ${user.tipoDePerfil ?: "N/D"}"
                        preencherCamposPerfil(view, user)
                        // Selecionar o tipo de perfil no RadioGroup
                        if (user.tipoDePerfil == "publico") {
                            publicProfile.isChecked = true
                        } else {
                            privateProfile.isChecked = true
                        }
                    } else {
                        Toast.makeText(requireContext(), "Utilizador não encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro de comunicação: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        configurarBotoes(view, apiService, token)
        return view
    }

    //Preencher os campos de edição com as informações atuais
    private fun preencherCamposPerfil(view: View, user: User) {
        val profileName = view.findViewById<TextView>(R.id.profile_name)
        val emailEdit = view.findViewById<EditText>(R.id.profile_email_edit)

        profileName.text = "${user.nome} ${user.apelido}"
        emailEdit.setText(user.email)
    }

    // Configurar os botoes que atualizam as informações do utilizador
    private fun configurarBotoes(view: View, apiService: ApiService, token: String) {
        val emailEdit = view.findViewById<EditText>(R.id.profile_email_edit)
        val passwordEdit = view.findViewById<EditText>(R.id.profile_password_edit)

        val saveEmailButton = view.findViewById<Button>(R.id.save_email_button)
        val saveProfileTypeButton = view.findViewById<Button>(R.id.save_profile_type_button)
        val savePasswordButton = view.findViewById<Button>(R.id.save_password_button)

        // Atualizar o email
        saveEmailButton.setOnClickListener {
            val newEmail = emailEdit.text.toString()
            if (isValidEmail(newEmail)) {
                updateUserData(apiService, token, mapOf("email" to newEmail))
            } else {
                Toast.makeText(requireContext(), "Email inválido", Toast.LENGTH_SHORT).show()
            }
        }

        val publicProfile = view.findViewById<RadioButton>(R.id.public_profile)
        // Atualizar o tipo de perfil
        saveProfileTypeButton.setOnClickListener {
            val selectedType = if (publicProfile.isChecked) "publico" else "privado"
            updateUserData(apiService, token, mapOf("tipoDePerfil" to selectedType))
        }

        // Atualizar a password
        savePasswordButton.setOnClickListener {
            val newPassword = passwordEdit.text.toString()
            if (newPassword.length < 6) {
                Toast.makeText(requireContext(), "A password deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Atualizar a password diretamente sem encriptar
            updateUserData(apiService, token, mapOf("password" to newPassword))
        }
    }

    // validação de email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    // atualizar as informações do utilizador logado, pela API
    private fun updateUserData(apiService: ApiService, token: String, data: Map<String, String>) {
        userId?.let { id ->
            lifecycleScope.launch {
                try {
                    val response: Response<Void> = apiService.updateUser("Bearer $token", id, data)
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Alteração salva com sucesso", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                        Toast.makeText(requireContext(), "Erro ao salvar alteração: ${response.code()} - $errorBody", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erro de comunicação: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(requireContext(), "ID do utilizador não encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}