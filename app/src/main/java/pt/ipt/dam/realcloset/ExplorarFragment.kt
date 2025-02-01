package pt.ipt.dam.realcloset.fragments

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

    private lateinit var recyclerViewPecas: RecyclerView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)
        recyclerViewPecas = view.findViewById(R.id.recyclerViewPecas)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o gerenciador de sessão
        sessionManager = SessionManager(requireContext())

        // Configura o RecyclerView e inicia a procura de peças
        setupRecyclerView()
        fetchPecas()
    }

    private fun setupRecyclerView() {
        // Configura o layout do RecyclerView para exibir itens na vertical
        recyclerViewPecas.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchPecas() {
        val apiService = RetrofitInitializer().apiService()
        val isUserLoggedIn = sessionManager.isUserLoggedIn()

        // Chamada à API para buscar as peças de forma assíncrona
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { apiService.getPecasPublic() }
                if (response.isNotEmpty()) {
                    // Atualiza o RecyclerView com a lista de peças
                    recyclerViewPecas.adapter = PecasAdapter(response, isUserLoggedIn) { peca ->
                        adicionarAosFavoritos(peca)
                    }
                } else {
                    Toast.makeText(requireContext(), "Nenhuma peça encontrada.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao obter peças: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun adicionarAosFavoritos(peca: Peca) {

        val apiService = RetrofitInitializer().apiService()
        val token  = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                // Chamada à API para adicionar a peça aos favoritos
                 withContext(Dispatchers.IO) {
                     apiService.adicionarFavorito(pecaId=peca.PecaID,  "Bearer $token")
                 }
                Toast.makeText(requireContext(), "${peca.Titulo} adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao adicionar favorito: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
