package com.study.mvi

import android.os.Bundle

/**
 * 不可变对象，渲染 [MviView] 所需要的所有信息
 */
interface MviViewState


/**
 * 用于将 [MviViewState] 转换为 [Bundle]，反之亦然
 */
interface MviViewStateSaver<S : MviViewState> {
    fun S.toBundle(): Bundle

    fun restore(bundle: Bundle?): S
}