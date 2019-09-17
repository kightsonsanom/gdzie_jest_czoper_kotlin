package pl.tolichwer.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import pl.tolichwer.czoperkotlin.model.Geo

@Dao
interface GeoDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeo(geo: Geo): Single<Long>

    @Query("SELECT * FROM geo")
    fun getAllGeos(): Single<List<Geo>>

    @Query("SELECT * FROM geo g WHERE g.userID=:userID ORDER BY date DESC LIMIT 1")
    fun loadLatestGeo(userID: Int): Single<Geo>
}