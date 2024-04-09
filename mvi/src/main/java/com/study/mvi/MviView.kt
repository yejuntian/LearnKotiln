package com.study.mvi

import kotlinx.coroutines.flow.Flow


/**
 * 表示一个UI界面
 * 具有以下功能：
 * 1.发射其意图给视图模型
 * 2.订阅视图模型来渲染其UI
 * 3.订阅一个视图模型来处理单个事件
 *
 * @param I:MviIntent -> 表示[MviView]将会发射的意图
 * @param S:MviViewState -> 表示[MviView]将会订阅视图的状态
 * @param E:MviSingleEvent -> 表示[MviView]将会订阅的单个事件
 */
interface MviView<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    /**
     * 渲染UI界面的入口，基于[MviViewState]来渲染UI界面
     */
    fun render(viewState: S)

    /**
     * 处理单个事件的入口点
     */
    fun handleSingleEvent(event: E)

    /**
     * 获取视图意图的Flow，返回Flow对象。
     * 让[MviViewModel]监听[MviView]的意图，所有[MviView]的意图必须通过这个Flow传递。
     */
    fun viewIntents(): Flow<I>
}