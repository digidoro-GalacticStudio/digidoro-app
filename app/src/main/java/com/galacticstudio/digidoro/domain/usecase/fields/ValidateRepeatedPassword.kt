package com.galacticstudio.digidoro.domain.usecase.fields

import com.galacticstudio.digidoro.domain.usecase.ValidationResult

/**
 * Use case responsible for validating if a password matches the repeated password.
 */
class ValidateRepeatedPassword {
    /**
     * Executes the password validation and returns the result.
     *
     * @param password The password string to validate.
     * @param repeatedPassword The repeated password string to validate against.
     * @return The validation result containing the success status and an optional error message.
     */
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        // Check if the passwords match
        if(password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }

        // Password validation succeeded
        return ValidationResult(
            successful = true,
            errorMessage = null,
        )
    }
}