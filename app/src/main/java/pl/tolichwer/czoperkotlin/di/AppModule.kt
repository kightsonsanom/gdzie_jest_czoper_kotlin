package pl.tolichwer.czoperkotlin.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.tolichwer.czoperkotlin.api.CzoperApi
import pl.tolichwer.czoperkotlin.db.AppDatabase
import pl.tolichwer.czoperkotlin.db.SharedPreferencesRepository
import pl.tolichwer.czoperkotlin.db.dao.GeoDao
import pl.tolichwer.czoperkotlin.db.dao.PositionDao
import pl.tolichwer.czoperkotlin.db.dao.PositionGeoJoinDao
import pl.tolichwer.czoperkotlin.db.dao.UserDao
import pl.tolichwer.czoperkotlin.model.Geo
import pl.tolichwer.czoperkotlin.util.GeoDeserializingAdapter
import pl.tolichwer.czoperkotlin.util.GeoSerializingAdapter
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
            .registerTypeAdapter(Geo::class.java, GeoSerializingAdapter())
            .registerTypeAdapter(Geo::class.java, GeoDeserializingAdapter())
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

    @Provides
    @Singleton
    fun providePositionDao(database: AppDatabase): PositionDao {
        return database.positionDao()
    }

    @Provides
    @Singleton
    fun providePositionGeoJoinDao(database: AppDatabase): PositionGeoJoinDao {
        return database.positionGeoJoinDao()
    }
}