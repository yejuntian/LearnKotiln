package com.study.jetpack.mvi.base

sealed class LoadUiState {
    data class Loading(val isShow: Boolean) : LoadUiState()

    object ShowMainView : LoadUiState()

    data class Error(val msg: String) : LoadUiState()
}
