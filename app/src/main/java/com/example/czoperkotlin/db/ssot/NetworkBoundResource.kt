package com.example.czoperkotlin.db.ssot

import android.content.Context
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>(context: Context) {

    // result is a Flowable because Room Database only returns Flowables
    // Retrofit response will also be folded into the stream as a Flowable
    private val result: Flowable<ApiResource<ResultType>>

    init {
        // Lazy disk observable.
        val diskObservable = Flowable.defer {
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
                .map {
                    Log.d("logtag", "disk observable = $it")
                        return@map it

                }
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()
                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .map {
                    Log.d("logtag", "createCall = $it")
                    return@map it
                }
                .doOnNext { response: Response<RequestType> ->
                    Log.d("Logtag", "response $response")
                    if (response.isSuccessful) {
                        saveCallResult(processResponse(response))
                    }
                }
                .onErrorReturn { throwable: Throwable ->
                    Log.d("Logtag", "response $throwable")
                    throw throwable
                }
                .flatMap { loadFromDb() }
        }

        result = when {
            context.isNetworkStatusAvailable() -> networkObservable
                .map<ApiResource<ResultType>> {
                    Log.d("Logtag", "networkObservable $it")
                    ApiResource.Success(it) }
                .onErrorReturn {
                    Log.d("Logtag", "networkObservable onErrorReturn $it")
                    ApiResource.Failure(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(ApiResource.Loading())
            else -> diskObservable
                .map<ApiResource<ResultType>> {
                    Log.d("Logtag", "diskObservable  $it")
                    ApiResource.Success(it) }
                .onErrorReturn {
                    Log.d("Logtag", "diskObservable onErrorReturn $it")
                    ApiResource.Failure(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(ApiResource.Loading())
        }
    }

    fun asFlowable(): Flowable<ApiResource<ResultType>> {
        return result
    }

    private fun processResponse(response: Response<RequestType>): RequestType {
        return response.body()!!
    }

    protected abstract fun saveCallResult(request: RequestType)

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<Response<RequestType>>
}
