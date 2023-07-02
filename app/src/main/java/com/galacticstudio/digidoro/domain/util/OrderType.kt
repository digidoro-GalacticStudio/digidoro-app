package com.galacticstudio.digidoro.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
