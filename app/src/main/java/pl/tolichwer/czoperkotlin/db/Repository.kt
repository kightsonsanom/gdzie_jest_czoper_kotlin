package pl.tolichwer.czoperkotlin.db

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
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

    fun getUsers(login: String, password: String): Flowable<ApiResource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<User>>(context) {
            override fun saveCallResult(request: List<User>) {
                userDao.insertAll(request)
            }

            override fun loadFromDb(): Flowable<List<User>> {
                return userDao.getAllUsers()
            }

            override fun createCall(): Flowable<Response<List<User>>> {
                return czoperApi.getUsers(login, password)
                    .map {
                        val currentUser =
                            it.body()
                                ?.filter { user ->
                                    user.name == login && user.password == password
                                }

                        sharedPreferencesRepository.saveCurrentUserID(currentUser!![0].userID)
                        return@map it
                    }
            }
        }.asFlowable()
    }

    fun getUsersFromDB(): Flowable<List<User>> {
        return userDao.getAllUsers()
    }

    fun getUserNamesFromDB(): Flowable<List<String>> {
        return userDao.getAllUsers()
            .map {
                it.map {
                    it.name
                }
            }
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

    fun getAllGeos(): Disposable {
        return geoDao.getAllGeos()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { Log.d("getAllGeos", "Error getting geos ") },
                onSuccess = { Log.d("getAllGeos", "Success! List of geos: ${it.map { return@map "$it\n" }}") }
            )
    }

    fun getLatesGeoFromDB(userID: Int): Single<Geo> {
        return geoDao.loadLatestGeo(userID)
    }

    fun getLatestPositionFromDB(userID: Int): Single<Position> {
        return positionDao.getLatestPositionFromDB(userID)
    }

    fun getOldestGeoForPosition(positionId: Long): Single<Geo> {
        return positionGeoJoinDao.getOldestGeoForPosition(positionId)
    }

    fun assignGeoToPosition(positionGeoJoin: PositionGeoJoin) {
        positionGeoJoinDao.insert(positionGeoJoin)
    }

    fun sendGeoAndPosition(newGeo: Geo, newPosition: Position) {
        geoDao.insertGeo(newGeo)
        positionDao.insertPosition(newPosition)
        positionGeoJoinDao.insert(PositionGeoJoin(newGeo.id, newPosition.id, newGeo.date))

        val geoObservable = czoperApi.sendGeo(newGeo.userID, newGeo)
        val positionObservable = czoperApi.sendPosition(newPosition.userID, newPosition)

        // positionObservable.zipWith(geoObservable, BiFunction(Position,Geo))
    }
}