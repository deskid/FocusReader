package com.github.deskid.focusreader.api.data

sealed class UIState {
    data class LoadingState(val cancelable: Boolean = true) : UIState()

    open class LoadedState : UIState()

    data class ErrorState(val msg: String?) : UIState()

    open class NetworkErrorState : UIState()
}