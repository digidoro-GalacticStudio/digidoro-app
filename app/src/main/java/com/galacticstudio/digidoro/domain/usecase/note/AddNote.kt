package com.galacticstudio.digidoro.domain.usecase.note

import com.galacticstudio.digidoro.domain.usecase.ValidationResult
import com.galacticstudio.digidoro.navigation.Screen
import com.galacticstudio.digidoro.repository.NoteRepository

class AddNote() {
    operator fun invoke(
        title: String,
        message: String,
        tags: List<String>,
        theme: String
    ): ValidationResult {
        if (title.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "Title cannot be empty")
        }

        if (message.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "Message cannot be empty")
        }

        if (theme.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "Theme cannot be empty")
        }

        return ValidationResult(successful = true,errorMessage = null,)
    }
}