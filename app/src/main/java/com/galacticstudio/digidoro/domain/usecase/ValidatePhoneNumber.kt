package com.galacticstudio.digidoro.domain.usecase

/**
 * Use case responsible for validating a phone number.
 */
class ValidatePhoneNumber {
    /**
     * Executes the phone number validation and returns the result.
     *
     * @param phoneNumber The phone number string to validate.
     * @return The validation result containing the success status and an optional error message.
     */
    fun execute(phoneNumber: String): ValidationResult {
        // Remove any non-digit characters from the phone number
        val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")

        // Check if the phone number is empty
        if (digitsOnly.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number can't be empty"
            )
        }

        // Check if the phone number is of the desired length (e.g., 10 digits)
        val desiredLength = 8
        if (digitsOnly.length < desiredLength) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number needs to consist of at least $desiredLength digits long"
            )
        }

        // Phone number validation succeeded
        return ValidationResult(successful = true)
    }
}
