// package com.example.asinit_user.gdziejestczoper.services
//
// import android.content.Context
// import android.content.Intent
// import android.location.Address
// import android.location.Geocoder
// import android.location.Location
// import android.os.Bundle
// import android.os.ResultReceiver
// import androidx.annotation.NonNull
// import androidx.core.app.JobIntentService
//
// import com.example.czoperkotlin.db.Repository
// import com.example.czoperkotlin.util.Constants
// import com.google.gson.JsonObject
//
//
// import java.io.IOException
// import java.util.ArrayList
// import java.util.Locale
//
// import javax.inject.Inject
//
// import dagger.android.AndroidInjection
//
// class GeocodeAddressIntentService : JobIntentService(), GeocodeAddressCallback {
//
//     @Inject
//     lateinit var repository: Repository
//
//     @Inject
//     lateinit var geocoder: Geocoder
//
//     private val JOB_ID = 2000
//
//     protected var resultReceiver: ResultReceiver
//
//     override fun onCreate() {
//         AndroidInjection.inject(this)
//         super.onCreate()
//         repository.setGeocodeAddressCallback(this)
//     }
//
//
//
//     override fun onHandleWork(@NonNull intent: Intent) {
//
//         val addresses: List<Address>?
//         var addressString: String?
//
//         val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)
//         resultReceiver = intent.getParcelableExtra(Constants.RECEIVER)
//
//         val lat = location.latitude
//         val lng = location.longitude
//
//
//         try {
//             addresses = geocoder!!.getFromLocation(lat, lng, 1)
//             if (addresses != null && addresses.size > 0) {
//                 addressString = getAddressString(addresses)
//                 if (addressString == null) {
//                     addressString = getFirstPartOfAddress(addresses)
//                 }
//                 deliverResultToReceiver(Constants.SUCCESS_RESULT, addressString)
//             } else {
//                 callGeocodingApi(lat, lng)
//             }
//         } catch (e: IOException) {
//             addressString = Constants.GEOCODING_FAILURE
//             deliverResultToReceiver(Constants.SUCCESS_RESULT, addressString)
//         }
//     }
//
//     private fun getFirstPartOfAddress(addresses: List<Address>): String {
//         val address = addresses[0]
//         val splitAddress = address.getAddressLine(0)
//             .split(",".toRegex())
//             .dropLastWhile { it.isEmpty() }
//             .toTypedArray()
//         return splitAddress[0]
//     }
//
//     private fun callGeocodingApi(lat: Double, lng: Double) {
//         val address =
//             String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/geocode/json?latlng=%1\$f,%2\$f&location_type=ROOFTOP&result_type=point_of_interest&key=AIzaSyADPN7X3cxWbdMfpi5aHoikbaOv9N1L1LY", lat, lng)
//         repository.getReverseGeocoding(address)
//     }
//
//     private fun getAddressString(addresses: List<Address>): String {
//         val address = addresses[0]
//         val addressString: String
//         if (address.subThoroughfare != null) {
//             addressString = address.thoroughfare + ", " + address.subThoroughfare
//         } else {
//             addressString = address.thoroughfare
//         }
//         return addressString
//     }
//
//     private fun deliverResultToReceiver(resultCode: Int, message: String?) {
//         val bundle = Bundle()
//         bundle.putString(Constants.RESULT_DATA_KEY, message)
//         resultReceiver.send(resultCode, bundle)
//     }
//
//     fun onSuccessGetAddress(address: JsonObject) {
//         val retList = ArrayList<Address>()
//
//         val jsonPrimitive = address.getAsJsonPrimitive("status")
//
//
//         if ("OK".equals(jsonPrimitive.asString, ignoreCase = true)) {
//             val results = address.getAsJsonArray("results")
//             if (results.size() > 0) {
//                 var i = 0
//                 while (i < results.size() && i < 1) {
//                     val result = results.get(i)
//                         .asJsonObject
//                     val addr = Address(Locale.getDefault())
//
//                     val components = result.getAsJsonArray("address_components")
//                     for (a in 0 until components.size()) {
//                         val component = components.get(a)
//                             .asJsonObject
//                         val types = component.getAsJsonArray("types")
//                         for (j in 0 until types.size()) {
//                             val type = types.get(j)
//                                 .asString
//                             when (type) {
//                                 "locality" -> addr.locality = component.get("long_name")
//                                     .asString
//                                 "street_number" -> addr.subThoroughfare = component.get("long_name")
//                                     .asString
//                                 "route" -> addr.thoroughfare = component.get("long_name")
//                                     .asString
//                             }
//                         }
//                     }
//
//                     addr.latitude = result.getAsJsonObject("geometry")
//                         .getAsJsonObject("location")
//                         .get("lat")
//                         .asDouble
//                     addr.longitude = result.getAsJsonObject("geometry")
//                         .getAsJsonObject("location")
//                         .get("lng")
//                         .asDouble
//                     retList.add(addr)
//                     i++
//                 }
//             }
//         }
//
//         if (retList.size > 0) {
//             val addressString = retList[0].thoroughfare + ", " + retList[0].subThoroughfare
//             deliverResultToReceiver(Constants.SUCCESS_RESULT, addressString)
//         }
//     }
//
//     fun onFailureGetAddress() {
//         deliverResultToReceiver(Constants.SUCCESS_RESULT, Constants.GEOCODING_FAILURE)
//     }
//
//     override fun onDestroy() {
//         super.onDestroy()
//     }
// }