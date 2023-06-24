package com.galacticstudio.digidoro.domain.usecase.todo

import com.galacticstudio.digidoro.domain.usecase.ValidationResult
import java.util.Calendar

class AddTodo() {
    operator fun invoke(
        title: String,
        description: String,
        theme: String,
        reminder: Calendar
    ): ValidationResult {
        if(title.isBlank())
            return ValidationResult(successful = false, errorMessage = "Title cannot be empty")
        if(description.isBlank())
            return ValidationResult(successful = false, errorMessage = "Description cannot be empty")
        if (theme.isBlank())
            return ValidationResult(successful = false, errorMessage = "Theme cannot be empty")
        if (reminder == null)
            return ValidationResult(successful = false, errorMessage = "Reminder cannot be empty or null")

        return ValidationResult(successful = true, errorMessage = null)
    }
}