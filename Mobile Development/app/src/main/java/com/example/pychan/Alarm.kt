package com.example.pychan

import java.io.Serializable

data class Alarm(
    val id: Int,
    var time: String,
    var days: String,
    var isActive: Boolean
) : Serializable {
    override fun toString(): String {
        return "$id|$time|$days|$isActive"
    }

    companion object {
        fun fromString(alarmString: String): Alarm {
            val parts = alarmString.split("|")
            return Alarm(
                id = parts[0].toInt(),
                time = parts[1],
                days = parts[2],
                isActive = parts[3].toBoolean()
            )
        }
    }
}