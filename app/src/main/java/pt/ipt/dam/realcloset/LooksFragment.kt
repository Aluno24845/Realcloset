package pt.ipt.dam.realcloset.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.utils.SessionManager
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import pt.ipt.dam.realcloset.model.Peca

class LooksFragment : Fragment() {

    private lateinit var botaoPecaAleatoria: Button  // Botão para mostrar peça aleatória
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_looks, container, false)

        // Referencia o botão
        botaoPecaAleatoria = view.findViewById(R.id.botao_peca_aleatoria)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o gestor de sessão do utilizador
        sessionManager = SessionManager(requireContext())

        // Configura o clique no botão
        botaoPecaAleatoria.setOnClickListener {
            mostrarPecaAleatoria()
        }
    }

    private fun mostrarPecaAleatoria() {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                // Solicita todas as peças do utilizador
                val response = withContext(Dispatchers.IO) { apiService.getPecas("Bearer $token") }

                // Verifica se há peças
                if (response.isNotEmpty()) {
                    // Escolhe uma peça aleatória
                    val pecaAleatoria = response.random()

                    // Cria o layout de cartão da peça
                    val pecaCardView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.peca_card, null)

                    // Acessa os elementos do cartão
                    val titulo = pecaCardView.findViewById<TextView>(R.id.peca_titulo)
                    val marca = pecaCardView.findViewById<TextView>(R.id.peca_marca)
                    val referencia = pecaCardView.findViewById<TextView>(R.id.peca_referencia)
                    val categoria = pecaCardView.findViewById<TextView>(R.id.peca_categoria)
                    val imagem = pecaCardView.findViewById<ImageView>(R.id.peca_imagem)

                    // Preenche os dados da peça no cartão
                    titulo.text = pecaAleatoria.Titulo
                    marca.text = "Marca: ${pecaAleatoria.Marca}"
                    referencia.text = "Referência: ${pecaAleatoria.Referencia}"
                    categoria.text = "Categoria: ${pecaAleatoria.Categoria}"
                    mostrarImagemBase64(pecaAleatoria.Imagem, imagem)

                    // Exibe o cartão no layout do fragmento
                    val container = view?.findViewById<ViewGroup>(R.id.container_peca_aleatoria)
                    container?.removeAllViews() // Remove qualquer view existente
                    container?.addView(pecaCardView) // Adiciona o cartão de peça

                } else {
                    // Caso não haja peças
                    Toast.makeText(requireContext(), "Sem peças para mostrar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Tratar erros
                Toast.makeText(requireContext(), "Erro ao obter peças: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun mostrarImagemBase64(base64String: String, imageView: ImageView) {
        try {
            // Decodifica a string Base64 para bytes
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)

            // Converte os bytes para Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            // Mostra a imagem no ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace() // Tratar erro em caso de string inválida
        }
    }


}
