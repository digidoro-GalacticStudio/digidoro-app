package com.galacticstudio.digidoro.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pending")
data class PendingRequestEntity(
    @PrimaryKey
    val _id: String = UUID.randomUUID().toString(),
    val operation: Operation,
    val data: String,
    val entityModel: String
)

enum class Operation {
    CREATE,
    UPDATE,
    DELETE,
    TOGGLE
}