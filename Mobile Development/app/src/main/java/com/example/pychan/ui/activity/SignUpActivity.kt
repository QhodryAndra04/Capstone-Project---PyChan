package com.example.pychan.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R
import com.example.pychan.data.RegisterRequest
import com.example.pychan.data.RegisterResponse
import com.example.pychan.retrofit.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        usernameInputLayout = findViewById(R.id.usernameInputLayout)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        btnSignUp = findViewById(R.id.btnSignUp)

        // Add TextWatchers
        addTextWatchers()

        // Handle Sign Up button click
        btnSignUp.setOnClickListener {
            if (isFormValid()) {
                signUp()
            }
        }
    }

    private fun addTextWatchers() {
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val username = s.toString()
                if (username.isEmpty()) {
                    usernameInputLayout.error = "Username harus diisi"
                } else {
                    usernameInputLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isEmpty()) {
                    emailInputLayout.error = "Email harus diisi"
                } else {
                    emailInputLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    passwordInputLayout.error = "Password harus diisi"
                } else if (password.length < 8) {
                    passwordInputLayout.error = "Password minimal 8 karakter"
                } else {
                    passwordInputLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val confirmPassword = s.toString()
                val password = etPassword.text.toString()
                if (confirmPassword.isEmpty()) {
                    confirmPasswordInputLayout.error = "Konfirmasi Password harus diisi"
                } else if (confirmPassword != password) {
                    confirmPasswordInputLayout.error = "Password tidak cocok"
                } else {
                    confirmPasswordInputLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun signUp() {
        // Collect form data
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        // Show loading indicator
        loadingIndicator.visibility = ProgressBar.VISIBLE

        // Create com.example.pychan.data.RegisterRequest object
        val registerRequest = RegisterRequest(
            username = username,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        // Call the registration API
        RetrofitClient.apiService.register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    // Hide loading indicator
                    loadingIndicator.visibility = ProgressBar.GONE

                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        Toast.makeText(
                            this@SignUpActivity,
                            registerResponse?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to SignInActivity
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Server error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    loadingIndicator.visibility = ProgressBar.GONE
                    Toast.makeText(this@SignUpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun isFormValid(): Boolean {
        return usernameInputLayout.error == null &&
                emailInputLayout.error == null &&
                passwordInputLayout.error == null &&
                confirmPasswordInputLayout.error == null
    }
}