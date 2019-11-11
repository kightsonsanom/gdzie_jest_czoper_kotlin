package pl.tolichwer.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.PositionGeoJoin

@Dao
interface PositionGeoJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(positionGeoJoin: PositionGeoJoin): Single<Long>

    // @Query(
    //     "SELECT * FROM geo " +
    //         "INNER JOIN position_geo_join ON geo.id = position_geo_join.geoId " +
    //         "WHERE position_geo_join.positionId=:positionId"
    // )
    // fun getGeoForPosition(positionId: Long): LiveData<List<Geo>>

    @Query(
        ("SELECT * FROM (SELECT * FROM geo " +
            "INNER JOIN positiongeojoin ON geo.id = positiongeojoin.geoId " +
            "WHERE positiongeojoin.positionid=:positionid)" +
            "ORDER BY date LIMIT 1")
    )
    fun getOldestGeoForPosition(positionid: Long): Single<Geo>

    @Query("SELECT * FROM positiongeojoin WHERE positiongeojoin.assignTime >= :positionIdFromPreferences")
    fun getAssignsSinceFailure(positionIdFromPreferences: Long): List<PositionGeoJoin>

    @Query("SELECT * FROM positiongeojoin")
    fun getAllAssigns(): Single<List<PositionGeoJoin>>
}