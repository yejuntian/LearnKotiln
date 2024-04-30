package com.study.mvi

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.study.mvi.exetends.debugCheckImmediateMainDispatcher
import com.study.mvi.exetends.flowWithLifecycle2
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class AbstractMviActivity<
        I : MviIntent,
        S : MviViewState,
        E : MviSingleEvent,
        vm : MviViewModel<I, S, E>>
    (@LayoutRes contentLayoutId: Int) : AppCompatActivity(), MviView<I, S, E> {

    protected abstract val viewModel: vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        bindVM()
    }

    private fun bindVM() {
        // observe viewModel
        viewModel.viewState.flowWithLifecycle2(this) {
            render(it)
        }

        //observe single event
        viewModel.singleEvent.flowWithLifecycle2(this) {
            debugCheckImmediateMainDispatcher()
            handleSingleEvent(it)
        }

        //发送intent到viewModel
        viewIntents().onEach {
            viewModel.processIntent(it)
        }.launchIn(lifecycleScope)
    }

    abstract fun setupViews()

}