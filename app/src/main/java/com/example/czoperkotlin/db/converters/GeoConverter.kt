package com.example.czoperkotlin.db.converters

import android.location.Location
import android.location.LocationManager
import androidx.room.TypeConverter

class GeoConverter {

    @TypeConverter
    fun locationToString(location: Location): String {
        return "${location.longitude}, ${location.latitude}"
    }

    @TypeConverter
    fun stringToLocation(locationString: String): Location {
        val location = Location(LocationManager.GPS_PROVIDER)
        val doubleValues = locationString.split(", ")
            .map {
                it.trim()
                    .toDouble()
            }

        location.longitude = doubleValues[0]
        location.latitude = doubleValues[1]

        return location
    }
}
