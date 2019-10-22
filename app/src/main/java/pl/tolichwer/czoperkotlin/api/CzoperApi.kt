package pl.tolichwer.czoperkotlin.api

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.model.Position
import pl.tolichwer.czoperkotlin.model.User
import pl.tolichwer.czoperkotlin.model.utilityobjects.RemotePositionGeoJoin
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CzoperApi {

    @GET("user")
    fun getUsers(@Query("login") login: String, @Query("password") password: String): Flowable<Response<List<User>>>

    @Headers("Content-Type: application/json")
    @PUT("position/positionList/{userid}")
    fun sendPositionList(@Path("userid") userid: Int, @Body positionList: List<Position>): Call<Void>

    @PUT("position/{userid}")
    fun sendPosition(@Path("userid") userid: Int, @Body position: Position): Observable<Void>

    @PUT("geo/{userid}")
    fun sendGeo(@Path("userid") userid: Int, @Body geo: Geo): Observable<Void>

    @Headers("Content-Type: application/json")
    @PUT("geo/geoList/{userid}")
    fun sendGeoList(@Path("userid") userid: Int, @Body geoList: List<Geo>): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("assignGeoToPosition")
    fun assignGeoToPosition(@Body remotePositionGeoJoin: RemotePositionGeoJoin): Single<Void>

    @Headers("Content-Type: application/json")
    @POST("assignGeoToPosition/list")
    fun assignGeoToPositionList(@Body remotePositionGeoJoins: List<RemotePositionGeoJoin>): Call<Void>

    @GET("position/positionForDayAndUser")
    fun getPositionsForDayAndUser(@Query("userName") userName: String, @Query("rangeFrom") rangeFrom: Long, @Query("rangeTo") rangeTo: Long): Call<List<Position>>
}