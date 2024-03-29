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
 * 2.StateFlow和SharedFlow是热流->在垃圾回收之前都是存在内存中，并且处于活跃状态。
 *
 * StateFlow
 * StateFlow 是 Flow 的一个特殊类型，它是专门设计来保存状态并观察状态变化的。它是热流（hot flow），
 * 意味着它始终有一个当前值，且会在新的观察者开始收集时立即接收最新值。
 *
 * 特点：
 * 1.总是有一个初始值。
 * 2.只会发射新的状态（如果新的值与前一个值相同，则不会发射）。
 * 3.StateFlow 的收集器总是立即接收到最近一次发射的值。
 *
 * 用途：
 * 1.用于观察和响应状态变化，如 UI 状态的更新。
 * 2.适合用于数据绑定，例如将 UI 组件绑定到数据源。
 * 3.通常用于替代 LiveData。
 *
 * SharedFlow
 * SharedFlow 是一个更通用的响应式流类型，它允许配置多种行为，如缓冲、回放和并发更新。
 *
 * 特点：
 * 1.可以配置回放缓冲区（replay cache），它可以保存一定数量的最新值，
 * 并在新的收集器开始收集时重播这些值。
 * 2.可以配置额外的属性，如缓冲大小、回放策略和并发更新策略。
 * 3.发射的值不需要是唯一的，即使相同的值可以连续多次发射。

 * 用途：
 *1. 用于事件的发布和订阅，如用户操作事件、通知等。
 *2.适合需要更复杂配置的场景，如需要保留多个最新值的情况。
 *3.通常用于代替 EventBus 或 BroadcastChannel。
 *
 * 使用场景比较
 * 1.如果你需要表示和观察应用的状态（通常是 UI 状态），
 * 并且只关心最新的状态值，那么 StateFlow 是一个很好的选择。
 * 2.如果你需要处理事件流（如点击事件、网络响应等），
 * 并且可能需要保留和重播多个事件，那么 SharedFlow 更加适合。
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