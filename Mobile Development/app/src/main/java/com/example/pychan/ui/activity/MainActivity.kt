package com.example.pychan.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pychan.ui.fragment.DashboardFragment
import com.example.pychan.R
import com.example.pychan.ui.fragment.AlarmFragment
import com.example.pychan.ui.fragment.ConsultationFragment
import com.example.pychan.ui.fragment.LambungSehatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        // Initialize DrawerLayout and Toolbar
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Get user data from SharedPreferences (with default values if not found)
        val userName = sharedPreferences.getString("userName", "Nama Pengguna Default")
            ?: "Nama Pengguna Default"
        val userEmail = sharedPreferences.getString("userEmail", "Email Default") ?: "Email Default"

        // Access the NavigationView header and TextViews inside it
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)

        // Get TextViews from the header
        val userNameTextView = headerView.findViewById<TextView>(R.id.tv_user_name)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.tv_user_email)

        // Set the user data in the TextViews
        userNameTextView.text = userName
        userEmailTextView.text = userEmail

        // Set up DrawerLayout toggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up NavigationView listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }

                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }

                R.id.nav_logout -> {
                    // Logout logic: set isLoggedIn to false
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", false) // Mark as logged out
                    editor.apply()

                    // Redirect to SignInActivity
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Set up BottomNavigationView to switch between fragments
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_lambung_sehat -> loadFragment(LambungSehatFragment())
                R.id.nav_consultation -> loadFragment(ConsultationFragment())
                R.id.nav_alarm -> loadFragment(AlarmFragment())
                else -> false
            }
        }

        // Load the default fragment (DashboardFragment)
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}