package com.study.mvi.exetends

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 * LiveData扩展函数封装
 */
fun <T> FragmentActivity.observe(liveData: MutableLiveData<T>, observer: (t: T) -> Unit) {
    liveData.observe(this) { observer(it) }
}

/**
 * repeatOnLifecycle: https://juejin.cn/post/7001371050202103838
 */
@Deprecated("用下面的方法:flowWithLifecycle2()")
inline fun <T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit,
) = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { action(it) }
}

/**
 * 封装Flow.flowWithLifecycle，用于UI层单个Flow去订阅数据时使用
 */
inline fun <T> Flow<T>.flowWithLifecycle2(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(T) -> Unit,
) = lifecycleOwner.lifecycleScope.launch {
    //前后台切换时可以重复订阅数据。如：Lifecycle.State是STARTED，那么在生命周期进入 STARTED 状态时开始任务，在 STOPED 状态时停止订阅
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { block(it) }
    //另外一种写法：
    //    lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
    //        collect {
    //            action(it)
    //        }
    //    }
}

/**
 * MVI模式中使用
 */
inline fun <T, V> Flow<T>.flowWithLifecycle2(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    prop1: KProperty1<T, V>,
    crossinline block: suspend CoroutineScope.(V) -> Unit,
) = lifecycleOwner.lifecycleScope.launch {
    //前后台切换时可以重复订阅数据。如：Lifecycle.State是STARTED，那么在生命周期进入 STARTED 状态时开始任务，在 STOP 状态时停止订阅
    flowOnSingleLifecycle(lifecycleOwner.lifecycle, minActiveState)
        .map { prop1.get(it) }
        .collect { block(it) }
}

inline fun <T> Flow<T>.flowSingleWithLifecycle2(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(T) -> Unit,
) = lifecycleOwner.lifecycleScope.launch {
    flowOnSingleLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { block(it) }
}

/**
 * NOTE: 如果不想对UI层的Lifecycle.repeatOnLifecycle/Flow.flowWithLifecycle在前后台切换时重复订阅，可以使用此方法；
 * 效果类似于Channel，不过Channel是一对一的，而这里是一对多的
 */
fun <T> Flow<T>.flowOnSingleLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isFirstCollect: Boolean = true,
): Flow<T> = callbackFlow {
    var lastValue: T? = null
    lifecycle.repeatOnLifecycle(minActiveState) {
        this@flowOnSingleLifecycle.collect {
            if ((lastValue != null || isFirstCollect) && (lastValue != it)) {
                send(it)
            }
            lastValue = it
        }
    }
    lastValue = null
    close()
}

/**
 * callbackFlow 构建器用于创建一个 Flow，我们在其中定义了一个 TextWatcher 并将其添加到 TextView 上。
 * 在 TextWatcher 的 afterTextChanged 方法中，我们使用 trySend 将变化后的文本发送到流中。
 *
 * awaitClose 被调用以确保当流的收集被取消时，我们可以清理资源，
 * 也就是移除 TextWatcher。
 */
fun TextView.textWatchFlow() = callbackFlow<String> {
    val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // 当文本变化时，将新的文本发送到流中
            trySend(s.toString())
        }
    }
    addTextChangedListener(textWatcher)
    //当流收集取消时，移除文本变化监听
    awaitClose {
        removeTextChangedListener(textWatcher)
    }
}