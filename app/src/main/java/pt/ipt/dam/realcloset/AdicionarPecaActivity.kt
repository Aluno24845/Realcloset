package pt.ipt.dam.realcloset.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.ipt.dam.realcloset.CameraActivity
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.model.Peca
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.retrofit.service.ApiService
import pt.ipt.dam.realcloset.utils.SessionManager
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AdicionarPecaActivity : AppCompatActivity() {

    private lateinit var nomePeca: EditText
    private lateinit var marcaPeca: EditText
    private lateinit var referenciaPeca: EditText
    private lateinit var categoriaPeca: Spinner
    private lateinit var botaoSalvar: Button
    private lateinit var takePhotoIcon: ImageView // Adicionando a referência para o ImageView da câmera

    private lateinit var sessionManager: SessionManager
    var botaoImageByteArray: ByteArray = byteArrayOf(1, 2, 3, 4)
    var caminhoImagem: String = ""

    private fun convertImageToByteArray(uri: Uri): ByteArray {
        try {
            // Abre o InputStream da URI
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.let {
                // Converte o InputStream para um Bitmap
                val bitmap = BitmapFactory.decodeStream(it)
                // Converte o Bitmap para um ByteArray
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }
        } catch (e: Exception) {

        }
        return ByteArray(1)
    }


   private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        cameraActivityResultLauncher.launch(intent)
    }
    private val cameraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            caminhoImagem = result.data?.getStringExtra("IMAGE_URI").toString()
            val uri = Uri.parse(caminhoImagem)
            botaoImageByteArray = convertImageToByteArray( uri);

        } else {
            Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_peca)

        sessionManager = SessionManager(this)

        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()



        nomePeca = findViewById(R.id.add_titulo)
        marcaPeca = findViewById(R.id.add_marca)
        referenciaPeca = findViewById(R.id.add_referencia)
        categoriaPeca = findViewById(R.id.categoria_peca)
        botaoSalvar = findViewById(R.id.botao_salvar)
        takePhotoIcon = findViewById(R.id.takePhotoIcon)  // Inicializa o ImageView da câmera

        // Criação da lista de categorias
        val categorias = listOf("Partes de Cima", "Partes de Baixo", "Calçado", "Acessórios")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriaPeca.adapter = adapter

        // Evento de clique no ícone da câmera
        takePhotoIcon.setOnClickListener {
            // Navegar para a Activity da Câmera
            openCameraActivity()
        }

        botaoSalvar.setOnClickListener {
            val nome = nomePeca.text.toString()
            val marca = marcaPeca.text.toString()
            val referencia = referenciaPeca.text.toString()
            val categoria = categoriaPeca.selectedItem.toString()

            if (nome.isNotEmpty() && marca.isNotEmpty() && referencia.isNotEmpty() && categoria.isNotEmpty()) {

                val peca = Peca(
                    PecaID = -1,
                    Titulo = nome,
                    Imagem = "",
                    Marca = marca,
                    Referencia = referencia,
                    Categoria = categoria,
                    UtilizadorID = -1
                )
                    adicionarPeca(apiService, token, peca)

            } else {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Função para criar o MultipartBody.Part para o arquivo
    fun createMultipartFromByteArray(byteArray: ByteArray, fieldName: String, mimeType: String): MultipartBody.Part {
        val requestBody = RequestBody.create(mimeType.toMediaTypeOrNull(), byteArray)
        return MultipartBody.Part.createFormData(fieldName, "filename.jpg", requestBody)
    }

    // Função para criar o RequestBody a partir do objeto Peca
    fun createRequestBodyFromPeca(peca: Peca): RequestBody {
        val json = Gson().toJson(peca)
        return RequestBody.create("application/json".toMediaTypeOrNull(), json)
    }

    private fun adicionarPeca(apiService: ApiService, token: String, peca: Peca) {
        lifecycleScope.launch {
            try {


                val filePart = createMultipartFromByteArray(botaoImageByteArray, "file", "image/jpeg")
                val pecaRequestBody = createRequestBodyFromPeca(peca)

                val response = apiService.adicionarPeca("Bearer $token", filePart, pecaRequestBody)

                if (response.toString() != "") {
                    Toast.makeText(this@AdicionarPecaActivity, "Peça adicionada com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AdicionarPecaActivity, "Erro ao adicionar a peça", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                println(e)
                Toast.makeText(this@AdicionarPecaActivity, "Erro na comunicação com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
