package pl.tolichwer.czoperkotlin.db

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import pl.tolichwer.czoperkotlin.api.ApiResource
import pl.tolichwer.czoperkotlin.api.CzoperApi
import pl.tolichwer.czoperkotlin.db.dao.GeoDao
import pl.tolichwer.czoperkotlin.db.dao.PositionDao
import pl.tolichwer.czoperkotlin.db.dao.PositionGeoJoinDao
import pl.tolichwer.czoperkotlin.db.dao.UserDao
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.Position
import pl.tolichwer.czoperkotlin.model.PositionGeoJoin
import pl.tolichwer.czoperkotlin.model.User
import pl.tolichwer.czoperkotlin.model.utilityobjects.RemotePositionGeoJoin
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val czoperApi: CzoperApi,
    private val geoDao: GeoDao,
    private val userDao: UserDao,
    private val positionDao: PositionDao,
    private val positionGeoJoinDao: PositionGeoJoinDao,
    private val context: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    fun getUsers(login: String, password: String): List<User> {
        return czoperApi.getUsers(login,password)
    }

    suspend fun getUsersFromDB(): List<User> {
        return userDao.getAllUsers()
    }

    fun isLocationSerivceRunning(): Boolean {
        return sharedPreferencesRepository.isLocationServiceRunning()
    }

    fun getUserID(): Int {
        return sharedPreferencesRepository.getCurrentUserID()
    }

    @SuppressLint("CheckResult")
    fun insertGeo(geo: Geo) {
        geoDao.insertGeo(geo)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("insertGeoTag", "Error inserting Geo in $it row") },
                onSuccess = { Log.d("insertGeoTag", "inserted Geo in $it row") }
            )
    }

    fun getLatesGeoFromDB(userID: Int): Single<Geo> {
        return geoDao.loadLatestGeo(userID)
    }

    fun getLatestPositionFromDB(userID: Int): Single<Position> {
        return positionDao.getLatestPositionFromDB(userID)
    }

    fun getOldestGeoForPosition(positionid: Long): Single<Geo> {
        return positionGeoJoinDao.getOldestGeoForPosition(positionid)
    }

    private fun assignGeoToPositionOnServer(positionGeoJoin: PositionGeoJoin) {
        czoperApi.assignGeoToPosition(RemotePositionGeoJoin(positionGeoJoin.positionid, positionGeoJoin.geoid))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    sharedPreferencesRepository.setFailureTimestamp(positionGeoJoin.assignTime)
                },
                onSuccess = {
                    sharedPreferencesRepository.setFailureTimestamp(0)
                }
            )
    }

    fun sendGeoAndPosition(newGeo: Geo, newPosition: Position) {
        val positionGeoJoin = PositionGeoJoin(newPosition.id, newGeo.id, newGeo.date)

        geoDao.insertGeo(newGeo)
            .zipWith(positionDao.insertPosition(newPosition)) { geoResponse, positionRespone ->
                Log.d("inserResponse", "geoResponse = $geoResponse and positionResponse = $positionRespone ")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("insertMergeError", "$it ") },

                onSuccess = {

                    getDatabaseInfo()
                    Log.d("onCompleteMerge", "$it, positionjoin = $positionGeoJoin")
                    positionGeoJoinDao.insert(positionGeoJoin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribeBy(
                            onError = { Log.d("insertAssignError", "$it ") },
                            onSuccess = { Log.d("insertAssignSuccess", "$it") }
                        )
                }
            )

        // geoDao.insertGeo(newGeo)
        //     .subscribeOn(Schedulers.io())
        //     .subscribeBy(
        //         onError = { Log.d("insertGeoError", "$it ") },
        //         onSuccess = { Log.d("insertGeoSuccess", "$it") }
        //     )
        //
        // positionDao.insertPosition(newPosition)
        //     .subscribeOn(Schedulers.io())
        //     .subscribeBy(
        //         onError = { Log.d("insertPositionError", "$it ") },
        //         onSuccess = { Log.d("insertPositionSuccess", "$it") }
        //     )

        val geoObservable = czoperApi.sendGeo(newGeo.userID, newGeo)
        val positionObservable = czoperApi.sendPosition(newPosition.userID, newPosition)

        Observable.merge(
            geoObservable.subscribeOn(Schedulers.io()),
            positionObservable.subscribeOn(Schedulers.io())
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.d("sendGeoAndPositionTag", "onError: $it")
                    sharedPreferencesRepository.putPositionID(newGeo.id)
                    sharedPreferencesRepository.putPositionID(newPosition.id)
                    sharedPreferencesRepository.setFailureTimestamp(newGeo.date)

                },
                onComplete = {
                    Log.d("sendGeoAndPositionTag", "onComplete")

                    sharedPreferencesRepository.putGeoID(0)
                    sharedPreferencesRepository.putPositionID(0)
                    assignGeoToPositionOnServer(positionGeoJoin)
                }
            )
    }

    private fun getDatabaseInfo() {
        getAllGeos()
        getAllPositions()
        getAllAssigns()
    }

    private fun getAllGeos(): Disposable {
        return geoDao.getAllGeos()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("getAllGeos", "Error getting geos ") },
                onSuccess = { Log.d("getAllGeos", "Success! List of geos: ${it.map { return@map "$it\n" }}") }
            )
    }

    private fun getAllPositions(): Disposable {
        return positionDao.getAllPositions()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("getAllPositions", "Error getting positions ") },
                onSuccess = { Log.d("getAllPositions", "Success! List of positions: ${it.map { return@map "$it\n" }}") }
            )
    }

    private fun getAllAssigns(): Disposable {
        return positionGeoJoinDao.getAllAssigns()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("getAllAssigns", "Error getting assigns ") },
                onSuccess = { Log.d("getAllAssigns", "Success! List of assigns: ${it.map { return@map "$it\n" }}") }
            )
    }
}

