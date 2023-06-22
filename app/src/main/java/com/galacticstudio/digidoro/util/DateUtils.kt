package com.galacticstudio.digidoro.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {

        fun formatLongDateToString(dateMillis: Long): String {
            val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance().apply {
                timeInMillis = dateMillis
                add(Calendar.DAY_OF_MONTH, 1)
            }
            return dateFormat.format(calendar.time)
        }

        @SuppressLint("NewApi")
        fun convertToISO8601(dateString: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            val localDate = LocalDate.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return localDate.format(outputFormatter)
        }

        fun convertISO8601ToDate(dateString: String): Date {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            return dateFormat.parse(dateString)
        }

        fun formatDateWithTime(date: Date?): Pair<String, String> {
            if (date == null) {
                return "Invalid Date" to ""
            }

            val currentDate = Calendar.getInstance()
            val formattedDate = SimpleDateFormat("EEEE d", Locale.getDefault()).format(date)
            val formattedDateTime = SimpleDateFormat("hh:mma", Locale.getDefault()).format(date)

            val timeDifference = currentDate.timeInMillis - date.time
            val daysAgo = timeDifference / (24 * 60 * 60 * 1000)
            val hoursAgo = timeDifference / (60 * 60 * 1000)

            return when {
                hoursAgo < 24L && daysAgo == 0L -> {
                    when {
                        hoursAgo < 0 -> "Tomorrow, " to formattedDate
                        hoursAgo < 1 -> "< 1 hour ago, " to formattedDateTime
                        else -> "Today, " to formattedDateTime
                    }
                }
                daysAgo == 0L && hoursAgo > 24L -> "Yesterday, " to "$formattedDate $formattedDateTime"
                daysAgo == -1L -> "Tomorrow, " to formattedDate
                daysAgo == 1L -> "Yesterday, " to formattedDate
                daysAgo == 2L -> "Day before yesterday, " to formattedDate
                daysAgo > 0L -> "$daysAgo days ago, " to formattedDate
                else -> "In the future!, " to formattedDate
            }
        }

        fun formatDateToString(date: Date?): String {
            if (date == null) {
                return "Invalid Date"
            }
            return SimpleDateFormat("EEEE d", Locale.getDefault()).format(date)
        }
    }
}