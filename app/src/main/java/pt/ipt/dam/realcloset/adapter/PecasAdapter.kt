package pt.ipt.dam.realcloset.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.model.Peca

class PecasAdapter(
    private val pecas: List<Peca>, // Lista de peças a ser exibida
    private val isUserLoggedIn: Boolean, // Indica se o utilizador está autenticado
    private var pecasFavoritas: List<Peca>, // Lista das peças favoritas do utilizador
    private val onFavoritoClick: (Peca) -> Unit // Função a executar ao clicar no botão de favorito
) : RecyclerView.Adapter<PecasAdapter.PecaViewHolder>() {

    // ViewHolder que mantém as referências para os elementos do layout de cada item da lista
    class PecaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.peca_titulo) // Título da peça
        val marca: TextView = view.findViewById(R.id.peca_marca) // Marca da peça
        val referencia: TextView = view.findViewById(R.id.peca_referencia) // Referência
        val imagem: ImageView = view.findViewById(R.id.peca_imagem) // Imagem
        val categoria: TextView = view.findViewById(R.id.peca_categoria) // Categoria da peça
        val btnFavorito: ImageButton = view.findViewById(R.id.btn_favorito) // Botão para marcar/desmarcar favorito
    }

    // Criação de novas instâncias do ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PecaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.peca_item, parent, false)
        return PecaViewHolder(view)
    }

    // Ligação dos dados às views do ViewHolder
    override fun onBindViewHolder(holder: PecaViewHolder, position: Int) {
        val peca = pecas[position]
        holder.titulo.text = peca.Titulo // Define o título da peça
        holder.marca.text = "Marca: ${peca.Marca}" // Define a marca da peça
        holder.categoria.text = "Categoria: ${peca.Categoria}" // Define a categoria da peça
        holder.referencia.text = "Referência: ${peca.Referencia}" // Exibe a referência
        //holder.imagem.setImageResource(peca.Imagem) // Atribui a imagem da peça, supondo que a peça tenha um ID de recurso de imagem

        // Verifica se a peça está nos favoritos e define a cor da estrela
        if (pecasFavoritas.contains(peca)) {
            holder.btnFavorito.setColorFilter(Color.YELLOW) // Estrela amarela para favorito
        } else {
            holder.btnFavorito.setColorFilter(Color.GRAY) // Estrela cinzenta para não favorito
        }

        // Mostra ou oculta o botão favorito dependendo do estado de login
        if (isUserLoggedIn) {
            holder.btnFavorito.visibility = View.VISIBLE
            holder.btnFavorito.setOnClickListener {
                onFavoritoClick(peca) // Executa a função ao clicar no botão
            }
        } else {
            holder.btnFavorito.visibility = View.GONE // Esconde o botão se não estiver autenticado
        }
    }

    // Retorna o número total de peças
    override fun getItemCount(): Int = pecas.size

    // Atualiza a lista de favoritos e notifica a RecyclerView
    fun updateFavoritos(novosFavoritos: List<Peca>) {
        pecasFavoritas = novosFavoritos
        notifyDataSetChanged() // Atualiza a interface
    }

}
