package com.galacticstudio.digidoro.domain.usecase.todo

import android.util.Log
import com.galacticstudio.digidoro.domain.usecase.ValidationResult
import java.util.Calendar
import java.util.Date
import kotlin.math.log

class AddTodo() {
    operator fun invoke(
        title: String,
        description: String,
        theme: String,
        reminder: Date
    ): ValidationResult {
        if(title.isBlank())
            return ValidationResult(successful = false, errorMessage = "Title cannot be empty")
        if(description.isBlank())
            return ValidationResult(successful = false, errorMessage = "Description cannot be empty")
        if (theme.isBlank())
            return ValidationResult(successful = false, errorMessage = "Theme cannot be empty")

        return ValidationResult(successful = true, errorMessage = null)
    }
}