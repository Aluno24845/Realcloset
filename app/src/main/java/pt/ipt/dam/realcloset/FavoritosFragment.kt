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
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.utils.SessionManager

class FavoritosFragment : Fragment() {

    private lateinit var recyclerViewFavoritos: RecyclerView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        recyclerViewFavoritos = view.findViewById(R.id.recyclerViewFavoritos)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        setupRecyclerView()
        fetchFavoritos()
    }

    private fun setupRecyclerView() {
        recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchFavoritos() {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                val favoritosResponse = withContext(Dispatchers.IO) {
                    apiService.getFavoritos("Bearer $token")
                }

                if (favoritosResponse.isNotEmpty()) {
                    recyclerViewFavoritos.adapter = PecasAdapter(
                        favoritosResponse, true
                    ) { peca ->
                        Toast.makeText(requireContext(), "${peca.Titulo} já está nos favoritos.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Nenhuma peça nos favoritos.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro ao obter favoritos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
