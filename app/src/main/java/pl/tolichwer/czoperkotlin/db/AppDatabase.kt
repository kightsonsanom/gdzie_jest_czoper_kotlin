package pl.tolichwer.czoperkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.tolichwer.czoperkotlin.db.converters.GeoConverter
import pl.tolichwer.czoperkotlin.db.dao.GeoDao
import pl.tolichwer.czoperkotlin.db.dao.PositionDao
import pl.tolichwer.czoperkotlin.db.dao.PositionGeoJoinDao
import pl.tolichwer.czoperkotlin.db.dao.UserDao
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.Position
import pl.tolichwer.czoperkotlin.model.PositionGeoJoin
import pl.tolichwer.czoperkotlin.model.User

@Database(entities = [User::class, Geo::class, Position::class, PositionGeoJoin::class], version = 1 )
@TypeConverters(GeoConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun geoDao(): GeoDao
    abstract fun positionDao(): PositionDao
    abstract fun positionGeoJoinDao(): PositionGeoJoinDao

}