package com.study

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.study.mvi.exetends.showToast

/**
 * 监听Application的生命周期，可以用来判断应用前后台判断
 */
class MyApplicationLifecycleObserver : LifecycleObserver {
    private val TAG:String  ="MyApplicationLifecycleObserver"
    @SuppressLint("LongLogTag")
    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    fun onAppForeground() {
        Log.e(TAG, "onAppForeground")
        showToast("应用处于前台")
    }

    @SuppressLint("LongLogTag")
    @OnLifecycleEvent(value = Lifecycle.Event.ON_STOP)
    fun onAppBackground() {
        Log.e(TAG, "onAppBackground")
        showToast("应用处于后台")
    }
}