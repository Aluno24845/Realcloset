package pt.ipt.dam.realcloset

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import pt.ipt.dam.realcloset.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar o SessionManager
        sessionManager = SessionManager(this)

        // Configurar a Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Remover o título da aplicação na Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Inicializar o DrawerLayout e o NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        updateNavigationMenu()


        // Configurar o botão do menu
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar), R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Ação nos itens do menu
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Só mostrar para utilizadores autenticados
                    if (sessionManager.isUserLoggedIn()) {
                        val fragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
                R.id.nav_wardrobe -> {
                    // Só mostrar para utilizadores autenticados
                    if (sessionManager.isUserLoggedIn()) {
                        val fragment = ClosetFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
                R.id.nav_looks -> {
                    // Só mostrar para utilizadores autenticados
                    if (sessionManager.isUserLoggedIn()) {
                        val fragment = LooksFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
                R.id.nav_favoritos -> {
                    // Só mostrar para utilizadores autenticados
                    if (sessionManager.isUserLoggedIn()) {
                        val fragment = FavoritosFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                    }
                }
                R.id.nav_about -> {
                    // Mostrar sempre, tanto para autenticados quanto para não autenticados
                    val fragment = AboutFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Carregar o fragmento do Sobre como padrão
        if (savedInstanceState == null) {
            val fragment = AboutFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    // toolbar_menu (menu com login/logout/registo)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.apply {
            // Mostrar/ocultar botões com base no estado do login
            findItem(R.id.action_login)?.isVisible = !sessionManager.isUserLoggedIn()
            findItem(R.id.action_register)?.isVisible = !sessionManager.isUserLoggedIn()
            findItem(R.id.action_logout)?.isVisible = sessionManager.isUserLoggedIn()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                // Ação para o botão Login (Abrir a página de Login)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            // Ação para o botão Registar (Abrir a página de Registo)
            R.id.action_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                true
            }
            // Ação para o botão Logout
            R.id.action_logout -> {
                sessionManager.logout() // Fazer logout

                // Substituir o fragmento atual pelo fragmento padrão (AboutFragment)
                val fragment = AboutFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

                // Atualizar o menu de navegação (mostrar apenas a opção "Sobre")
                updateNavigationMenu()

                // Atualizar a Toolbar (esconder botão de logout)
                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Função para atualizar os itens de navegação após o logout
    private fun updateNavigationMenu() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu

        if (sessionManager.isUserLoggedIn()) {
            // Utilizadores autenticados: mostrar todos os itens, exceto "Sobre"
            menu.findItem(R.id.nav_profile).isVisible = true
            menu.findItem(R.id.nav_wardrobe).isVisible = true
            menu.findItem(R.id.nav_looks).isVisible = true
            menu.findItem(R.id.nav_about).isVisible = true  // "Sobre" também é visível
        } else {
            // Utilizadores não autenticados: mostrar apenas "Sobre"
            menu.findItem(R.id.nav_profile).isVisible = false
            menu.findItem(R.id.nav_wardrobe).isVisible = false
            menu.findItem(R.id.nav_looks).isVisible = false
            menu.findItem(R.id.nav_about).isVisible = true  // "Sobre" é visível
        }
    }
}
