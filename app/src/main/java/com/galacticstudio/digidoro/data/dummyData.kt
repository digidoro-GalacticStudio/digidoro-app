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
    NoteModel(
        user_id = "1",
        title = "Abstract Libro 1",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = "#FFFFFF",
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    NoteModel(
        user_id = "2",
        title = "Abstract Libro 2",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = "#D1EBFF",
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    NoteModel(
        user_id = "3",
        title = "Abstract Libro 3",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = "#FFD2D2",
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    NoteModel(
        user_id = "4",
        title = "Abstract Libro 4",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = "#FFFFFF",
        is_trashed = false,
        createdAt = Calendar.getInstance().time,
        updatedAt = Calendar.getInstance().time
    ),
    NoteModel(
        user_id = "5",
        title = "Abstract Libro 5",
        message = "Simplicity boils down to two steps: Identify the essential. Eliminate the rest.",
        tags = emptyList(),
        theme = "#FFFFFF",
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

val userList: MutableList<UserModel> = mutableListOf(
    UserModel(
        name = "John",
        lastname = "Doe",
        email = "johndoe@example.com",
        phone_number = "+123456789",
        password_hash = "password123",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/johndoe.jpg",
        level = "Dreamer",
        daily_score = 10,
        weekly_score = 75,
        monthly_score = 250,
        total_score = 1200
    ),
    UserModel(
        name = "Alice",
        lastname = "Smith",
        email = "alice.smith@example.com",
        phone_number = "+987654321",
        password_hash = "qwerty",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/alicesmith.jpg",
        level = "Explorer",
        daily_score = 5,
        weekly_score = 40,
        monthly_score = 150,
        total_score = 800
    ),
    UserModel(
        name = "Michael",
        lastname = "Johnson",
        email = "michael.johnson@example.com",
        phone_number = "+555555555",
        password_hash = "abcdef",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/michaeljohnson.jpg",
        level = "Adventurer",
        daily_score = 2,
        weekly_score = 20,
        monthly_score = 80,
        total_score = 400
    ),
    UserModel(
        name = "Emily",
        lastname = "Brown",
        email = "emily.brown@example.com",
        phone_number = "+111222333",
        password_hash = "password456",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/emilybrown.jpg",
        level = "Dreamer",
        daily_score = 8,
        weekly_score = 60,
        monthly_score = 200,
        total_score = 1000
    ),
    UserModel(
        name = "William",
        lastname = "Davis",
        email = "william.davis@example.com",
        phone_number = "+444555666",
        password_hash = "qwerty123",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/williamdavis.jpg",
        level = "Explorer",
        daily_score = 4,
        weekly_score = 30,
        monthly_score = 120,
        total_score = 600
    ),
    UserModel(
        name = "Sophia",
        lastname = "Wilson",
        email = "sophia.wilson@example.com",
        phone_number = "+777888999",
        password_hash = "abcdef123",
        date_birth = Calendar.getInstance().time,
        profile_pic = "https://example.com/profiles/sophiawilson.jpg",
        level = "Adventurer",
        daily_score = 1,
        weekly_score = 10,
        monthly_score = 40,
        total_score = 200
    )
)