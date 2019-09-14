package com.example.czoperkotlin.db

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.czoperkotlin.api.CzoperApi
import com.example.czoperkotlin.db.dao.GeoDao
import com.example.czoperkotlin.db.dao.UserDao
import com.example.czoperkotlin.db.ssot.ApiResource
import com.example.czoperkotlin.db.ssot.NetworkBoundResource
import com.example.czoperkotlin.model.Geo
import com.example.czoperkotlin.model.User
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val czoperApi: CzoperApi,
    private val geoDao: GeoDao,
    private val userDao: UserDao,
    private val context: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    fun repoTest(): String {
        return "dziala"
    }

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
            .map userList@{
                return@userList it.map {
                    return@map it.name
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
}