package com.study.mvi

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.jetpack.mvi.exetends.debugCheckImmediateMainDispatcher
import com.study.learnkotiln.BuildConfig
import com.study.timber.log.Timber
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import java.util.concurrent.atomic.AtomicInteger

/**
 * 提供了处理意图、发送事件、订阅流等功能，并包含了一些调试日志和异常处理的方法，
 * 以及一些用于功效留和调试日志的扩展函数。
 */
abstract class AbstractMviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> :
    ViewModel(), MviViewModel<I, S, E> {
    protected open val rawLogTag: String? = null

    private companion object {
        /**
         * 将由 [kotlinx.coroutines.flow.MutableSharedFlow] 分配的缓冲区大小。
         * 如果超过 64 个状态更新，它将开始暂停.
         * 消费者消费慢,应考虑使用 stateFlow.buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST)。.
         *
         * 内部分配的缓冲区是 replay + extraBufferCapacity，但始终分配 2^n 空间。 * 我们使用 replay=0，因此缓冲区为 64
         */
        private const val SubscriberBufferSize = 64

        private const val MAX_TAG_LENGTH = 23
    }

    protected val logTag by lazy(LazyThreadSafetyMode.PUBLICATION) {
        // Tag length limit was removed in API 26
        (rawLogTag ?: this::class.java.simpleName).let { tag: String ->
            if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
                tag
            } else {
                tag.take(MAX_TAG_LENGTH)
            }
        }
    }

    private val eventChannel = Channel<E>(Channel.UNLIMITED)
    private val intentMutableFlow = MutableSharedFlow<I>(extraBufferCapacity = SubscriberBufferSize)
    protected val intentSharedFlow: SharedFlow<I> get() = intentSharedFlow

    override val singleEvent: Flow<E>
        get() = eventChannel.receiveAsFlow()

    override suspend fun processIntent(intent: I) {
        intentMutableFlow.emit(intent)
    }

    /**
     *必须在 [kotlinx.coroutines.Dispatchers.Main.immediate] 中调用，否则会抛出异常。
     *
     * 如果要从其他 [kotlinx.coroutines.CoroutineDispatcher] 发送事件，
     * 使用 withContext(Dispatchers.Main.immediate) { sendEvent(event) }。
     */
    protected suspend fun sendEvent(event: E) {
        debugCheckImmediateMainDispatcher()

        eventChannel.trySend(event)
            .onFailure {
                Timber.tag(logTag).e(it, "Failed to send event: $event")
            }.getOrThrow()
    }


    // 使用 viewModelScope 的 Flow 扩展函数.
    protected fun <T> Flow<T>.debugLog(subject: String): Flow<T> =
        if (BuildConfig.DEBUG_MODE) {
            onEach {
                Timber.tag(logTag).d(">> $subject: $it")
            }
        } else {
            this
        }

    /**
     * val self = this 是将当前的 SharedFlow<T> 对象赋值给变量 self。
     * 接着使用 object : SharedFlow<T> by self 创建了一个匿名内部类，该内部类实现了 SharedFlow<T> 接口，
     * 并且使用 by self 关键字表示委托给 self 对象，也就是说内部类会继承 self 对象的所有属性和方法
     */
    protected fun <T> SharedFlow<T>.debugLog(subject: String): SharedFlow<T> =
        if (BuildConfig.DEBUG_MODE) {
            val self = this
            object : SharedFlow<T> by self {
                val subscriberCount = AtomicInteger(0)
                override suspend fun collect(collector: FlowCollector<T>): Nothing {
                    val count = subscriberCount.getAndIncrement()
                    self.collect {
                        Timber.tag(logTag).d(">>> $subject ~ $count: $it")
                        //将收集到的元素传递给外部的 self.collector
                        collector.emit(it)
                    }
                }
            }
        } else {
            this
        }

    /**
     * 在 [viewModelScope] 中共享流，当第一个订阅者到达时开始，并在最后一个订阅者离开时停止;
     * 当所有订阅者取消订阅时停止共享，避免数据被多次计算或重复发射
     */
    protected fun <T> Flow<T>.shareWhileSubscribed(): SharedFlow<T> =
        shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    //释放资源
    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
        Timber.tag(logTag).d("onCleared")
    }


}