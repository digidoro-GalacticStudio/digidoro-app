package com.galacticstudio.digidoro.domain.usecase

import android.util.Patterns

/**
 * Use case responsible for validating an email address.
 */
class ValidateEmail {
    /**
     * Executes the email validation and returns the result.
     *
     * @param email The email address to validate.
     * @return The validation result containing the success status and an optional error message.
     */
    fun execute (email: String): ValidationResult {
        // Check if the email is blank
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }

        // Check if the email matches the email address pattern
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }

        // Email validation succeeded
        return ValidationResult(
            successful = true,
            errorMessage = null,
        )
    }
}