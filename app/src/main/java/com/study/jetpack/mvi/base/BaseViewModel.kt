package com.study.jetpack.mvi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.jetpack.mvi.data.BaseData
import com.study.jetpack.mvi.data.ReqState
import com.study.jetpack.mvi.data.WanModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel基类
 * @param uiState: 重复性事件，view层可以多次接收并刷新
 * @param singleUiState:一次性事件，view层不支持多次消费，如弹Toast，导航Activity等
 */
abstract class BaseViewModel<uiState : IUiState, singleUiState : ISingleUiState> : ViewModel() {
    /**
     * 可以重复消费的事件
     */
    private val _uiStatuesFlow = MutableStateFlow(initUiState())
    val uiStatueFlow: StateFlow<uiState> = _uiStatuesFlow

    /**
     * 强制需要子类实现
     */
    protected abstract fun initUiState(): uiState

    /**
     * 一次性事件，一对一关系
     * channel特点：
     * 1.每个消息只有一个订阅者可以收到，用于一对一通信
     * 2.第一个订阅者可以收到collect之前的事件
     */
    private val _sUIStateFlow: Channel<singleUiState> = Channel()
    val sUIStateFlow: Flow<singleUiState> = _sUIStateFlow.receiveAsFlow()

    private val _loadUIStatusFlow: Channel<LoadUiState> = Channel()
    val loadUIStatusFlow: Flow<LoadUiState> = _loadUIStatusFlow.receiveAsFlow()

    /**
     * 据当前的 UI 状态对象生成一个新的 UI 状态对象，
     * 并将新生成的 UI 状态对象更新到 _uiStateFlow 中
     */
    protected fun sendUIStats(copy: uiState.() -> uiState) {
        _uiStatuesFlow.update {
            _uiStatuesFlow.value.copy()
        }
    }

    protected fun sendSingleUIState(sUIState: singleUiState) {
        viewModelScope.launch {
            _sUIStateFlow.send(sUIState)
        }
    }

    /**
     * 发送当前加载状态： Loading、Error、Normal
     */
    private fun sendLoadUIStatue(loadUiState: LoadUiState) {
        viewModelScope.launch {
            _loadUIStatusFlow.send(loadUiState)
        }
    }

    protected fun <T : Any> requestDataWithFlow(
        showLoading: Boolean = true,
        request: suspend () -> BaseData<T>,
        successCallBack: (T) -> Unit,
        failCallback: suspend (String) -> Unit = { errorMsg ->
            //默认异常处理，子类可以进行覆写
            sendLoadUIStatue(LoadUiState.Error(errorMsg))
        }
    ) {
        viewModelScope.launch {
            //是否展示loading
            if (showLoading) {
                sendLoadUIStatue(LoadUiState.Loading(true))
            }
            val baseData: BaseData<T>
            try {
                baseData = request()
                when (baseData.state) {
                    ReqState.Success -> {
                        sendLoadUIStatue(LoadUiState.ShowMainView)
                        baseData.data?.let { successCallBack(it) }
                    }

                    ReqState.Error -> baseData.msg?.let {
                        error(it)
                    }
                }
            } catch (e: Exception) {
                e.message?.let {
                    failCallback(it)
                }
            } finally {
                if (showLoading) {
                    sendLoadUIStatue(LoadUiState.Loading(false))
                }
            }
        }
    }
}