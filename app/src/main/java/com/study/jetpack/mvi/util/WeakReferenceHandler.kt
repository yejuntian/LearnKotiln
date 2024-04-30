package com.study.jetpack.mvi.util

import android.view.View
import java.lang.ref.WeakReference

/**
 * 通用的 WeakReferenceHandler 类来管理多个 View 的点击事件监听器
 */
open class WeakReferenceHandler {
    private val listenersMap: MutableMap<View, WeakReference<View.OnClickListener>> = mutableMapOf()
    fun setOnClickListener(view: View, listener: View.OnClickListener) {
        val weakRef = WeakReference(listener)
        listenersMap[view] = weakRef
        view.setOnClickListener(listener)
    }

    fun clearListeners() {
        for ((view, weakRef) in listenersMap) {
            view.setOnClickListener(null)
            weakRef.clear()
        }
        listenersMap.clear()
    }
}