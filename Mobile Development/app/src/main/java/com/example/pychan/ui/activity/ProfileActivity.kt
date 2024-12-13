package com.example.pychan.ui.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.Manifest
import android.content.SharedPreferences
import androidx.core.app.ActivityCompat
import com.example.pychan.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnUploadPicture: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    // Ambil gambar dan ubah ke bentuk lingkaran
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    ivProfilePicture.setImageBitmap(getCircularBitmap(bitmap))

                    // Simpan gambar ke SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("profile_image", selectedImageUri.toString())
                    editor.apply()
                } else {
                    Toast.makeText(this, "Gagal memuat gambar.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        btnUploadPicture = findViewById(R.id.btnUploadPicture)

        // Set data user dari SharedPreferences
        tvUserName.text = sharedPreferences.getString("userName", "Nama Pengguna Default")
        tvUserEmail.text = sharedPreferences.getString("userEmail", "Email Default")

        // Cek izin kamera dan galeri
        if (!hasPermissions()) {
            requestPermissions()
        }

        // Tombol untuk mengunggah gambar
        btnUploadPicture.setOnClickListener {
            if (hasPermissions()) {
                showImagePickerOptions()
            } else {
                Toast.makeText(this, "Izin belum diberikan", Toast.LENGTH_SHORT).show()
            }
        }

        // Load the profile image from SharedPreferences if it exists
        val imageUriString = sharedPreferences.getString("profile_image", null)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            ivProfilePicture.setImageURI(imageUri)
        }
    }

    // Menampilkan opsi untuk memilih gambar dari galeri
    private fun showImagePickerOptions() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    // Fungsi untuk memeriksa izin
    private fun hasPermissions(): Boolean {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Meminta izin jika belum diberikan
    private fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 100)
    }

    // Hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Izin diberikan.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin ditolak. Fitur gambar tidak dapat digunakan.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk memotong gambar menjadi lingkaran
    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = Math.min(bitmap.width, bitmap.height)
        val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = -0x1000000
        val rect = android.graphics.Rect(0, 0, size, size)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((size / 2).toFloat(), (size / 2).toFloat(), (size / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return result
    }
}
