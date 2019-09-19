package pl.tolichwer.czoperkotlin.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "position_geo_join", primaryKeys = ["positionId", "geoId"],
    foreignKeys = [ForeignKey(
        entity = Position::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("positionId")
    ), ForeignKey(
        entity = Geo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("geoId")
    )]
)
data class PositionGeoJoin(
    val positionId: Long,
    val geoId: Long,
    val assignTime: Long
)