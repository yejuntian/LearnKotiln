package com.study.mvi.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ObjectStore {
    @Volatile
    var context: Context? = null
}