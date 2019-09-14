package com.example.czoperkotlin.model

import android.location.Location
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "geo")
data class Geo(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long = 0,
    val location: Location,
    val date: Long,
    val displayText: String,
    val userID: Int
){

    @Ignore
    constructor(location: Location, userID: Int): this(id = 0,location = location, date = 0,displayText = "",userID = userID)


}