package pl.tolichwer.czoperkotlin.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "position_geo_join", primaryKeys = ["positionId", "geoId"],
    foreignKeys = [ForeignKey(
        entity = Position::class,
        parentColumns = ["id"],
        childColumns = ["positionId"]
    ), ForeignKey(
        entity = Geo::class,
        parentColumns = ["id"],
        childColumns = ["geoId"]
    )]
)
data class PositionGeoJoin(
    val positionId: Long,
    val geoId: Long,
    val assignTime: Long
)