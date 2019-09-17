package pl.tolichwer.czoperkotlin.db

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.tolichwer.czoperkotlin.api.ApiResource
import pl.tolichwer.czoperkotlin.util.isNetworkAvailable
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>(context: Context) {

    private val result: Flowable<ApiResource<ResultType>>

    init {
        val diskObservable = Flowable.defer {
            loadFromDb()
                .subscribeOn(Schedulers.computation())
                .map {
                    return@map it

                }
        }

        val networkObservable = Flowable.defer {
            createCall()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map {
                    return@map it
                }
                .doOnNext { response: Response<RequestType> ->
                    if (response.isSuccessful) {
                        saveCallResult(processResponse(response))
                    }
                }
                .onErrorReturn { throwable: Throwable ->
                    throw throwable
                }
                .flatMap { loadFromDb() }
        }

        result = when {
            context.isNetworkAvailable() -> networkObservable
                .map<ApiResource<ResultType>> {
                    ApiResource.Success(it)
                }
                .onErrorReturn {
                    ApiResource.Failure(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(ApiResource.Loading())

            else -> diskObservable
                .map<ApiResource<ResultType>> {
                    ApiResource.Success(it)
                }
                .onErrorReturn {
                    ApiResource.Failure(it)
                }
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
