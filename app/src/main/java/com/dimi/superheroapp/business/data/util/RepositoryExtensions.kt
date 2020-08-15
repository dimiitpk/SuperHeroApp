package com.dimi.superheroapp.business.data.util

import com.dimi.superheroapp.business.data.network.ApiResult
import com.dimi.superheroapp.business.data.network.NetworkConstants.NETWORK_TIMEOUT
import com.dimi.superheroapp.business.data.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.dimi.superheroapp.business.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.dimi.superheroapp.util.GenericErrors.ERROR_UNKNOWN
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT){
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)

                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {

                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}


private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}























