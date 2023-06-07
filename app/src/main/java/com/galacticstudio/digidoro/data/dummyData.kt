package com.galacticstudio.digidoro.data

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

//    calendar.add(Calendar.DAY_OF_YEAR, 14)
    calendar.add(Calendar.DAY_OF_YEAR, 23)
    add(TodoModel(
        title = "Completar guía de ejercicios",
        theme = "#85E0A3",
        createdAt = calendar.time
    ))
}