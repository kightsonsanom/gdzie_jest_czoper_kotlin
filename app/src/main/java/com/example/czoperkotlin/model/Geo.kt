package com.example.czoperkotlin.model

import android.location.Location
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geo")
data class Geo(
    @PrimaryKey
    @NonNull
    val id: Long,
    val location: Location,
    val date: Long,
    val displayText: String,
    val userID: Int
)