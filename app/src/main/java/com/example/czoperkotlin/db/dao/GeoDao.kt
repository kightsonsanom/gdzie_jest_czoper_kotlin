package com.example.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.czoperkotlin.model.Geo
import io.reactivex.Single

@Dao
interface GeoDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeo(geo: Geo): Single<Long>

    @Query("SELECT * FROM geo")
    fun getAllGeos(): Single<List<Geo>>
}