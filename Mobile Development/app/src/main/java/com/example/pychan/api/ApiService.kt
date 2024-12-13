package com.example.pychan.api

import com.example.pychan.data.ChangePasswordRequest
import com.example.pychan.data.ChangePasswordResponse
import com.example.pychan.data.CheckBoxResponse
import com.example.pychan.data.DashboardResponse
import com.example.pychan.data.DeleteAccountRequest
import com.example.pychan.data.DeleteAccountResponse
import com.example.pychan.data.ForgetPasswordRequest
import com.example.pychan.data.ForgetPasswordResponse
import com.example.pychan.data.LoginRequest
import com.example.pychan.data.LoginResponse
import com.example.pychan.data.RegisterRequest
import com.example.pychan.data.RegisterResponse
import com.example.pychan.data.ShowResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Login API call
    @POST("api/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Register API call
    @POST("api/auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("api/auth/forget-password")
    fun forgotPassword(
        @Header("Authorization") token: String,
        @Body request: ForgetPasswordRequest
    ): Call<ForgetPasswordResponse>


    @POST("api/auth/change-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Call<ChangePasswordResponse>


    @DELETE("api/auth/delete-account")
    fun deleteAccount(
        @Header("Authorization") token: String,
        @Body request: DeleteAccountRequest
    ): Call<DeleteAccountResponse>


    @GET("api/auth/dashboard")
    fun getDashboardData(
        @Header("Authorization") token: String
    ): Call<DashboardResponse>


    @POST("user_input/")
    fun submitUserInput(
        @Query("umur") umur: Int,
        @Query("bb") bb: Int,
        @Query("rokok") rokok: Int,
        @Query("polaMakan") polaMakan: Int,
        @Query("makanPedas") makanPedas: Int,
        @Query("minumSoda") minumSoda: Int,
        @Query("tidurMakan") tidurMakan: Int,
        @Query("olahraga") olahraga: Int,
        @Query("stres") stres: Int,
        @Query("obat") obat: Int,
        @Query("makanLemak") makanLemak: Int,
        @Query("minumKopi") minumKopi: Int,
        @Query("minumAlkohol") minumAlkohol: Int,
        @Query("mual") mual: Int,
        @Query("perutKembung") perutKembung: Int,
        @Query("sakitTenggorokan") sakitTenggorokan: Int,
        @Query("kesulitanMenelan") kesulitanMenelan: Int,
        @Query("tidakAda") tidakAda: Int,
        @Query("sendawa") sendawa: Int,
        @Query("panasDada") panasDada: Int,
        @Query("nyeriPerut") nyeriPerut: Int
    ): Call<CheckBoxResponse>

    @POST("show_respon/")
    fun showResponse(
        @Query("input") input: String
    ): Call<ShowResponse>

}