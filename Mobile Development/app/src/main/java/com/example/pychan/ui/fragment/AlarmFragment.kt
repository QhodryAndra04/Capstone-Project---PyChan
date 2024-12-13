package com.example.pychan.ui.fragment

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pychan.Alarm
import com.example.pychan.ui.receiver.AlarmReceiver
import com.example.pychan.R
import com.example.pychan.ui.activity.AddAlarmActivity
import com.example.pychan.ui.activity.EditAlarmActivity
import com.example.pychan.ui.adapter.AlarmAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class AlarmFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlarmAdapter
    private val alarms = mutableListOf<Alarm>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        val buttonAdd = view.findViewById<FloatingActionButton>(R.id.buttonAdd)

        adapter = AlarmAdapter(alarms, ::onEditAlarm, ::onToggleAlarm)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        buttonAdd.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(requireContext(), AddAlarmActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD_ALARM)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }

        loadAlarmsFromPreferences()

        return view
    }

    private fun loadAlarmsFromPreferences() {
        val sharedPreferences =
            requireContext().getSharedPreferences("alarms", Activity.MODE_PRIVATE)
        val alarmsSet =
            sharedPreferences.getStringSet("alarms_set", mutableSetOf()) ?: mutableSetOf()
        alarms.clear()
        alarmsSet.forEach { alarmString ->
            val alarm = Alarm.fromString(alarmString)
            alarms.add(alarm)
        }
        adapter.notifyDataSetChanged()
    }

    private fun onEditAlarm(alarm: Alarm) {
        val intent = Intent(requireContext(), EditAlarmActivity::class.java)
        intent.putExtra("alarm", alarm)
        startActivityForResult(intent, REQUEST_EDIT_ALARM)
    }

    private fun onToggleAlarm(alarm: Alarm, isActive: Boolean) {
        alarm.isActive = isActive
        if (isActive) {
            scheduleAlarm(alarm)
        } else {
            cancelAlarm(alarm)
        }
        saveAlarmsToPreferences()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_ADD_ALARM -> {
                    val newAlarm = data.getSerializableExtra("newAlarm") as? Alarm
                    if (newAlarm != null) {
                        alarms.add(newAlarm)
                        scheduleAlarm(newAlarm)
                    }
                }

                REQUEST_EDIT_ALARM -> {
                    val updatedAlarm = data.getSerializableExtra("updatedAlarm") as? Alarm
                    if (updatedAlarm != null) {
                        val index = alarms.indexOfFirst { it.id == updatedAlarm.id }
                        if (index != -1) alarms[index] = updatedAlarm
                        scheduleAlarm(updatedAlarm)
                    }
                    val deleteAlarm = data.getSerializableExtra("deleteAlarm") as? Alarm
                    if (deleteAlarm != null) {
                        alarms.removeAll { it.id == deleteAlarm.id }
                        cancelAlarm(deleteAlarm)
                    }
                }
            }
            saveAlarmsToPreferences()
            adapter.notifyDataSetChanged()
        }
    }

    private fun saveAlarmsToPreferences() {
        val sharedPreferences =
            requireContext().getSharedPreferences("alarms", Activity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val alarmsSet = alarms.map { it.toString() }.toMutableSet()
        editor.putStringSet("alarms_set", alarmsSet)
        editor.apply()
    }

    private fun scheduleAlarm(alarm: Alarm) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM),
                REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION
            )
            return
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.time.split(":")[0].toInt())
            set(Calendar.MINUTE, alarm.time.split(":")[1].toInt())
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun cancelAlarm(alarm: Alarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val REQUEST_ADD_ALARM = 1
        private const val REQUEST_EDIT_ALARM = 2
        private const val REQUEST_NOTIFICATION_PERMISSION = 3
        private const val REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION = 4
    }
}