package com.galacticstudio.digidoro.domain.usecase.fields

import com.galacticstudio.digidoro.domain.usecase.ValidationResult

/**
 * Use case responsible for validating a text field.
 */
class ValidateTextField {
    /**
     * Executes the text field validation and returns the result.
     *
     * @param text The text to validate.
     * @param maxLength The maximum allowed length for the text.
     * @return The validation result containing the success status and an optional error message.
     */
    fun execute(text: String, maxLength: Int): ValidationResult {
        // Check if the text is empty
        if (text.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "This field cannot be empty"
            )
        }

        // Check if the text exceeds the maximum length
        if (text.length > maxLength) {
            return ValidationResult(
                successful = false,
                errorMessage = "This field cannot exceed $maxLength characters"
            )
        }

        // Text field validation succeeded
        return ValidationResult(successful = true)
    }
}
