package pt.ipt.dam.realcloset.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.model.Peca
import android.util.Base64

class ClosetAdapter(
    private val pecas: List<Peca>
) : RecyclerView.Adapter<ClosetAdapter.ClosetViewHolder>() {

    // ViewHolder que mantém as referências para os elementos do layout de cada item da lista
    class ClosetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.peca_titulo) // Título da peça
        val marca: TextView = view.findViewById(R.id.peca_marca) // Marca da peça
        val referencia: TextView = view.findViewById(R.id.peca_referencia) // Referência
        val imagem: ImageView = view.findViewById(R.id.peca_imagem) // Imagem
        val categoria: TextView = view.findViewById(R.id.peca_categoria) // Categoria da peça
        val btnFavorito: ImageButton = view.findViewById(R.id.btn_favorito) // Botão para marcar/desmarcar favorito
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.peca_item, parent, false)
        return ClosetViewHolder(view)
    }


    // Ligação dos dados às views do ViewHolder
    override fun onBindViewHolder(holder: ClosetViewHolder, position: Int) {
        val peca = pecas[position]
        holder.titulo.text = peca.Titulo // Define o título da peça

        // Verificar se 'Marca' não é nula ou vazia
        if (peca.Marca.isNullOrEmpty()) {
            holder.marca.visibility = View.GONE
        } else {
            holder.marca.text = "Marca: ${peca.Marca}" // Define a marca da peça
        }

        // Verificar se 'Referencia' não é nula ou vazia
        if (peca.Referencia.isNullOrEmpty()) {
            holder.referencia.visibility = View.GONE
        } else {
            holder.referencia.text = "Referência: ${peca.Referencia}" // Exibe a referência
        }


        holder.categoria.visibility = View.GONE // Esconde a categoria
        holder.btnFavorito.visibility = View.GONE // Esconde o botão

        mostrarImagemBase64(peca.Imagem, holder.imagem)
    }
    // Retorna o número total de peças
    override fun getItemCount(): Int = pecas.size

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



