package com.galacticstudio.digidoro.network.dto

/**
 * Data classes representing a generic error response from an API.
 * ErrorResponse contains the main error response information,
 * ErrorData provides additional details about the error,
 * and ValidationError represents a validation error with a specific field and error message.
 */

data class ErrorResponse(
    val status: String?,
    val code: Int?,
    val message: Any?,
    val error: ErrorData?
)

data class ErrorData(
    val index: Int?,
    val code: Int?,
    val keyPattern: Map<String, Any>?,
    val keyValue: Map<String, Any>?,
    val errors: List<ValidationError>?
)

data class ValidationError(
    val field: String?,
    val message: String?
)