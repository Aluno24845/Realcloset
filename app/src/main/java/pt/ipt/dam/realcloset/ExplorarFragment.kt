package pt.ipt.dam.realcloset.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipt.dam.realcloset.adapter.PecasAdapter
import pt.ipt.dam.realcloset.model.Peca
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.utils.SessionManager

class ExplorarFragment : Fragment() {

    // RecyclerView para exibir a lista de peças
    private lateinit var recyclerViewPecas: RecyclerView
    // Gestor de sessão do utilizador
    private lateinit var sessionManager: SessionManager
    // Lista de peças favoritas
    private var pecasFavoritas: List<Peca> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)
        // Inicializa o RecyclerView
        recyclerViewPecas = view.findViewById(R.id.recyclerViewPecas)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializa o gestor de sessão do utilizador
        sessionManager = SessionManager(requireContext())
        // Configura o RecyclerView
        setupRecyclerView()
        // Vai buscar os favoritos antes das peças todas
        fetchFavoritos()
    }

    // Configura o layout do RecyclerView
    private fun setupRecyclerView() {
        recyclerViewPecas.layoutManager = LinearLayoutManager(requireContext())
    }

    // Vai buscar a lista de favoritos
    private fun fetchFavoritos() {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                // Solicita os favoritos numa thread separada
                val response = withContext(Dispatchers.IO) { apiService.getFavoritos("Bearer $token") }
                pecasFavoritas = response
            } catch (e: Exception) {
                // Ignora erro para garantir o carregamento das peças
            } finally {
                // Busca as peças, independentemente do resultado dos favoritos
                fetchPecas()
            }
        }
    }

    // Vai buscar a lista de peças públicas
    private fun fetchPecas() {
        val apiService = RetrofitInitializer().apiService()
        val isUserLoggedIn = sessionManager.isUserLoggedIn()

        lifecycleScope.launch {
            try {
                // Solicita as peças públicas numa thread separada
                val response = withContext(Dispatchers.IO) { apiService.getPecasPublic() }

                if (response.isNotEmpty()) {
                    // Configura o adapter com as peças e favoritos
                    val adapter = PecasAdapter(response, isUserLoggedIn, pecasFavoritas) { peca ->
                        toggleFavorito(peca)
                    }
                    recyclerViewPecas.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Sem peças para mostrar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao obter peças", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Adiciona ou remove uma peça dos favoritos
    private fun toggleFavorito(peca: Peca) {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                if (pecasFavoritas.contains(peca)) {
                    // Se a peça já for favorita, remove-a
                    apiService.removerFavorito(peca.PecaID, "Bearer $token")
                    pecasFavoritas = pecasFavoritas.filterNot { it.PecaID == peca.PecaID }
                    Toast.makeText(requireContext(), "${peca.Titulo} removido dos favoritos!", Toast.LENGTH_SHORT).show()
                } else {
                    // Se a peça não for favorita, adiciona-a
                    apiService.adicionarFavorito(peca.PecaID, "Bearer $token")
                    pecasFavoritas = pecasFavoritas + peca
                    Toast.makeText(requireContext(), "${peca.Titulo} adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                }

                // Atualiza a interface com a nova lista de favoritos
                recyclerViewPecas.adapter?.let {
                    (it as PecasAdapter).updateFavoritos(pecasFavoritas)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao atualizar favorito", Toast.LENGTH_LONG).show()
            }
        }
    }
}
