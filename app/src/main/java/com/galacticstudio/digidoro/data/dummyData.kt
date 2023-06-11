package com.galacticstudio.digidoro.data

import androidx.compose.ui.graphics.Color
import java.util.Calendar

val pomodoroList = mutableListOf<PomodoroModel>().apply {
    add(PomodoroModel(
        name = "Estudiar para Análisis de Sistemas",
        theme = "#8CC6E7",
    ))
    add(PomodoroModel(
        name = "Estudiar para Análisis de Sistemas",
        theme = "#C7B9FF",
    ))
    add(PomodoroModel(
        name = "Estudiar para Análisis de Sistemas",
        theme = "#FFC700",
    ))
}


val todoList = mutableListOf<TodoModel>().apply {
    val calendar = Calendar.getInstance()

    add(TodoModel(
        title = "Right Now",
        theme = "#E15A51",
        createdAt = calendar.time
    ))

    calendar.add(Calendar.HOUR_OF_DAY, -2)
    add(TodoModel(
        title = "2 hours ago",
        theme = "#E15A51",
        createdAt = calendar.time
    ))


    calendar.add(Calendar.DAY_OF_YEAR, 1)
    add(TodoModel(
        title = "Tomar apuntes de clases",
        theme = "#FFC700",
        createdAt = calendar.time
    ))

    calendar.add(Calendar.DAY_OF_YEAR, -2)
    add(TodoModel(
        title = "Revisar ensayo",
        theme = "#C7B9FF",
        createdAt = calendar.time
    ))

    calendar.add(Calendar.DAY_OF_YEAR, -3)
    add(TodoModel(
        title = "Completar guía de ejercicios",
        theme = "#85E0A3",
        createdAt = calendar.time
    ))

    calendar.add(Calendar.DAY_OF_YEAR, -5)
    add(TodoModel(
        title = "Completar guía de ejercicios",
        theme = "#85E0A3",
        createdAt = calendar.time
    ))

    calendar.add(Calendar.DAY_OF_YEAR, 23)
    add(TodoModel(
        title = "Completar guía de ejercicios",
        theme = "#85E0A3",
        createdAt = calendar.time
    ))
}

val itemNotesList = listOf(
    Note(
        user_id = 1,
        title = "Abstract Libro 1",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = Color.White.toString(),
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    Note(
        user_id = 2,
        title = "Abstract Libro 2",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = Color(0xFFD1EBFF).toString(),
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    Note(
        user_id = 3,
        title = "Abstract Libro 3",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = Color(0xFFFFD2D2).toString(),
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    Note(
        user_id = 4,
        title = "Abstract Libro 4",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = Color.White.toString(),
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    Note(
        user_id = 5,
        title = "Abstract Libro 5",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = Color(0xFFE3D4FF).toString(),
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    )
)

val tagList = mutableListOf<TagModel>().apply {
    add(
        TagModel(name = "Comics")
    )
    add(
        TagModel(name = "Food")
    )
    add(
        TagModel(name = "Music")
    )
    add(
        TagModel(name = "Movies")
    )
    add(
        TagModel(name = "Free breakfast")
    )
    add(
        TagModel(name = "Last Tags")
    )
}
