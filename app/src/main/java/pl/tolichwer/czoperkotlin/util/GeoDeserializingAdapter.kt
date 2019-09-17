package pl.tolichwer.czoperkotlin.util

import android.location.Location
import android.location.LocationManager
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import pl.tolichwer.czoperkotlin.model.Geo
import java.lang.reflect.Type

class GeoDeserializingAdapter: JsonDeserializer<Geo> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Geo {
        val jsonObject = json.asJsonObject
        var locationString = jsonObject.get("location")
            .asString
            .split(", ")
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()


        if (locationString.size < 2) {
            locationString = arrayOf("51.941067","15.504336")
        }


        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = java.lang.Double.parseDouble(locationString[0])
        location.longitude = java.lang.Double.parseDouble(locationString[1])


        return Geo(
            jsonObject.get("id").asLong,
            location,
            jsonObject.get("date").asLong,
            jsonObject.get("displayText").asString,
            jsonObject.getAsJsonObject("user").get("user_id").asInt
        )
    }
}