package com.galacticstudio.digidoro.domain.usecase

/**
 * Data class representing the result of a validation operation.
 *
 * @property successful Indicates whether the validation was successful.
 * @property errorMessage An optional error message indicating the reason for validation failure.
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
