package com.example.pychan.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R
import com.example.pychan.data.ForgetPasswordRequest
import com.example.pychan.data.ForgetPasswordResponse
import com.example.pychan.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etOldPassword = findViewById(R.id.etPassword)
        etNewPassword = findViewById(R.id.etCreateNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnResetPassword)
        progressBar = findViewById(R.id.progressBar)

        val sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""
        val savedOldPassword = sharedPreferences.getString("oldPassword", "") ?: ""

        setupTextWatchers()

        // Handle submit button click
        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val oldPassword = etOldPassword.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInputs(
                    email,
                    oldPassword,
                    newPassword,
                    confirmPassword,
                    savedOldPassword
                )
            ) {
                val request =
                    ForgetPasswordRequest(email, oldPassword, newPassword, confirmPassword)
                sendForgetPasswordRequest(request, token)
            }
        }
    }

    private fun setupTextWatchers() {
        etEmail.addTextChangedListener(createTextWatcher { validateEmail() })
        etOldPassword.addTextChangedListener(createTextWatcher { validateOldPassword() })
        etNewPassword.addTextChangedListener(createTextWatcher { validateNewPassword() })
        etConfirmPassword.addTextChangedListener(createTextWatcher { validateConfirmPassword() })
    }

    private fun createTextWatcher(validation: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validation()
            }
        }
    }

    private fun validateEmail(): Boolean {
        return if (etEmail.text.toString().trim().isEmpty()) {
            etEmail.error = "Email cannot be empty"
            false
        } else {
            etEmail.error = null
            true
        }
    }

    private fun validateOldPassword(): Boolean {
        return if (etOldPassword.text.toString().trim().isEmpty()) {
            etOldPassword.error = "Old password cannot be empty"
            false
        } else {
            etOldPassword.error = null
            true
        }
    }

    private fun validateNewPassword(): Boolean {
        val newPassword = etNewPassword.text.toString().trim()
        return when {
            newPassword.isEmpty() -> {
                etNewPassword.error = "New password cannot be empty"
                false
            }

            newPassword.length < 8 -> {
                etNewPassword.error = "New password must be at least 8 characters"
                false
            }

            else -> {
                etNewPassword.error = null
                true
            }
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = etConfirmPassword.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()
        return when {
            confirmPassword.isEmpty() -> {
                etConfirmPassword.error = "Please confirm your new password"
                false
            }

            confirmPassword != newPassword -> {
                etConfirmPassword.error = "Passwords do not match"
                false
            }

            else -> {
                etConfirmPassword.error = null
                true
            }
        }
    }

    private fun validateInputs(
        email: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        savedOldPassword: String
    ): Boolean {
        val isEmailValid = validateEmail()
        val isOldPasswordValid = if (oldPassword != savedOldPassword) {
            etOldPassword.error = "Old password is incorrect"
            false
        } else {
            etOldPassword.error = null
            true
        }
        val isNewPasswordValid = validateNewPassword()
        val isConfirmPasswordValid = validateConfirmPassword()

        return isEmailValid && isOldPasswordValid && isNewPasswordValid && isConfirmPasswordValid
    }

    private fun sendForgetPasswordRequest(request: ForgetPasswordRequest, token: String) {
        progressBar.visibility = android.view.View.VISIBLE

        RetrofitClient.apiService.forgotPassword("Bearer $token", request)
            .enqueue(object : Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                ) {
                    progressBar.visibility = android.view.View.GONE

                    if (response.isSuccessful) {
                        // Update the stored old password
                        val sharedPreferences =
                            getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("oldPassword", request.newPassword)
                            .apply()

                        val message = response.body()?.message ?: "Password updated successfully"
                        showToast(message)

                        val intent = Intent(this@ForgetPasswordActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Failed to update password")
                    }
                }

                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                    progressBar.visibility = android.view.View.GONE
                    showToast("Error: ${t.message}")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}