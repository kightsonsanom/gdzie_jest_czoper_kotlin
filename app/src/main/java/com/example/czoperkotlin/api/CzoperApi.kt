package com.example.czoperkotlin.api

import com.example.czoperkotlin.model.User
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CzoperApi{

    @GET("user")
    fun getUsers(@Query("login")login: String, @Query("password")password: String): Flowable<Response<List<User>>>

}