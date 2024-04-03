package org.ninetripods.mq.study.jetpack.mvvm.repo

import com.study.jetpack.mvi.data.BaseData
import com.study.jetpack.mvi.data.RankModel
import com.study.jetpack.mvi.data.WanModel
import com.study.jetpack.mvi.net.RetrofitUtil
import com.study.jetpack.mvi.repo.BaseRepository
import org.ninetripods.mq.study.jetpack.base.http.api.DrinkService

class WanRepository : BaseRepository() {
    val service = RetrofitUtil.getService(DrinkService::class.java)

    suspend fun requestWanData(drinkId: String): BaseData<List<WanModel>> {
        return executeRequest { service.getBanner() }
    }

    suspend fun requestRankData(): BaseData<RankModel> {
        return executeRequest { service.getRankList() }
    }
}