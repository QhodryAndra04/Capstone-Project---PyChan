package com.example.pychan.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Tombol untuk berpindah ke ChangePasswordActivity
        val tvChangePasswordText: TextView = findViewById(R.id.tvChangePasswordText)
        tvChangePasswordText.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // Tombol untuk membuka GitHub
        val menuGithub = findViewById<LinearLayout>(R.id.menuGithub)
        menuGithub.setOnClickListener {
            val githubUrl = "https://github.com/QhodryAndra04/Capstone-Project---PyChan"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }

    }
}