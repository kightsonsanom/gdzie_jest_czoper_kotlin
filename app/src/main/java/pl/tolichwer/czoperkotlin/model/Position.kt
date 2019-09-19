package pl.tolichwer.czoperkotlin.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "position")
data class Position(
    @PrimaryKey
    @NonNull
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    var startDate: String = "",
    var endDate: String = "",
    var lastLocationDate: Long = 0,
    var firstLocationDate: Long = 0,
    var startLocation: String = "",
    var endLocation: String = "",
    var status: Int = 0,
    val userID: Int = 0
)
