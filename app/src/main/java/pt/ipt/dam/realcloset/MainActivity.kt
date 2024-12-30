package pt.ipt.dam.realcloset

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar a Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Inicializar o DrawerLayout e o NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

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
                    // Quando o item "Perfil" for selecionado, carregar o Fragment de Perfil
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                }
                R.id.nav_wardrobe -> {
                    // Quando o item "Guarda-Roupa" for selecionado, carregar o Fragment de Closet
                    val fragment = ClosetFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                }
                R.id.nav_looks -> {
                    // Quando o item "Looks" for selecionado, carregar o Fragment de Looks
                    val fragment = LooksFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                }
                R.id.nav_about -> {
                    // Quando o item "Sobre" for selecionado, carregar o Fragment "Sobre"
                    val fragment = AboutFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                }
            }
            drawerLayout.closeDrawers() // Fecha o menu após a seleção
            true
        }

        // Carregar o fragmento de guarda-roupa (ou outro por padrão) quando a app iniciar
        if (savedInstanceState == null) {
            val fragment = ClosetFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
