package com.example.czoperkotlin.services

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.czoperkotlin.db.Repository
import com.example.czoperkotlin.model.Geo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class LocationServiceHelper @Inject constructor(
    private val repository: Repository,
    private val context: Context
) {

    private lateinit var newGeo: Geo
    private var userID: Int
    private val geocoder = Geocoder(context)

    init {
        userID = repository.getUserID()
    }

    fun testHelper(): String {
        return repository.repoTest()
    }

    fun startProcessingLocation(newLocation: Location) {
        userID = repository.getUserID()
        newGeo = Geo(newLocation, userID)


       getLocationAddress(newLocation)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { Log.d("geocodeTag", "geocodeError")},
                onSuccess = { Log.d("geocodeTag", "geocode success $it")}
            )
    }

    fun getLocationAddress(location: Location): Single<String> {
        return Single.create { e ->
            try {
                val address =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = address[0].locality
                e.onSuccess(cityName)
            } catch (e1: IOException) {
                e.onError(e1)
            }
        }
    }
//
// fun startGeoCodingService(location: Location) {
//     val intent = Intent(context, GeocodeAddressIntentService::class.java)
//     intent.putExtra(Constants.RECEIVER, addressResultReceiver)
//     intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
//
//     GeocodeAddressIntentService.enqueueWork(context, intent)
// }

    fun isServiceRunningInForeground(): Boolean {
        return repository.isLocationSerivceRunning()
    }

// internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
//
//     override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
//         when (resultCode) {
//             Constants.SUCCESS_RESULT -> {
//                 locationAddress = resultData.getString(Constants.RESULT_DATA_KEY)
//                 getLastPositionFromDb()
//             }
//             Constants.FAILURE_RESULT -> {
//                 Timber.d("Repeat geocoding service")
//                 val location = resultData.getParcelable<Location>("location")
//                 startGeoCodingService(location)
//             }
//         }
//     }
// }
}