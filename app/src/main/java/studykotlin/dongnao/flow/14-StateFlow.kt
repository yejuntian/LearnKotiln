package studykotlin.dongnao.flow

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.study.learnkotiln.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import studykotlin.dongnao.viewmodel.NumberViewModel

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
 */


class NumberStateFlowActivity : AppCompatActivity() {
    private var mEditResult: TextView? = null
    private var mBtnPlus: Button? = null
    private var mBtnSub: Button? = null
    private val numberViewModel by viewModels<NumberViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stateflow)

        initView()
        initListener()
        // 监听 StateFlow 的数据变化并更新 EditText
        numberViewModel.numberStateFlow.onEach { value: Int ->
            mEditResult?.text = "$value"
        }.launchIn(lifecycleScope)
    }

    private fun initView() {
        mEditResult = findViewById<TextView>(R.id.editNumber)
        mBtnPlus = findViewById<Button>(R.id.btn_plus)
        mBtnSub = findViewById<Button>(R.id.btn_sub)
    }

    private fun initListener() {
        mBtnPlus?.setOnClickListener {
            numberViewModel.increment()
        }

        mBtnSub?.setOnClickListener {
            numberViewModel.decrement()
        }
    }
}