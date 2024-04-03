package org.ninetripods.mq.study.jetpack.base.http.api

import com.study.jetpack.mvi.data.BaseData
import com.study.jetpack.mvi.data.RankModel
import com.study.jetpack.mvi.data.WanModel
import retrofit2.http.GET

interface DrinkService {

    @GET("banner/json")
    suspend fun getBanner(): BaseData<List<WanModel>>

    @GET("coin/rank/1/json")
    suspend fun getRankList(): BaseData<RankModel>
}