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
import pt.ipt.dam.realcloset.retrofit.RetrofitInitializer
import pt.ipt.dam.realcloset.utils.SessionManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dam.realcloset.R
import pt.ipt.dam.realcloset.adapter.ClosetAdapter

class ClosetFragment : Fragment() {

    private lateinit var recyclerPartesCima: RecyclerView
    private lateinit var recyclerPartesBaixo: RecyclerView
    private lateinit var recyclerCalcado: RecyclerView
    private lateinit var recyclerAcessorios: RecyclerView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        recyclerPartesCima = view.findViewById(R.id.recycler_partes_cima)
        recyclerPartesBaixo = view.findViewById(R.id.recycler_partes_baixo)
        recyclerCalcado = view.findViewById(R.id.recycler_calcado)
        recyclerAcessorios = view.findViewById(R.id.recycler_acessorios)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o gestor de sessão do utilizador
        sessionManager = SessionManager(requireContext())
        // Configura o RecyclerView
        setupRecyclerView()

        fetchPecas()
    }

    // Configura o layout do RecyclerView
    private fun setupRecyclerView() {
        recyclerPartesCima.layoutManager = LinearLayoutManager(context)
        recyclerPartesBaixo.layoutManager = LinearLayoutManager(context)
        recyclerCalcado.layoutManager = LinearLayoutManager(context)
        recyclerAcessorios.layoutManager = LinearLayoutManager(context)

    }

    private fun fetchPecas() {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                // Solicita as peças
                val response = withContext(Dispatchers.IO) { apiService.getPecas("Bearer $token") }

                // Verifica se a resposta tem peças
                if (response.isNotEmpty()) {
                    // Agrupar as peças por categoria
                    val partesCima = response.filter { it.Categoria == "Partes de Cima" }
                    val partesBaixo = response.filter { it.Categoria == "Partes de Baixo" }
                    val calcado = response.filter { it.Categoria == "Calçado" }
                    val acessorios = response.filter { it.Categoria == "Acessórios" }

                    // Verifica se alguma lista está vazia antes de passar para o adapter
                    if (partesCima.isNotEmpty()) {
                        recyclerPartesCima.adapter = ClosetAdapter(partesCima)
                    }
                    if (partesBaixo.isNotEmpty()) {
                        recyclerPartesBaixo.adapter = ClosetAdapter(partesBaixo)
                    }
                    if (calcado.isNotEmpty()) {
                        recyclerCalcado.adapter = ClosetAdapter(calcado)
                    }
                    if (acessorios.isNotEmpty()) {
                        recyclerAcessorios.adapter = ClosetAdapter(acessorios)
                    }

                } else {
                    // Caso não tenha peças, pode exibir uma mensagem ou tratar de forma apropriada
                    Toast.makeText(requireContext(), "Sem peças para mostrar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Tratar erros, como problemas de rede
                Toast.makeText(requireContext(), "Erro ao obter peças: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }





}
