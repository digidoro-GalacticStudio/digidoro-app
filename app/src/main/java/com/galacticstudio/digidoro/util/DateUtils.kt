package com.galacticstudio.digidoro.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {
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
    }
}