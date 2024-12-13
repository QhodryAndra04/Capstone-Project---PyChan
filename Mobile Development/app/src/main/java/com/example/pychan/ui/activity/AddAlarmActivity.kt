package com.example.pychan.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.pychan.Alarm
import com.example.pychan.R

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var selectedDays: String
    private lateinit var checkBoxes: List<CheckBox>
    private var isShakeEnabled: Boolean = false
    private var notificationSoundUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val btnSave = findViewById<ImageView>(R.id.btnSave)
        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNotification = findViewById<LinearLayout>(R.id.btnNotification)
        val btnShake = findViewById<LinearLayout>(R.id.btnShake)
        val btnLabel = findViewById<LinearLayout>(R.id.btnLabel)
        val btnDelete = findViewById<LinearLayout>(R.id.btnDelete)

        checkBoxes = listOf(
            findViewById<CheckBox>(R.id.checkSunday),
            findViewById<CheckBox>(R.id.checkMonday),
            findViewById<CheckBox>(R.id.checkTuesday),
            findViewById<CheckBox>(R.id.checkWednesday),
            findViewById<CheckBox>(R.id.checkThursday),
            findViewById<CheckBox>(R.id.checkFriday),
            findViewById<CheckBox>(R.id.checkSaturday)
        )

        var selectedDays = ""

        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDays += "${getFullDayName(checkBox.text.toString())}, "
                } else {
                    selectedDays =
                        selectedDays.replace("${getFullDayName(checkBox.text.toString())}, ", "")
                }
                checkAllDaysSelected()
            }
        }

        loadCheckBoxStatus()
        loadShakeStatus()
        loadNotificationSound()

        btnShake.setOnClickListener {
            isShakeEnabled = !isShakeEnabled
            saveShakeStatus()
        }

        btnNotification.setOnClickListener {
            val intent = Intent(Settings.ACTION_SOUND_SETTINGS)
            startActivityForResult(intent, REQUEST_NOTIFICATION_SOUND)
        }

        btnSave.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)

            val alarm = Alarm(
                id = System.currentTimeMillis().toInt(),
                time = time,
                days = selectedDays.trimEnd(','),
                isActive = true
            )

            saveAlarmToPreferences(alarm)
            saveCheckBoxStatus()
            saveNotificationSound()

            val resultIntent = Intent()
            resultIntent.putExtra("newAlarm", alarm)
            resultIntent.putExtra("isShakeEnabled", isShakeEnabled)
            resultIntent.putExtra("notificationSoundUri", notificationSoundUri.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        btnClose.setOnClickListener { finish() }

        btnLabel.setOnClickListener {
            // Implement label settings logic here
        }

        btnDelete.setOnClickListener {
            // Implement delete alarm logic here
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NOTIFICATION_SOUND && resultCode == Activity.RESULT_OK) {
            notificationSoundUri = data?.data
        }
    }

    private fun getFullDayName(shortName: String): String {
        return when (shortName) {
            "M" -> "Minggu"
            "S" -> "Senin"
            "S" -> "Selasa"
            "R" -> "Rabu"
            "K" -> "Kamis"
            "J" -> "Jumat"
            "S" -> "Sabtu"
            else -> shortName
        }
    }

    private fun saveAlarmToPreferences(alarm: Alarm) {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val alarms = sharedPreferences.getStringSet("alarms_set", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
        alarms.add(alarm.toString())
        editor.putStringSet("alarms_set", alarms)
        editor.apply()
    }

    private fun saveCheckBoxStatus() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        checkBoxes.forEach { checkBox ->
            editor.putBoolean(checkBox.id.toString(), checkBox.isChecked)
        }
        editor.apply()
    }

    private fun loadCheckBoxStatus() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        checkBoxes.forEach { checkBox ->
            checkBox.isChecked = sharedPreferences.getBoolean(checkBox.id.toString(), false)
        }
    }

    private fun saveShakeStatus() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isShakeEnabled", isShakeEnabled)
        editor.apply()
    }

    private fun loadShakeStatus() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        isShakeEnabled = sharedPreferences.getBoolean("isShakeEnabled", false)
    }

    private fun saveNotificationSound() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notificationSoundUri", notificationSoundUri.toString())
        editor.apply()
    }

    private fun loadNotificationSound() {
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("notificationSoundUri", null)
        notificationSoundUri = uriString?.let { Uri.parse(it) }
    }

    private fun checkAllDaysSelected() {
        selectedDays = if (checkBoxes.all { it.isChecked }) {
            "Setiap Hari"
        } else {
            checkBoxes.filter { it.isChecked }
                .joinToString(", ") { getFullDayName(it.text.toString()) }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_SOUND = 1
    }
}