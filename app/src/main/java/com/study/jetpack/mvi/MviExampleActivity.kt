package com.study.jetpack.mvi

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.jetpack.mvi.adapter.RankAdapter
import com.study.jetpack.mvi.base.LoadUiState
import com.study.jetpack.mvi.viewmodel.MViewModel
import com.study.learnkotiln.R
import org.ninetripods.lib_viewpager2.MVPager2
import org.ninetripods.mq.study.kotlin.ktx.flowWithLifecycle2
import org.ninetripods.mq.study.kotlin.ktx.id
import org.ninetripods.mq.study.kotlin.ktx.showToast
import org.ninetripods.mq.study.kotlin.ktx.visible


class MviExampleActivity : BaseMviActivity() {
    private val mBtnQuest: Button by id(R.id.btn_request)
    private val mToolBar: Toolbar by id(R.id.toolbar)
    private val mContentView: ViewGroup by id(R.id.cl_content_view)

    private val mViewPager2: MVPager2 by id(R.id.mvp_pager2)
    private val mRvRank: RecyclerView by id(R.id.rv_view)

    private val mViewModel: MViewModel by viewModels()
    private val adapter by lazy { RankAdapter() }
    override fun getLayoutId(): Int {
        return R.layout.activity_android_mvi
    }

    override fun initView() {
        // 创建LinearLayoutManager并设置方向
        val layoutManager = LinearLayoutManager(this)
        // 或者LinearLayoutManager.HORIZONTAL
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        // 设置RecyclerView的布局管理器
        mRvRank.layoutManager = layoutManager
        mRvRank.adapter = adapter
    }

    override fun initEvents() {
        registerEvent()
        mBtnQuest.setOnClickListener {
            mViewModel.showToast() //一次性消费
            mViewModel.loadBannerData()
            mViewModel.loadDetailData()
        }
    }

    private fun registerEvent() {
        /**
         * Load加载事件 Loading、Error、ShowMainView
         */
        mViewModel.loadUIStatusFlow.flowWithLifecycle2(this) { state ->
            when (state) {
                is LoadUiState.Error -> {
                    mStatusViewUtil.showErrorView(state.msg)
                    showToast("LoadUiState.Error")
                }

                is LoadUiState.ShowMainView -> {
                    showToast("LoadUiState.ShowMainView")
                    mStatusViewUtil.showMainView()
                }

                is LoadUiState.Loading -> {
                    showToast("LoadUiState.Loading $state")
                    mStatusViewUtil.showLoadingView(state.isShow)
                }
            }
        }
        /**
         * 一次性消费事件
         */
        mViewModel.sUIStateFlow.flowWithLifecycle2(this) { data ->
            showToast(data.message)
        }

        mViewModel.uiStatueFlow.flowWithLifecycle2(
            this,
            prop1 = MViewModel.MviState::bannerUiState
        ) { state ->
            when (state) {
                is MViewModel.BannerUIState.Init -> {}
                is MViewModel.BannerUIState.Success -> {
                    mViewPager2.visible()
                    mBtnQuest.visibility = View.GONE
                    val imgs = mutableListOf<String>()
                    for (model in state.models) {
                        imgs.add(model.imagePath)
                    }
                    mViewPager2.setIndicatorShow(true).setModels(imgs).start()
                }
            }

        }

        mViewModel.uiStatueFlow.flowWithLifecycle2(
            this, Lifecycle.State.STARTED,
            prop1 = MViewModel.MviState::detailUiState
        ) { state ->
            when (state) {
                is MViewModel.DetailUIStatue.Init -> {}
                is MViewModel.DetailUIStatue.Success -> {
                    mRvRank.visibility = View.VISIBLE
                    val list = state.details.datas
                    adapter.setModels(list)
                }
            }
        }
    }

    override fun retryRequest() {
        //点击屏幕重试
        mViewModel.showToast() //一次性消费
        mViewModel.loadBannerData()
        mViewModel.loadDetailData()
    }

    override fun getStatusOwnerView(): View? {
        return mContentView
    }
}