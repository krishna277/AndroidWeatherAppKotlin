package com.krishna.weatherapp

import java.text.SimpleDateFormat
import java.util.TimeZone

object DateHelper {

    fun getLocalTimeTxt(src: String): String {

        var localDate = "00/00/0000 00:00 AM"

        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(src)

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Singapore")
            localDate = dateFormatter.format(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return localDate
    }
}
