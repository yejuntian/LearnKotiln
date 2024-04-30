package com.study.jetpack.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.study.jetpack.mvi.util.WeakReferenceHandler
import org.ninetripods.mq.study.jetpack.base.widget.StatusViewOwner

/**
 * Mvi基类
 */
abstract class BaseMviActivity : AppCompatActivity() {
    protected lateinit var mStatusViewUtil: StatusViewOwner
    val weakReferenceHandler = WeakReferenceHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayoutId() != 0) {
            //暂时给MVVM架构使用的
            setContentView(getLayoutId())
        } else {
            setContentView()
        }
        mStatusViewUtil = StatusViewOwner(this, getStatusOwnerView()) {
            retryRequest()
        }
        initView()
        initEvents()
    }

    open fun setContentView() {}

    @LayoutRes
    open fun getLayoutId(): Int {
        return 0
    }

    abstract fun initEvents()

    abstract fun initView()

    /**
     * 重新请求
     */
    open fun retryRequest() {}

    /**
     * 展示Loading、Empty、Error视图等
     */
    open fun getStatusOwnerView(): View? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        weakReferenceHandler.clearListeners()
    }
}