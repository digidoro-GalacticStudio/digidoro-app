package com.galacticstudio.digidoro.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Date(orderType: OrderType): NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when(this) {
            is Date -> Date(orderType)
        }
    }
}
