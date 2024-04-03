package com.study.jetpack.mvi.viewmodel

import com.study.jetpack.mvi.base.BaseViewModel
import com.study.jetpack.mvi.base.ISingleUiState
import com.study.jetpack.mvi.base.IUiState
import com.study.jetpack.mvi.data.RankModel
import com.study.jetpack.mvi.data.WanModel
import org.ninetripods.mq.study.jetpack.mvvm.repo.WanRepository

open class MViewModel : BaseViewModel<MViewModel.MviState, MViewModel.MviSingleUiState>() {
    //Repository中间层 管理所有数据来源 包括本地的及网络的
    private val mWanRepo = WanRepository()
    override fun initUiState(): MviState {
        return MviState(BannerUIState.Init, DetailUIStatue.Init)
    }

    //请求banner数据
    fun loadBannerData() {
        requestDataWithFlow(
            showLoading = true,
            request = { mWanRepo.requestWanData("") },
            successCallBack = { data ->
                sendUIStats { copy(bannerUiState = BannerUIState.Success(data)) }
            })
    }

    //请求List数据
    fun loadDetailData(){
        requestDataWithFlow(
            showLoading = false,
            request = { mWanRepo.requestRankData() },
            successCallBack = { data ->
                sendUIStats { copy(detailUiState = DetailUIStatue.Success(data)) }
            })
    }

    fun showToast() {
        sendSingleUIState(MviSingleUiState("触发了一次性消费事件！"))
    }


    /**
     * 定义UiState将view层所有实体类相关的都包括在这里，
     * 可以有效的避免模版代码（StateFlow只需要定义一个即可）
     */
    data class MviState(val bannerUiState: BannerUIState, val detailUiState: DetailUIStatue) :
        IUiState

    data class MviSingleUiState(val message: String) : ISingleUiState

    sealed class BannerUIState {
        object Init : BannerUIState()
        data class Success(val models: List<WanModel>) : BannerUIState()
    }

    sealed class DetailUIStatue {
        object Init : DetailUIStatue()
        data class Success(val details: RankModel) : DetailUIStatue()
    }


}