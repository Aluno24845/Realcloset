package pt.ipt.dam.realcloset.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.model.Peca

class PecasAdapter(
    private val pecas: List<Peca>,
    private val isUserLoggedIn: Boolean, // Verificar se o utilizador está autenticado
    private val onFavoritoClick: (Peca) -> Unit
) : RecyclerView.Adapter<PecasAdapter.PecaViewHolder>() {

    class PecaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.peca_titulo)
        val marca: TextView = view.findViewById(R.id.peca_marca)
        val categoria: TextView = view.findViewById(R.id.peca_categoria)
        val btnFavorito: ImageButton = view.findViewById(R.id.btn_favorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PecaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.peca_item, parent, false)
        return PecaViewHolder(view)
    }

    // Liga os dados de uma peça a um item da lista
    override fun onBindViewHolder(holder: PecaViewHolder, position: Int) {
        val peca = pecas[position]
        holder.titulo.text = peca.Titulo
        holder.marca.text = "Marca: ${peca.Marca}"
        holder.categoria.text = "Categoria: ${peca.Categoria}"

        // Mostrar ou ocultar o botão favorito dependendo do login
        if (isUserLoggedIn) {
            holder.btnFavorito.visibility = View.VISIBLE
            // Ação ao clicar no botão favorito
            holder.btnFavorito.setOnClickListener {
                holder.btnFavorito.setColorFilter(Color.YELLOW) // Muda a cor do botão ao ser selecionado
                onFavoritoClick(peca) // Callback para manipular a seleção da peça
            }
        } else {
            holder.btnFavorito.visibility = View.GONE // Oculta o botão se não houver login
        }
    }
    // Retorna o número total de peças
    override fun getItemCount(): Int = pecas.size
}
