package pl.tolichwer.czoperkotlin.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "position")
data class Position(
    @PrimaryKey
    @NonNull
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
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
        id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
        startDate = "",
        endDate = "",
        lastLocationDate = 0,
        firstLocationDate = 0,
        startLocation = "",
        endLocation = "",
        status = 0,
        userID = userID)
}