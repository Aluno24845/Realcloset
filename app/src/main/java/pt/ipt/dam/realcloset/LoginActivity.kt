package pt.ipt.dam.realcloset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipt.dam.realcloset.model.LoginRequest
import pt.ipt.dam.realcloset.model.LoginResponse
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerText = findViewById<TextView>(R.id.register_text)

        // Lógica do botão de login
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Verificar se os campos não estão vazios
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Preparar a requisição de login
                val loginRequest = LoginRequest(email = email, password = password)

                // Chamar o método de login usando Retrofit (com Coroutine)
                val retrofit = RetrofitInitializer()
                val apiService = retrofit.apiService()

                lifecycleScope.launch {
                    try {
                        // Tenta realizar o login
                        val response: Response<LoginResponse> = apiService.loginUser(loginRequest)

                        if (response.isSuccessful) {
                            // Se o login for bem-sucedido, obtém o token
                            val loginResponse = response.body()
                            val token = loginResponse?.token

                            // Armazenar o token (exemplo, em SharedPreferences)
                            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("auth_token", token)
                            editor.apply()

                            // Exibir uma mensagem de sucesso
                            Toast.makeText(applicationContext, "Login bem-sucedido", Toast.LENGTH_SHORT).show()

                            // Redirecionar para a MainActivity (tela principal)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()  // Finaliza a tela de login
                        } else {
                            Toast.makeText(applicationContext, "Erro no login", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // Caso ocorra algum erro com a requisição
                        Toast.makeText(applicationContext, "Erro na requisição: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Ao clicar no texto de registo, redireciona para a RegisterActivity
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
