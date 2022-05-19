package com.melvin.ongandroid.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.melvin.ongandroid.R
import com.melvin.ongandroid.application.SomosMasApp.Companion.prefs
import com.melvin.ongandroid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        drawerLayout = binding.drawerLayout
        navView = binding.navView

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener { itemMenu ->
            navigationFragments(itemMenu)
            true
        }

        binding.navView.bringToFront()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigationFragments(item: MenuItem) {
        when (item.itemId) {
            // navigate to the "HOME" view
            R.id.inicio_menu -> {
                Toast.makeText(applicationContext, getString(R.string.inicio), Toast.LENGTH_SHORT)
                    .show()

                //Navigation to Home fragment
                changeFragment(HomeFragment())
            }

            R.id.actividades_menu -> {
                Toast.makeText(applicationContext, getString(R.string.actividades), Toast.LENGTH_SHORT)
                    .show()

                //Navigation to Activities fragment
                changeFragment(ActivitiesFragment())
            }

            R.id.novedades_menu -> {
                Toast.makeText(applicationContext, getString(R.string.novedades), Toast.LENGTH_SHORT)
                .show()
                
                //Navigation to News fragment
                changeFragment(NewsFragment())
            }

            R.id.testimonios_menu -> {
                Toast.makeText(applicationContext, getString(R.string.testimonios), Toast.LENGTH_SHORT)
                    .show()
            }

            // navigate to the "ABOUT" view
            R.id.nosotros_menu -> {
                Toast.makeText(applicationContext, getString(R.string.nosotros), Toast.LENGTH_SHORT)
                    .show()
                //Navigation to About Us fragment
                changeFragment(AboutUsFragment())
            }

            // navigate to the "CONTACT" view
            R.id.contacto_menu -> {
                Toast.makeText(applicationContext, getString(R.string.contacto), Toast.LENGTH_SHORT)
                    .show()
                changeFragment(ContactFragment())
            }
            // close session - 2022-05-17 L.Gomez
            R.id.close_session -> {
                Toast.makeText(applicationContext, getString(R.string.closing_session), Toast.LENGTH_SHORT)
                    .show()
                closeSession()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    // Replace the fragment in the ContainerView
    private fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragmentContainerView,frag).commit()
    }

    // Actions to do when user tries to close session - 2022-05-17 L.Gomez
    private fun closeSession() {
        // Delete user token
        prefs.deleteUserToken()

        // Go to splash Activity
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}