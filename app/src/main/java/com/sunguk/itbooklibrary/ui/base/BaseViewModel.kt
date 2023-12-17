package com.sunguk.itbooklibrary.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

/**
 * @param initialState - The initial state of viewModel. Highly recommended to make this immutable.
 */
abstract class BaseViewModel<STATE : Any, EVENT : Any>(
    initialState: STATE,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = intentScope.coroutineContext

    private val intentScope =
        viewModelScope + Dispatchers.Default + CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

    private val _stateFlow = MutableStateFlow(initialState)
    val state: STATE
        get() = _stateFlow.value
    val stateFlow: StateFlow<STATE>
        get() = _stateFlow

    private val _eventFlow: Channel<EVENT> = Channel(Channel.BUFFERED)
    val eventFlow: Flow<EVENT>
        get() = _eventFlow.receiveAsFlow()

    /**
     * update new event to handle
     */
    protected suspend fun sendEvent(event: EVENT) {
        _eventFlow.send(event)
    }

    /**
     * update new state
     */
    protected suspend fun updateState(state: STATE) {
        _stateFlow.emit(state)
    }

    override fun onCleared() {
        super.onCleared()
        intentScope.cancel()
    }
}