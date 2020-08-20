package com.dimi.superheroapp.business.data.util


import com.dimi.superheroapp.business.data.cache.CacheResponseHandler
import com.dimi.superheroapp.business.data.network.ApiResult
import com.dimi.superheroapp.business.data.network.NetworkErrors.NETWORK_ERROR
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.util.GenericErrors.ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@FlowPreview
abstract class NetworkBoundResource<NetworkResponse, CacheResponse, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkResponse?,
    private val cacheCall: suspend () -> CacheResponse?
) {

    val result: Flow<DataState<ViewState>?> = flow {

        emit(returnCache(markJobComplete = false, cacheResponse = null))

        val apiResult = safeApiCall(dispatcher){apiCall.invoke()}

        var response: Response? = null

        when(apiResult){
            is ApiResult.GenericError -> {
                emit(
                    buildError(
                        apiResult.errorMessage?.let { it }?: ERROR_UNKNOWN,
                        UIComponentType.Dialog(),
                        stateEvent
                    )
                )
            }

            is ApiResult.NetworkError -> {
                emit(
                    buildError(
                        NETWORK_ERROR,
                        UIComponentType.Dialog(),
                        stateEvent
                    )
                )
            }

            is ApiResult.Success -> {
                if(apiResult.value == null){
                    emit(
                        buildError(
                            ERROR_UNKNOWN,
                            UIComponentType.Dialog(),
                            stateEvent
                        )
                    )
                }
                else {
                    response = updateCache(apiResult.value)
                }
            }
        }

        emit(returnCache(markJobComplete = true, cacheResponse = response))
    }

    private suspend fun returnCache(markJobComplete: Boolean, cacheResponse: Response?): DataState<ViewState>? {

        val cacheResult = safeCacheCall(dispatcher){cacheCall.invoke()}

        var jobCompleteMarker: StateEvent? = null
        if(markJobComplete){
            jobCompleteMarker = stateEvent
        }

        return object: CacheResponseHandler<ViewState, CacheResponse>(
            response = cacheResult,
            stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(resultObj: CacheResponse): DataState<ViewState> {

                return handleCacheSuccess(resultObj, cacheResponse, jobCompleteMarker)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkResponse) : Response?

    abstract fun handleCacheSuccess(resultObj: CacheResponse, cacheUpdateResponse: Response?, stateEvent: StateEvent?): DataState<ViewState>
}















