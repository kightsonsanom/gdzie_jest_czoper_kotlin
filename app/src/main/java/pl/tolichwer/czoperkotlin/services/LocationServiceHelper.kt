package pl.tolichwer.czoperkotlin.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.Position
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
            .observeOn(Schedulers.io())
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

            newPosition = Position(
                startDate = Constants.longToString(newGeo.date),
                lastLocationDate = newGeo.date,
                startLocation = address,
                status = Constants.PositionStatus.UNKNOWN.ordinal,
                userID = userID
            )
        } else if (isLatestGeoFromDbTooOld()) {

            newPosition = Position(
                startDate = Constants.longToString(latestPositionFromDB.lastLocationDate),
                endDate = Constants.longToString(newGeo.date),
                firstLocationDate = latestPositionFromDB.lastLocationDate,
                lastLocationDate = newGeo.date,
                status = Constants.PositionStatus.PAUSE.ordinal,
                userID = userID
            )

            sendGeoAndPosition(newGeo, newPosition)

            newPosition = Position(
                startDate = Constants.longToString(newGeo.date + NEW_POSITION_OFFSET),
                firstLocationDate = newGeo.date + NEW_POSITION_OFFSET,
                lastLocationDate = newGeo.date + NEW_POSITION_OFFSET,
                startLocation = address,
                status = Constants.PositionStatus.UNKNOWN.ordinal,
                userID = userID
            )
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
        } else if (latestPositionFromDB.status == Constants.PositionStatus.STOP.ordinal) {

            if (isLastGeoFarAway()) {

                newPosition = Position(
                    startDate = latestPositionFromDB.endDate,
                    endDate = Constants.longToString(newGeo.date),
                    firstLocationDate = newGeo.date,
                    lastLocationDate = newGeo.date + NEW_POSITION_OFFSET,
                    startLocation = latestPositionFromDB.startLocation,
                    endLocation = address,
                    status = Constants.PositionStatus.MOVE.ordinal,
                    userID = userID
                )
            } else {
                newPosition = latestPositionFromDB
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.lastLocationDate = newGeo.date
            }
        } else if (latestPositionFromDB.status == Constants.PositionStatus.MOVE.ordinal) {

            if (isLastGeoFarAway()) {
                newPosition = latestPositionFromDB

                newPosition.lastLocationDate = newGeo.date
                newPosition.endDate = Constants.longToString(newGeo.date)
                newPosition.endLocation = address
            } else {

                newPosition = Position(
                    startDate = latestPositionFromDB.endDate,
                    endDate = Constants.longToString(newGeo.date),
                    firstLocationDate = newGeo.date,
                    lastLocationDate = newGeo.date + NEW_POSITION_OFFSET,
                    startLocation = address,
                    status = Constants.PositionStatus.MOVE.ordinal,
                    userID = userID
                )
            }
        } else {
            throw RuntimeException("Wrong position status")
        }

        sendGeoAndPosition(newGeo, newPosition)
    }

    private fun sendGeoAndPosition(newGeo: Geo, newPosition: Position) {
        repository.sendGeoAndPosition(newGeo, newPosition)
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