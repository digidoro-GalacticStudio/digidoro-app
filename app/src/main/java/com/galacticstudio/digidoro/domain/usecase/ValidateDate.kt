package com.galacticstudio.digidoro.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Use case responsible for validating a date.
 */
class ValidateDate {
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")

    /**
     * Executes the date validation and returns the result.
     *
     * @param date The date string to validate in the format "MM-dd-yyyy".
     * @return The validation result containing the success status and an optional error message.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun execute(date: String): ValidationResult {
        try {
            val parsedDate = LocalDate.parse(date, dateFormatter)

            // Check if the year is before 1900
            if (parsedDate.year < 1900) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Please enter a valid date"
                )
            }

            // Check if the date is in the future
            if (parsedDate.isAfter(LocalDate.now())) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Please enter a date in the past"
                )
            }

            // Date validation succeeded
            return ValidationResult(successful = true)
        } catch (e: DateTimeParseException) {
            // Invalid date format
            return ValidationResult(
                successful = false,
                errorMessage = "Please enter a valid date in the format MM-dd-yyyy"
            )
        }
    }
}
