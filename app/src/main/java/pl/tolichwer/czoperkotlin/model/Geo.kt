package pl.tolichwer.czoperkotlin.model

import android.location.Location
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pl.tolichwer.czoperkotlin.util.Constants
import java.util.UUID

@Entity(tableName = "geo")
data class Geo(
    @PrimaryKey
    @NonNull
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    val location: Location,
    val date: Long,
    val displayText: String,
    val userID: Int
){

    @Ignore
    constructor(location: Location, userID: Int): this(
        id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
        location = location,
        date = System.currentTimeMillis(),
        displayText = "Date = ${Constants.longToString(System.currentTimeMillis())}",
        userID = userID)

}