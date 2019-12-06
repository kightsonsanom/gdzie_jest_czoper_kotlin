package pl.tolichwer.czoperkotlin.db

import android.content.Context
import pl.tolichwer.czoperkotlin.util.isNetworkAvailable
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>(context: Context) {

    private val result: ResultType

    init {
        result = when {
            context.isNetworkAvailable() -> {
                val result: RequestType = createCall()
                saveCallResult(result)
                loadFromDb()
            }

            else ->
                loadFromDb()
        }
    }


    private fun processResponse(response: Response<RequestType>): RequestType {
        return response.body()!!
    }

    protected abstract fun saveCallResult(request: RequestType)

    protected abstract fun loadFromDb(): ResultType

    protected abstract fun createCall(): RequestType
}
