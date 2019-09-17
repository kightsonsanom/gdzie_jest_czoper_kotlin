package pl.tolichwer.czoperkotlin.util

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import pl.tolichwer.czoperkotlin.model.Geo
import java.lang.reflect.Type

class GeoSerializingAdapter : JsonSerializer<Geo> {

    override fun serialize(src: Geo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        val locationString = "${src.location.latitude}, ${src.location.longitude}"
        obj.addProperty("id", src.id)
        obj.addProperty("date", src.date)
        obj.addProperty("displayText", src.displayText)
        obj.addProperty("location", locationString)

        return obj
    }
}