package com.study.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 处理[MviIntent]并更新[MviViewState],同时可以发射[MviSingleEvent]
 *
 * @param I:MviIntent -> 表示[MviViewModel]将会订阅[MviIntent]类型
 * @param S:MviViewState -> 表示[MviViewModel]将会发射[MviViewState]类型
 * @param E:MviSingleEvent -> 表示[MviViewModel]将会发生[MviSingleEvent]的单个事件
 */
interface MviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    val viewState: StateFlow<S>

    val singleEvent: Flow<E>

    suspend fun processIntent(intent: I)
}