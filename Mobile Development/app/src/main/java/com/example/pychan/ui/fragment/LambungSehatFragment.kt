package com.example.pychan.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pychan.R

class LambungSehatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lambung_sehat, container, false)

        // Ambil SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("CheckBoxPrefs", Context.MODE_PRIVATE)
        val predictionResult =
            sharedPreferences.getString("prediction", "Hasil prediksi belum tersedia")
        val lifestyle =
            sharedPreferences.getString("lifestyle", "Informasi gaya hidup belum tersedia")

        // Tampilkan hasil prediksi di TextView
        val tvPredictionResult = view.findViewById<TextView>(R.id.tv_prediction_result)
        val tvLifestyle = view.findViewById<TextView>(R.id.tv_lifestyle)

        tvPredictionResult.text = "Hasil Prediksi: $predictionResult"
        tvLifestyle.text = "Gaya Hidup: $lifestyle"

        return view
    }
}