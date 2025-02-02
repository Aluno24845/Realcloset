package pt.ipt.dam.realcloset.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import pt.ipt.dam.realcloset.activities.AdicionarPecaActivity
import pt.ipt.dam.realcloset.adapter.ClosetAdapter
import pt.ipt.dam.realcloset.model.Peca

class ClosetFragment : Fragment() {

    private lateinit var recyclerPartesCima: RecyclerView
    private lateinit var recyclerPartesBaixo: RecyclerView
    private lateinit var recyclerCalcado: RecyclerView
    private lateinit var recyclerAcessorios: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var adicionarButton: Button  // Definir o botão

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        recyclerPartesCima = view.findViewById(R.id.recycler_partes_cima)
        recyclerPartesBaixo = view.findViewById(R.id.recycler_partes_baixo)
        recyclerCalcado = view.findViewById(R.id.recycler_calcado)
        recyclerAcessorios = view.findViewById(R.id.recycler_acessorios)
        adicionarButton = view.findViewById(R.id.adicionar_button)  // Referencia o botão

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o gestor de sessão do utilizador
        sessionManager = SessionManager(requireContext())
        // Configura o RecyclerView
        setupRecyclerView()

        fetchPecas()

        // Configura o clique no botão "Adicionar"
        adicionarButton.setOnClickListener {
            Toast.makeText(requireContext(), "Botão clicado!", Toast.LENGTH_SHORT).show()
            // Inicia a AdicionarPecaActivity quando o botão for clicado
            val intent = Intent(requireContext(), AdicionarPecaActivity::class.java)
            startActivity(intent)
        }
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

                    // Passa a função de remoção para o adapter
                    recyclerPartesCima.adapter = ClosetAdapter(partesCima.toMutableList()) { peca ->
                        removePeca(peca)
                    }
                    recyclerPartesBaixo.adapter = ClosetAdapter(partesBaixo.toMutableList()) { peca ->
                        removePeca(peca)
                    }
                    recyclerCalcado.adapter = ClosetAdapter(calcado.toMutableList()) { peca ->
                        removePeca(peca)
                    }
                    recyclerAcessorios.adapter = ClosetAdapter(acessorios.toMutableList()) { peca ->
                        removePeca(peca)
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

    private fun removePeca(peca: Peca) {
        val apiService = RetrofitInitializer().apiService()
        val token = sessionManager.getAuthToken()

        lifecycleScope.launch {
            try {
                // Faz a solicitação para remover a peça
                val response = withContext(Dispatchers.IO) {
                    apiService.removerPeca( peca.PecaID, "Bearer $token")
                }

                // Verifica se houve resposta
                if (response != null) {
                    // Sucesso: peça removida com sucesso
                    Toast.makeText(requireContext(), "Peça removida com sucesso", Toast.LENGTH_LONG).show()

                    // Aqui você pode fazer o fetch novamente para atualizar a lista de peças
                    fetchPecas()  // Atualiza a lista de peças após a remoção

                } else {
                    // Falha: a resposta da API não indica sucesso
                    Toast.makeText(requireContext(), "Falha ao remover peça", Toast.LENGTH_LONG).show()
                }


            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erro de rede: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
