package pt.ipt.dam.realcloset.model

data class Peca(
    val PecaID: Int,
    val Titulo: String,
    val Imagem: String,  // Aqui pode armazenar URL ou base64 da imagem
    val Marca: String,
    val Referencia: String,
    val Categoria: String,
    val UtilizadorID: Int
) {
    // Sobrescreve o método equals para garantir que a comparação de objetos Peca
    // seja feita com base no PecaID, e não apenas nas referências de memória
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Peca

        return PecaID == other.PecaID
    }
    // Sobrescreve o método hashCode para garantir que objetos com o mesmo PecaID tenham o mesmo código hash.
    override fun hashCode(): Int {
        return PecaID
    }
}