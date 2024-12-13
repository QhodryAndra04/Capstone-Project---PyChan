package com.example.pychan.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R
import com.example.pychan.data.ChangePasswordRequest
import com.example.pychan.data.ChangePasswordResponse
import com.example.pychan.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_activity)

        // Initialize views
        etOldPassword = findViewById(R.id.etPassword)
        etNewPassword = findViewById(R.id.etCreateNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnChangePassword)
        progressBar = findViewById(R.id.progressBar)

        val sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""
        val savedOldPassword = sharedPreferences.getString("oldPassword", "") ?: ""
        val token = sharedPreferences.getString("token", "") ?: ""

        setupTextWatchers()

        // Handle submit button click
        btnSubmit.setOnClickListener {
            val oldPassword = etOldPassword.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInputs(oldPassword, newPassword, confirmPassword, savedOldPassword)) {
                val request =
                    ChangePasswordRequest(userId, oldPassword, newPassword, confirmPassword)
                sendChangePasswordRequest(request, token)
            }
        }
    }

    private fun setupTextWatchers() {
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
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        savedOldPassword: String
    ): Boolean {
        val isOldPasswordValid = if (oldPassword != savedOldPassword) {
            etOldPassword.error = "Old password is incorrect"
            false
        } else {
            etOldPassword.error = null
            true
        }
        val isNewPasswordValid = validateNewPassword()
        val isConfirmPasswordValid = validateConfirmPassword()

        return isOldPasswordValid && isNewPasswordValid && isConfirmPasswordValid
    }

    private fun sendChangePasswordRequest(request: ChangePasswordRequest, token: String) {
        progressBar.visibility = android.view.View.VISIBLE

        RetrofitClient.apiService.changePassword("Bearer $token", request)
            .enqueue(object : Callback<ChangePasswordResponse> {
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    progressBar.visibility = android.view.View.GONE

                    if (response.isSuccessful) {
                        val message = response.body()?.message ?: "Password changed successfully"
                        showToast(message)

                        val sharedPreferences =
                            getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("oldPassword", request.newPassword)
                            .apply()

                        val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        showToast("Failed to change password: $errorBody")
                        println("API Error: ${response.code()} - $errorBody")
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    progressBar.visibility = android.view.View.GONE
                    showToast("Error: ${t.message}")
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
