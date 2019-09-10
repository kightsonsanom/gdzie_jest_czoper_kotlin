package com.example.czoperkotlin.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "position")
data class Position(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long,
    val startDate: String,
    val endDate: String,
    val lastLocationDate: Long,
    val firstLocationDate: Long,
    val startLocation: String,
    val endLocation: String,
    val status: Int,
    val userID: Int
)