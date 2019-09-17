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
    //         "INNER JOIN position_geo_join ON geo.id = position_geo_join.geoID " +
    //         "WHERE position_geo_join.positionID=:positionID"
    // )
    // fun getGeoForPosition(positionID: Long): LiveData<List<Geo>>

    @Query(
        ("SELECT * FROM (SELECT * FROM geo " +
            "INNER JOIN position_geo_join ON geo.id = position_geo_join.geoID " +
            "WHERE position_geo_join.positionID=:positionID)" +
            "ORDER BY date LIMIT 1")
    )
    fun getOldestGeoForPosition(positionID: Long): Single<Geo>

    @Query("SELECT * FROM position_geo_join WHERE position_geo_join.assignTime >= :positionIDFromPreferences")
    fun getAssignsSinceFailure(positionIDFromPreferences: Long): List<PositionGeoJoin>
}