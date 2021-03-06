package com.dimi.superheroapp.presentation.common

import android.util.Log
import androidx.lifecycle.*
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.util.GenericErrors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import com.dimi.superheroapp.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.flow.flow

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState : ViewStateInterface>
constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    private var savingStateObserver: Observer<ViewState>

    init {

        getViewState()?.let { viewState ->
            setViewState(viewState)
        }

        savingStateObserver = Observer() {
            savedStateHandle.set(
                getUniqueViewStateIdentifier(),
                getViewStateCopyWithoutBigLists(it)
            )
        }
        _viewState.observeForever(savingStateObserver)
    }

    private val dataChannelManager: DataChannelManager<ViewState> =
        object : DataChannelManager<ViewState>() {

            override fun handleNewData(data: ViewState) {
                this@BaseViewModel.handleNewData(data)
            }
        }

    val viewState: LiveData<ViewState>
        get() = _viewState

    val shouldDisplayProgressBar: LiveData<Boolean> = dataChannelManager.shouldDisplayProgressBar

    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    // FOR DEBUGGING
    fun getMessageStackSize(): Int {
        return dataChannelManager.messageStack.size
    }

    fun setupChannel() = dataChannelManager.setupChannel()

    abstract fun handleNewData(data: ViewState)

    abstract fun setStateEvent(stateEvent: StateEvent)

    fun emitStateMessageEvent(
        stateMessage: StateMessage,
        stateEvent: StateEvent
    ) = flow {
        emit(
            DataState.error<ViewState>(
                response = stateMessage.response,
                stateEvent = stateEvent
            )
        )
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return dataChannelManager.isJobAlreadyActive(stateEvent)
    }

    fun emitInvalidStateEvent(stateEvent: StateEvent) = flow {
        emit(
            DataState.error<ViewState>(
                response = Response(
                    message = GenericErrors.INVALID_STATE_EVENT,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Error()
                ),
                stateEvent = stateEvent
            )
        )
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>?>
    ) = dataChannelManager.launchJob(stateEvent, jobFunction)

    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun clearStateMessage(index: Int = 0) {
        Log.d("BaseViewModel", "clearStateMessage")
        dataChannelManager.clearStateMessage(index)
    }

    fun clearActiveStateEvents() = dataChannelManager.clearActiveStateEventCounter()

    fun clearAllStateMessages() = dataChannelManager.clearAllStateMessages()

    fun printStateMessages() = dataChannelManager.printStateMessages()

    fun cancelActiveJobs() = dataChannelManager.cancelJobs()

    abstract fun initNewViewState(): ViewState

    private fun getViewState(): ViewState? {
        return savedStateHandle.get(getUniqueViewStateIdentifier())
    }

    abstract fun getViewStateCopyWithoutBigLists(viewState: ViewState): ViewState

    abstract fun getUniqueViewStateIdentifier(): String

    override fun onCleared() {
        super.onCleared()

        if (_viewState.hasActiveObservers())
            _viewState.removeObserver(savingStateObserver)
    }

}








