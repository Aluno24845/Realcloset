package pt.ipt.dam.realcloset.model

data class Peca(
    val PecaID: Int,
    val Titulo: String,
    val Imagem: String,  // Aqui pode armazenar URL ou base64 da imagem
    val Marca: String,
    val Referencia: String,
    val Categoria: String,
    val UtilizadorID: Int
)