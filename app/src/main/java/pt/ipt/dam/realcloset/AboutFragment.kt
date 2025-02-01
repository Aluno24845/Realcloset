package pt.ipt.dam.realcloset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        // Configura as informações do projeto
        val courseInfo = view.findViewById<TextView>(R.id.course_info)
        val librariesInfo = view.findViewById<TextView>(R.id.libraries_info)

        // Configura as informações dos autores
        val author1Name = view.findViewById<TextView>(R.id.author1_name)
        val author1Number = view.findViewById<TextView>(R.id.author1_number)
        val author1Picture = view.findViewById<ImageView>(R.id.author1_picture)
        val author2Name = view.findViewById<TextView>(R.id.author2_name)
        val author2Number = view.findViewById<TextView>(R.id.author2_number)
        val author2Picture = view.findViewById<ImageView>(R.id.author2_picture)

        // Informação sobre bibliotecas e frameworks
        librariesInfo.text = """
            - Android SDK: Para desenvolvimento da aplicação.
            - androidx.core: Extensões Kotlin para operações no Android.
            - androidx.appcompat: Suporte de compatibilidade com versões anteriores do Android.
            - Material Components: Implementação de Material Design.
            - ConstraintLayout: Layout responsivo baseado em constraints.
            - Retrofit: Comunicação com APIs REST.
            - OkHttp: Logging de chamadas HTTP.
        """.trimIndent()

        return view
    }
}
