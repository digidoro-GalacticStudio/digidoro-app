package com.galacticstudio.digidoro.ui.screens.todo.item

import java.util.Calendar
import java.util.Date

data class ItemTodoState(
    var id: String = "",
    var title: String = "",
    var description: String = "some default description",
    var theme: String = "FFFFFF",
    var createdAt: Date = Calendar.getInstance().time,
    var state: Boolean = false,
    var reminder: Date = Calendar.getInstance().time
)
