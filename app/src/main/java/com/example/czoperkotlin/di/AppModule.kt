package com.example.czoperkotlin.di

import android.app.Application
import androidx.room.Room
import com.example.czoperkotlin.api.CzoperApi
import com.example.czoperkotlin.db.AppDatabase
import com.example.czoperkotlin.db.SharedPreferencesRepository
import com.example.czoperkotlin.db.dao.GeoDao
import com.example.czoperkotlin.db.dao.UserDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferencesRepository {
        return SharedPreferencesRepository(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {

        return GsonBuilder()
            // .registerTypeAdapter(Geo.class, new GeoSerializingAdapter())
            // .registerTypeAdapter(Geo.class, new GeoDeserializingAdapter())
            // .registerTypeAdapter(Position.class, new PositionAdapter())
            .serializeNulls()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideCzoperApi(gson: Gson): CzoperApi {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://94.23.53.86:8585/czoper/api/")
//                .baseUrl("http://192.168.1.132:8585/api/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CzoperApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "actions.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideGeoDao(database: AppDatabase): GeoDao {
        return database.geoDao()
    }
}