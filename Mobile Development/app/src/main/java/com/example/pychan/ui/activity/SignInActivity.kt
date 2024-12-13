package com.example.pychan.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R
import com.example.pychan.data.LoginRequest
import com.example.pychan.data.LoginResponse
import com.example.pychan.retrofit.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailInputLayout = findViewById<TextInputLayout>(R.id.emailInputLayout)
        val emailEditText = findViewById<TextInputEditText>(R.id.etEmail)
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val passwordEditText = findViewById<TextInputEditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnSignIn)
        val registerTextView = findViewById<TextView>(R.id.tvSignUp)
        val forgotPasswordTextView = findViewById<TextView>(R.id.tvForgotPassword)
        val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    passwordInputLayout.error = "Password must be at least 8 characters"
                } else {
                    passwordInputLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) {
                emailInputLayout.error = "Email must not be empty"
                return@setOnClickListener
            } else {
                emailInputLayout.error = null
            }

            if (password.isEmpty()) {
                passwordInputLayout.error = "Password must not be empty"
                return@setOnClickListener
            } else {
                passwordInputLayout.error = null
            }

            if (emailInputLayout.error != null || passwordInputLayout.error != null) {
                Toast.makeText(this, "Please fix the errors before logging in", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            loadingIndicator.visibility = View.VISIBLE

            val loginRequest = LoginRequest(email = email, password = password)

            RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loadingIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.error == false) {
                            val userName = loginResponse.loginResult?.name
                            val userEmail = loginResponse.loginResult?.email
                            val token = loginResponse.loginResult?.token
                            val userPassword = loginResponse.loginResult?.password // Ambil password
                            val userId = loginResponse.loginResult?.userId // Ambil userId

                            // Simpan data ke SharedPreferences
                            val sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userName", userName)
                            editor.putString("userEmail", userEmail)
                            editor.putString("token", token)
                            editor.putString("password", userPassword) // Simpan password
                            editor.putString("userId", userId) // Simpan userId
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()

                            // Tampilkan welcome message
                            Toast.makeText(
                                this@SignInActivity,
                                "Welcome, $userName",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Arahkan ke MainActivity setelah login berhasil
                            val intent = Intent(this@SignInActivity, CheckBoxActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                loginResponse?.message ?: "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            "Server error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loadingIndicator.visibility = View.GONE
                    Toast.makeText(this@SignInActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}
