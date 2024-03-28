package studykotlin.dongnao.flow

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.study.learnkotiln.R
import studykotlin.dongnao.viewmodel.NumberViewModel
import studykotlin.dongnao.viewmodel.SharedFlowViewModel

/**
 * 冷流还是热流
 * 1.Flow是冷流，如果Flow有个订阅者Collector以后，发射出的值才会存在内存中，
 * 这根懒加载很像。
 *
 * 2.StateFlow和SharedFlow是热流，在垃圾回收之前都是存在内存中，并且处于活跃状态。
 *
 * StateFlow
 * StateFlow是一个状态容器'可观察数据流'，可以向当前收集器发出当前状态更新和新状态更新。
 * 还可以通过其value属性读取当前状态值。
 *
 * SharedFlow:->类似BroadCastFlow允许有多个收集者，并可以同时收到数据
 * SharedFlow:会向使用收集值的所有方法发出数据。
 */


class SharedFlowActivity : AppCompatActivity() {
    private var mBtnStart: Button? = null
    private var mBtnEnd: Button? = null
    private val sharedFlowViewModel by viewModels<SharedFlowViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharedflow)

        initView()
        initListener()
    }

    private fun initView() {
        mBtnStart = findViewById<Button>(R.id.btn_start)
        mBtnEnd = findViewById<Button>(R.id.btn_end)
    }

    private fun initListener() {
        mBtnStart?.setOnClickListener {
            sharedFlowViewModel.startRefresh()
        }

        mBtnEnd?.setOnClickListener {
            sharedFlowViewModel.stopRefresh()
        }
    }
}