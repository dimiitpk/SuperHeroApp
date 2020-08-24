package com.dimi.superheroapp.business.data.network

import com.dimi.superheroapp.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.dimi.superheroapp.business.data.network.NetworkErrors.NETWORK_ERROR
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.business.domain.state.ViewState as ViewStateInterface


abstract class ApiResponseHandler <ViewState : ViewStateInterface, Data>(
    private val viewState: ViewState,
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage.toString()}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_ERROR",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_DATA_NULL.",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    viewState.setData(response.value)
                    handleSuccess(resultViewState = viewState)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultViewState: ViewState): DataState<ViewState>?

}