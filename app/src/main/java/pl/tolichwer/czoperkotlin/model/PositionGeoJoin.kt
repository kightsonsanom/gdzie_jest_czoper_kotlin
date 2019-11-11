package pl.tolichwer.czoperkotlin.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "positiongeojoin", primaryKeys = ["positionid", "geoid"],
    foreignKeys = [ForeignKey(
        entity = Position::class,
        parentColumns = ["id"],
        childColumns = ["positionid"]
    ), ForeignKey(
        entity = Geo::class,
        parentColumns = ["id"],
        childColumns = ["geoid"]
    )]
)
data class PositionGeoJoin(
    val positionid: Long,
    val geoid: Long,
    val assignTime: Long
)