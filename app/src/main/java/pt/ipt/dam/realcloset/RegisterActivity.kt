package pt.ipt.dam.realcloset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.RadioButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        // Inicializar os componentes
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        birthdate = findViewById(R.id.birthdate)
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

            // Validar passwords
            if (passwordText != confirmPasswordText) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar campos obrigatórios
            if (firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Determinar a visibilidade do perfil
            val isProfilePublic = publicProfile.isChecked

            // Exibir mensagem de sucesso (fazer a lógica de registo)
            Toast.makeText(this, "Registo efetuado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}
