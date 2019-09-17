package pl.tolichwer.czoperkotlin.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "position")
data class Position(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long,
    var startDate: String,
    var endDate: String,
    var lastLocationDate: Long,
    var firstLocationDate: Long,
    var startLocation: String,
    var endLocation: String,
    var status: Int,
    val userID: Int
) {

    @Ignore
    constructor(userID: Int) : this(
        id = 0,
        startDate = "",
        endDate = "",
        lastLocationDate = 0,
        firstLocationDate = 0,
        startLocation = "",
        endLocation = "",
        status = 0,
        userID = userID)
}