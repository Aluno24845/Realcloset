package pt.ipt.dam.realcloset

// Importação das bibliotecas necessárias
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Button
import android.content.Intent

// Definição do fragmento "AboutFragment", que exibe informações sobre o projeto, autores e bibliotecas
class AboutFragment : Fragment() {

    // Método responsável por criar e configurar a interface do fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // Configura o elemento de interface que mostra as informações sobre biblioteca e frameworks
        val librariesInfo = view.findViewById<TextView>(R.id.libraries_info)

        // Preenche o TextView com as informações sobre as bibliotecas e frameworks utilizados no projeto
        librariesInfo.text = """
            - Android SDK: Para desenvolvimento da aplicação.
            - androidx.core: Extensões Kotlin para operações no Android.
            - androidx.appcompat: Suporte de compatibilidade com versões anteriores do Android.
            - Material Components: Implementação de Material Design.
            - ConstraintLayout: Layout responsivo baseado em constraints.
            - Retrofit: Comunicação com APIs REST.
            - OkHttp: Logging de chamadas HTTP.
        """.trimIndent()

        // Configura o botão que, ao ser clicado, abrirá a política de privacidade no browser
        val privacyButton = view.findViewById<Button>(R.id.privacy_policy_button)
        privacyButton.setOnClickListener {
            // Cria uma Intent para abrir o URL da política de privacidade no browser
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://aluno24845.github.io/Realcloset/politica_privacidade.html")
            startActivity(intent)
        }

        // Retorna a vista configurada para o fragmento
        return view
    }
}
