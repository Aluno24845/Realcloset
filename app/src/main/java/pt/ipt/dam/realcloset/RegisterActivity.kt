package pt.ipt.dam.realcloset

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipt.dam.realcloset.model.RegisterRequest
import pt.ipt.dam.realcloset.model.RegisterResponse
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var birthdate: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var publicProfile: RadioButton
    private lateinit var privateProfile: RadioButton
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar o EditText
        birthdate = findViewById(R.id.birthdate)

        // Configurar o DatePickerDialog
        birthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Formatar a data selecionada para "aaaa/mm/dd"
                val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
                birthdate.setText(formattedDate)
            }, year, month, dayOfMonth)

            datePickerDialog.show()
        }

        // Inicializar os componentes
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirm_password)
        publicProfile = findViewById(R.id.public_profile)
        privateProfile = findViewById(R.id.private_profile)
        registerButton = findViewById(R.id.register_button)

        // Configurar o botão de registo
        registerButton.setOnClickListener {
            val firstNameText = firstName.text.toString()
            val lastNameText = lastName.text.toString()
            val birthdateText = birthdate.text.toString()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            // Validar campos obrigatórios
            if (firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || birthdateText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar email
            if (!isValidEmail(emailText)) {
                Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar passwords
            if (passwordText != confirmPasswordText) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Determinar a visibilidade do perfil
            val tipoDePerfil = if (publicProfile.isChecked) "publico" else "privado"

            // Converter a data de nascimento para Date
            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val birthdateDate = try {
                sdf.parse(birthdateText) ?: Date() // Caso não seja válida, usa a data atual
            } catch (e: Exception) {
                Toast.makeText(this, "Data de nascimento inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Criar o objeto de registo
            val registerRequest = RegisterRequest(
                nome = firstNameText,
                apelido = lastNameText,
                dataNascimento = birthdateDate,
                tipoDePerfil = tipoDePerfil,
                email = emailText,
                password = passwordText
            )

            // Chamar o método de registo utilizando Retrofit (com Coroutine)
            val retrofit = RetrofitInitializer()
            val apiService = retrofit.apiService()

            lifecycleScope.launch {
                try {
                    // Realiza a requisição para registar o utilizador
                    val response: Response<RegisterResponse> = apiService.registerUser(registerRequest)

                    if (response.isSuccessful) {
                        // Se o registo for bem-sucedido, obter a resposta
                        val registerResponse = response.body()
                        Toast.makeText(this@RegisterActivity, registerResponse?.message, Toast.LENGTH_SHORT).show()

                        // Redirecionar para a tela de login ou tela principal
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()  // Finaliza a tela de registo
                    } else {
                        Toast.makeText(this@RegisterActivity, "Erro no registo. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Caso ocorra algum erro com a requisição
                    Toast.makeText(this@RegisterActivity, "Erro na requisição: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Função para validar o formato do email
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }
}
