package com.example.czoperkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.czoperkotlin.db.converters.GeoConverter
import com.example.czoperkotlin.db.dao.GeoDao
import com.example.czoperkotlin.db.dao.UserDao
import com.example.czoperkotlin.model.Geo
import com.example.czoperkotlin.model.User

@Database(entities = [User::class, Geo::class], version = 1 )
@TypeConverters(GeoConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun geoDao(): GeoDao

}