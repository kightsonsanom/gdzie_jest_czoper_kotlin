package pl.tolichwer.czoperkotlin.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "position_geo_join", primaryKeys = ["positionID", "geoID"],
    foreignKeys = [ForeignKey(
        entity = Position::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("positionID")
    ), ForeignKey(
        entity = Geo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("geoID")
    )]
)
data class PositionGeoJoin(
    val positionID: Long,
    val geoID: Long,
    val assignTime: Long
)