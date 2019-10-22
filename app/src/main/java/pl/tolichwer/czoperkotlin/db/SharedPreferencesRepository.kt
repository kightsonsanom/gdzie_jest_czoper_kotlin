package pl.tolichwer.czoperkotlin.db

import android.content.Context
import android.content.SharedPreferences
import pl.tolichwer.czoperkotlin.R

const val USER_ID = "user_id"
const val GEO_ID = "geo_id"
const val POSITION_ID = "positionID"
const val ASSIGN_FAILURE_TIMESTAMP = "assign_failure_timestamp"
const val KEY_LOCATION_SERVICE_STATUS = "key_location_service_status"

class SharedPreferencesRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    fun isLocationServiceRunning(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOCATION_SERVICE_STATUS, false)
    }

    fun setLocationServiceStatus(isRunning: Boolean) {
        editor.putBoolean(KEY_LOCATION_SERVICE_STATUS, isRunning)
            .commit()
    }

    fun saveCurrentUserID(currentUserID: Int) {
        editor.putInt(USER_ID, currentUserID)
            .commit()
    }

    fun getCurrentUserID(): Int {
        return sharedPreferences.getInt(USER_ID, -1)
    }

    fun putGeoID(geoID: Long) {
        editor.putLong(GEO_ID, geoID)
            .commit()
    }

    fun putPositionID(positionID: Long) {
        editor.putLong(POSITION_ID, positionID)
            .commit()
    }

    fun setFailureTimestamp(date: Long) {
        editor.putLong(ASSIGN_FAILURE_TIMESTAMP, date)
            .commit()
    }
}