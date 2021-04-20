package com.appttude.h_mal.atlas_weather.monoWeather.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.monoWeather.ui.settings.UnitSettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_navigation.*

class MainActivity : AppCompatActivity() {

    lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        setSupportActionBar(toolbar)

        navHost = supportFragmentManager
                .findFragmentById(R.id.container) as NavHostFragment
        val navController = navHost.navController
        navController.setGraph(R.navigation.main_navigation)

        setupBottomBar(navView, navController)
    }

    private fun setupBottomBar(navView: BottomNavigationView, navController: NavController) {
        val tabs = setOf(R.id.nav_home, R.id.nav_world)
        val appBarConfiguration = AppBarConfiguration(tabs)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val i = Intent(this, UnitSettingsActivity::class.java)
                startActivity(i)
                return true
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}