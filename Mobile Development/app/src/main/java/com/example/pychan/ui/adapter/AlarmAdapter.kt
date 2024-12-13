package com.example.pychan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pychan.Alarm
import com.example.pychan.R

class AlarmAdapter(
    private val alarms: List<Alarm>,
    private val onEdit: (Alarm) -> Unit,
    private val onToggle: (Alarm, Boolean) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvDays: TextView = view.findViewById(R.id.tvDays)
        val switchAlarm: SwitchCompat = view.findViewById(R.id.switchAlarm1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.tvTime.text = alarm.time
        holder.tvDays.text = getFullDayNames(alarm.days)
        holder.switchAlarm.isChecked = alarm.isActive

        holder.itemView.setOnClickListener { onEdit(alarm) }
        holder.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            onToggle(alarm, isChecked)
        }
    }

    private fun getFullDayNames(days: String): String {
        if (days == "Setiap Hari") {
            return days
        }
        val dayList = days.split(", ").map { day ->
            when (day) {
                "M" -> "Minggu"
                "S" -> "Senin"
                "S" -> "Selasa"
                "R" -> "Rabu"
                "K" -> "Kamis"
                "J" -> "Jumat"
                "S" -> "Sabtu"
                else -> day
            }
        }
        return dayList.joinToString(", ")
    }

    override fun getItemCount() = alarms.size
}