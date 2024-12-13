package com.example.pychan.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pychan.R

class DashboardFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ivImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Inisialisasi elemen-elemen UI
        tvTitle = rootView.findViewById(R.id.tv_title_dashboard)
        tvDescription = rootView.findViewById(R.id.tv_description_dashboard)
        ivImage = rootView.findViewById(R.id.iv_image_dashboard)

        // Set teks dan gambar secara programatik
        tvTitle.text = "Apa itu penyakit Asam Lambung?"
        tvDescription.text = """
            Penyakit asam lambung, atau dalam istilah medis dikenal sebagai Gastroesophageal Reflux Disease (GERD), adalah suatu kondisi di mana asam lambung naik ke kerongkongan (esofagus), saluran yang menghubungkan mulut dan lambung, sehingga menyebabkan iritasi.
            Penyakit ini dapat menyerang siapa saja, tanpa memandang usia maupun jenis kelamin, dan biasanya ditandai dengan gejala utama berupa rasa terbakar di dada (heartburn), terutama setelah makan atau ketika berbaring. Kondisi ini terjadi karena melemahnya otot kerongkongan bagian bawah (otot LES), yang seharusnya menutup setelah makanan masuk ke lambung. Jika otot ini melemah, kerongkongan akan tetap terbuka, sehingga asam lambung dapat naik ke kerongkongan.
            GERD yang tidak ditangani dengan baik dapat menyebabkan komplikasi serius, seperti peradangan pada esofagus (esofagitis), luka pada jaringan kerongkongan yang membuat sulit menelan, dan kondisi yang lebih serius seperti Barrett's esophagus, yang berisiko meningkatkan peluang terjadinya kanker esofagus.
        """.trimIndent()
        ivImage.setImageResource(R.drawable.asam_lambung) // Pastikan Anda memiliki gambar dengan nama 'asam_lambung'

        return rootView
    }
}