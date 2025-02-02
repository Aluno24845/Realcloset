package pt.ipt.dam.realcloset

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer

/**
 * Fragmento responsável por mostrar o perfil do utilizador logado.
 */
class ProfileFragment : Fragment() {

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

        // Utiliza uma Coroutine para executar a chamada à API de forma assíncrona.
       lifecycleScope.launch {
           try {
               // Realiza a requisição ao endpoint para obter o utilizador logado.
               val response = apiService.getLoggedUser("Bearer $token")

               // Verifica se a resposta da API foi bem-sucedida.
               if (response.isSuccessful) {
                   val user = response.body()
                   if (user != null) {
                       // Recupera as referências aos elementos da interface gráfica.
                       val profileName = view.findViewById<TextView>(R.id.profile_name)
                       val profileEmail = view.findViewById<TextView>(R.id.profile_email)
                       val profileDetails = view.findViewById<TextView>(R.id.profile_details)

                       // Preenche os campos do perfil com as informações do utilizador.
                       profileName.text = "${user.nome ?: "Sem nome"} ${user.apelido ?: ""}"
                       profileEmail.text = user.email ?: "Email não disponível"
                       profileDetails.text = "Data de nascimento: ${user.dataNascimento ?: "N/D"}\nTipo de perfil: ${user.tipoDePerfil ?: "N/D"}"
                   } else {
                       // Caso o utilizador não seja encontrado, apresenta uma mensagem de erro.
                       Toast.makeText(requireContext(), "Utilizador não encontrado", Toast.LENGTH_SHORT).show()
                   }
               } else {
                   // Apresenta uma mensagem de erro caso a resposta não seja bem-sucedida.
                   Toast.makeText(requireContext(), "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
               }
           } catch (e: Exception) {
               // Captura erros de comunicação e apresenta uma mensagem de erro.
               Toast.makeText(requireContext(), "Erro de comunicação: ${e.message}", Toast.LENGTH_SHORT).show()
           }
       }
       return view
    }
}