package com.example.pychan.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.R
import com.example.pychan.data.CheckBoxResponse
import com.example.pychan.retrofit.RetrofitClient.mlApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckBoxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_box)

        val inputUmur = findViewById<EditText>(R.id.input_umur)
        val inputBB = findViewById<EditText>(R.id.input_bb)
        val tvPredictionResult = findViewById<TextView>(R.id.tvPredictionResult)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)

        // Inisialisasi checkbox
        val checkboxes = mapOf(
            "rokok" to findViewById<CheckBox>(R.id.checkbox_rokok),
            "polaMakan" to findViewById<CheckBox>(R.id.checkbox_pola_makan),
            "makanPedas" to findViewById<CheckBox>(R.id.checkbox_makan_pedas),
            "minumSoda" to findViewById<CheckBox>(R.id.checkbox_minum_soda),
            "tidurMakan" to findViewById<CheckBox>(R.id.checkbox_tidur_makan),
            "olahraga" to findViewById<CheckBox>(R.id.checkbox_olahraga),
            "stres" to findViewById<CheckBox>(R.id.checkbox_stres),
            "obat" to findViewById<CheckBox>(R.id.checkbox_obat),
            "makanLemak" to findViewById<CheckBox>(R.id.checkbox_makan_lemak),
            "minumKopi" to findViewById<CheckBox>(R.id.checkbox_minum_kopi),
            "minumAlkohol" to findViewById<CheckBox>(R.id.checkbox_minum_alkohol),
            "mual" to findViewById<CheckBox>(R.id.checkbox_mual),
            "perutKembung" to findViewById<CheckBox>(R.id.checkbox_perut_kembung),
            "sakitTenggorokan" to findViewById<CheckBox>(R.id.checkbox_sakit_tenggorokan),
            "kesulitanMenelan" to findViewById<CheckBox>(R.id.checkbox_kesulitan_menelan),
            "tidakAda" to findViewById<CheckBox>(R.id.checkbox_tidak_ada),
            "sendawa" to findViewById<CheckBox>(R.id.checkbox_sendawa),
            "panasDada" to findViewById<CheckBox>(R.id.checkbox_panas_dada),
            "nyeriPerut" to findViewById<CheckBox>(R.id.checkbox_nyeri_perut)
        )

        val btnLanjutMasuk: Button = findViewById(R.id.btn_lanjut_masuk)
        btnLanjutMasuk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("CheckBoxPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Cek apakah ada hasil prediksi sebelumnya
        val previousPrediction =
            sharedPreferences.getString("prediction", "Belum ada hasil prediksi")
        tvPredictionResult.text = "Hasil Sebelumnya: $previousPrediction"

        btnSubmit.setOnClickListener {
            val umur = inputUmur.text.toString().toIntOrNull()
            val bb = inputBB.text.toString().toIntOrNull()

            if (umur == null || bb == null) {
                Toast.makeText(
                    this,
                    "Umur dan berat badan harus diisi dengan angka!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Ambil nilai dari semua checkbox
            val checkboxValues = checkboxes.mapValues { if (it.value.isChecked) 1 else 0 }

            // Panggil API
            mlApiService.submitUserInput(
                umur,
                bb,
                checkboxValues["rokok"] ?: 0,
                checkboxValues["polaMakan"] ?: 0,
                checkboxValues["makanPedas"] ?: 0,
                checkboxValues["minumSoda"] ?: 0,
                checkboxValues["tidurMakan"] ?: 0,
                checkboxValues["olahraga"] ?: 0,
                checkboxValues["stres"] ?: 0,
                checkboxValues["obat"] ?: 0,
                checkboxValues["makanLemak"] ?: 0,
                checkboxValues["minumKopi"] ?: 0,
                checkboxValues["minumAlkohol"] ?: 0,
                checkboxValues["mual"] ?: 0,
                checkboxValues["perutKembung"] ?: 0,
                checkboxValues["sakitTenggorokan"] ?: 0,
                checkboxValues["kesulitanMenelan"] ?: 0,
                checkboxValues["tidakAda"] ?: 0,
                checkboxValues["sendawa"] ?: 0,
                checkboxValues["panasDada"] ?: 0,
                checkboxValues["nyeriPerut"] ?: 0
            ).enqueue(object : Callback<CheckBoxResponse> {
                override fun onResponse(
                    call: Call<CheckBoxResponse>,
                    response: Response<CheckBoxResponse>
                ) {
                    if (response.isSuccessful) {
                        val prediction = response.body()?.predicted_label ?: "Hasil tidak tersedia"
                        val lifestyle =
                            response.body()?.lifestyle ?: "Tidak ada informasi gaya hidup"

                        // Simpan hasil ke SharedPreferences
                        editor.putString("prediction", prediction)
                        editor.putString("lifestyle", lifestyle)
                        editor.apply()

                        // Tampilkan hanya prediksi pada checkbox
                        tvPredictionResult.text = "Hasil Prediksi: $prediction"
                    } else {
                        tvPredictionResult.text = "Gagal mengirim data!"
                    }
                }

                override fun onFailure(call: Call<CheckBoxResponse>, t: Throwable) {
                    Toast.makeText(this@CheckBoxActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
}