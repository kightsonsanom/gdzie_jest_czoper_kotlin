package pl.tolichwer.czoperkotlin.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.Position
import pl.tolichwer.czoperkotlin.model.PositionGeoJoin
import pl.tolichwer.czoperkotlin.util.Constants
import java.io.IOException
import javax.inject.Inject

const val NO_GEO_BREAK_15MIN: Int = 1500000
const val NEW_POSITION_OFFSET: Long = 1
const val ACCEPTABLE_DISTANCE_BETWEEN_GEO: Float = 250f

class LocationServiceHelper @Inject constructor(
    private val repository: Repository,
    private val context: Context
) {

    private lateinit var newGeo: Geo
    private lateinit var latestGeoFromDB: Geo
    private lateinit var latestPositionFromDB: Position

    private var userID: Int = repository.getUserID()
    private val geocoder = Geocoder(context)
    private var address = ""

    @SuppressLint("CheckResult")
    fun startProcessingLocation(newLocation: Location) {
        newGeo = Geo(newLocation, userID)

        getLocationAddress(newLocation)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    address = context.getString(R.string.internet_connection_required)
                    getLastPositionFromDb()
                },
                onSuccess = {
                    address = it
                    getLastPositionFromDb()
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun getLastPositionFromDb() {
        repository.getLatestPositionFromDB(userID)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Log.d("getLastPositionFromDb", "$it")
                    getLatestGeoFromDB()
                },
                onSuccess = {
                    Log.d("getLastPositionFromDb", "$it")
                    latestPositionFromDB = it
                    if (it.status == Constants.PositionStatus.STOP.ordinal) {
                        getOldestGeoForPositionFromDB()
                    } else {
                        getLatestGeoFromDB()
                    }
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun getLatestGeoFromDB() {
        repository.getLatesGeoFromDB(userID)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    processNewGeo()
                },
                onSuccess = {
                    latestGeoFromDB = it
                    processNewGeo()

                }
            )
    }

    private fun getOldestGeoForPositionFromDB() {
        repository.getOldestGeoForPosition(latestPositionFromDB.id)
    }

    private fun processNewGeo() {
        var newPosition: Position



        if (!(::latestPositionFromDB.isInitialized) || !(::latestGeoFromDB.isInitialized)) {
            newPosition = Position(userID)
            newPosition.startLocation = address
            newPosition.startDate = Constants.longToString(newGeo.date)
            newPosition.firstLocationDate = newGeo.date
            newPosition.status = Constants.PositionStatus.UNKNOWN.ordinal
            newPosition.lastLocationDate = newGeo.date


        } else if (isLatestGeoFromDbTooOld()) {

            newPosition = Position(userID)
            newPosition.startDate = Constants.longToString(latestPositionFromDB.lastLocationDate)
            newPosition.endDate = Constants.longToString(newGeo.date)
            newPosition.firstLocationDate = latestPositionFromDB.lastLocationDate
            newPosition.lastLocationDate = newGeo.date
            newPosition.status = Constants.PositionStatus.PAUSE.ordinal

            sendGeoAndPosition(newGeo,newPosition)
            assignGeoToPosition(PositionGeoJoin(newPosition.id, newGeo.id, newGeo.date))


            newPosition = Position(userID)
            newPosition.startLocation = address
            newPosition.startDate = Constants.longToString(newGeo.date + NEW_POSITION_OFFSET)
            newPosition.firstLocationDate = newGeo.date + NEW_POSITION_OFFSET
            newPosition.lastLocationDate = newGeo.date + NEW_POSITION_OFFSET
            newPosition.status = Constants.PositionStatus.UNKNOWN.ordinal

            //post position
        } else if (latestPositionFromDB.status == Constants.PositionStatus.UNKNOWN.ordinal) {

            newPosition = latestPositionFromDB

            if (isLastGeoFarAway()) {
                newPosition.status = Constants.PositionStatus.MOVE.ordinal
                newPosition.endLocation = address
                newPosition.endDate = Constants.longToString(newGeo.date)
            } else {
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.status = Constants.PositionStatus.STOP.ordinal
            }
            newPosition.lastLocationDate = newGeo.date
            //post position
        } else if (latestPositionFromDB.status == Constants.PositionStatus.STOP.ordinal) {

            if (isLastGeoFarAway()) {
                newPosition = Position(userID)
                newPosition.status = Constants.PositionStatus.MOVE.ordinal
                newPosition.startLocation = latestPositionFromDB.startLocation
                newPosition.endLocation = address
                newPosition.firstLocationDate = newGeo.date
                newPosition.lastLocationDate = newGeo.date + NEW_POSITION_OFFSET
                newPosition.startDate = latestPositionFromDB.endDate
                newPosition.endDate = Constants.longToString(newGeo.date)

                //post position
            } else {
                newPosition = latestPositionFromDB
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.lastLocationDate = newGeo.date

                //post position
            }
        } else if (latestPositionFromDB.status == Constants.PositionStatus.MOVE.ordinal) {

            if (isLastGeoFarAway()) {
                newPosition = latestPositionFromDB

                newPosition.lastLocationDate = newGeo.date
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.endLocation = address

                // post position
            } else {

                newPosition = Position(userID)
                newPosition.status = Constants.PositionStatus.MOVE.ordinal
                newPosition.startLocation = address
                newPosition.firstLocationDate = newGeo.date
                newPosition.startDate = latestPositionFromDB.endDate
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.lastLocationDate = newGeo.date + NEW_POSITION_OFFSET

                // post position
            }
        } else {
            throw RuntimeException("Wrong position status")
        }

        sendGeoAndPosition(newGeo,newPosition)
        assignGeoToPosition(PositionGeoJoin(newPosition.id, newGeo.id, newGeo.date))

    }

    private fun sendGeoAndPosition(newGeo: Geo, newPosition: Position) {
        repository.sendGeoAndPosition(newGeo, newPosition)
    }

    private fun assignGeoToPosition(positionGeoJoin: PositionGeoJoin) {
        repository.assignGeoToPosition(positionGeoJoin)
    }

    private fun isLastGeoFarAway(): Boolean {
        return newGeo.location.distanceTo(latestGeoFromDB.location) > ACCEPTABLE_DISTANCE_BETWEEN_GEO
    }

    private fun isLatestGeoFromDbTooOld(): Boolean {
        return newGeo.date - latestPositionFromDB.lastLocationDate > NO_GEO_BREAK_15MIN
    }

    private fun getLocationAddress(location: Location): Single<String> {
        return Single.create { e ->
            try {
                val address =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val addressString = getAddressString(address[0])
                e.onSuccess(addressString)
            } catch (e1: IOException) {
                e.onError(e1)
            }
        }
    }

    private fun getAddressString(locality: Address): String {
        return if (locality.subThoroughfare != null) {
            locality.thoroughfare + ", " + locality.subThoroughfare
        } else {
            locality.thoroughfare
        }
    }

    fun isServiceRunningInForeground(): Boolean {
        return repository.isLocationSerivceRunning()
    }
}