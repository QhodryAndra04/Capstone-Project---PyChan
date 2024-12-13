package com.example.pychan.data

import com.google.gson.annotations.SerializedName

// Response model for Login API
data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
)

// Details of the user from Login response
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String,
    val email: String,
    val password: String
)

// Response model for Register API
data class RegisterResponse(
    val message: String,
    val userId: String
)

// Request model for Login API
data class LoginRequest(
    val email: String,
    val password: String
)

// Request model for Register API
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

// ForgotPasswordRequest.kt
data class ForgetPasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)

// ForgotPasswordResponse.kt
data class ForgetPasswordResponse(
    val message: String
)

data class ChangePasswordRequest(
    val userId: String,
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)

// ForgotPasswordResponse.kt
data class ChangePasswordResponse(
    val message: String
)

data class DeleteAccountRequest(val userId: String)

data class DeleteAccountResponse(
    val message: String
)

data class DashboardResponse(
    val error: Boolean,
    val imageUrl: String,
    val definition: String
)

data class ShowResponse(
    val response: String
)

data class CheckBoxResponse(
    @SerializedName("predicted_label") val predicted_label: String,
    @SerializedName("lifestyle") val lifestyle: String
)

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean, // Status keberhasilan API

    @SerializedName("message")
    val message: String // Pesan dari API
)