package com.galacticstudio.digidoro.domain.usecase.fields

import com.galacticstudio.digidoro.domain.usecase.ValidationResult

/**
 * Use case responsible for validating a password.
 */
class ValidatePassword {
    /**
     * Executes the password validation and returns the result.
     *
     * @param password The password to validate.
     * @return The validation result containing the success status and an optional error message.
     */
    fun execute (password: String): ValidationResult {
        // Check if the password length is less than 8 characters
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }

        // Check if the password contains at least one digit and one letter
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }

        // Password validation succeeded
        return ValidationResult(
            successful = true,
            errorMessage = null,
        )
    }
}