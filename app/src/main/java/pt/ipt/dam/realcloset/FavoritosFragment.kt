package pt.ipt.dam.realcloset.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.adapter.PecasAdapter
import pt.ipt.dam.realcloset.model.Peca
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.utils.SessionManager

class FavoritosFragment : Fragment() {

    // RecyclerView para exibir a lista de favoritos
    private lateinit var recyclerViewFavoritos: RecyclerView
    private lateinit var sessionManager: SessionManager // Gerenciador de sessão do utilizador

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        recyclerViewFavoritos = view.findViewById(R.id.recyclerViewFavoritos)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o gestor de sessão
        sessionManager = SessionManager(requireContext())
        setupRecyclerView() // Configura a RecyclerView
        fetchFavoritos() // Obtém a lista de favoritos
    }

    // Configura a RecyclerView com um layout linear
    private fun setupRecyclerView() {
        recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
    }

    // Obtém a lista de favoritos
    private fun fetchFavoritos() {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken() // Recupera o token de autenticação

        lifecycleScope.launch {
            try {
                // Chama a API em contexto IO (thread de entrada/saída)
                val response = withContext(Dispatchers.IO) {
                    apiService.getFavoritos("Bearer $token")
                }
                // Define o adaptador com a lista de favoritos
                recyclerViewFavoritos.adapter = PecasAdapter(response, true, response) { peca ->
                    removerFavorito(peca) // Remove favorito ao clicar no item
                }
            } catch (e: Exception) {
                // Mostra uma mensagem se não houver favoritos
                Toast.makeText(requireContext(), "Sem favoritos para mostrar", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Remove uma peça da lista de favoritos
    private fun removerFavorito(peca: Peca) {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken() // Recupera o token de autenticação

        lifecycleScope.launch {
            try {
                // Chama a API para remover o favorito
                apiService.removerFavorito(peca.PecaID, "Bearer $token")
                // Mostra uma mensagem de sucesso
                Toast.makeText(requireContext(), "${peca.Titulo} removido dos favoritos!", Toast.LENGTH_SHORT).show()
                fetchFavoritos() // Atualiza a lista de favoritos
            } catch (e: Exception) {
                // Mostra uma mensagem de erro caso a remoção falhe
                Toast.makeText(requireContext(), "Erro ao remover favorito", Toast.LENGTH_LONG).show()
            }
        }
    }
}
