package com.study.jetpack.mvi.repo

import com.study.jetpack.mvi.data.BaseData
import com.study.jetpack.mvi.data.ReqState

open class BaseRepository {

    suspend fun <T : Any> executeRequest(
        block: suspend () -> BaseData<T>
    ): BaseData<T> {
        val baseData = block.invoke()
        if (baseData.code == 0) {
            //正确
            baseData.state = ReqState.Success
        } else {
            //错误
            baseData.state = ReqState.Error
        }
        return baseData
    }
}