package com.example.czoperkotlin.db

import android.content.Context
import com.example.czoperkotlin.api.CzoperApi
import com.example.czoperkotlin.db.dao.GeoDao
import com.example.czoperkotlin.db.dao.UserDao
import com.example.czoperkotlin.db.ssot.ApiResource
import com.example.czoperkotlin.db.ssot.NetworkBoundResource
import com.example.czoperkotlin.model.User
import io.reactivex.Flowable
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val czoperApi: CzoperApi,
    private val geoDao: GeoDao,
    private val userDao: UserDao,
    private val context: Context,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {


    fun repoTest():String{
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




}