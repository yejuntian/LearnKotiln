package studykotlin.dongnao.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Generic inline classes is an Experimental feature.
// It may be dropped or changed at any time.
// Opt-in is required with the -language-version 1.8 compiler option.
// See https://kotlinlang.org/docs/inline-classes.html for more information.
@JvmInline
value class SelfReference<T>(
    val value: T,
) : ReadOnlyProperty<Any?, T> {
    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = value
}

/**
 * 允许引用对象本身的委托。这对于避免初始化顺序问题很有用。
 *
 * 这是一种替代方法
 *  - `lateinit var` (mutable variables can be modified by mistake).
 *  - [lazy] (lazy evaluation is unnecessary in this case).
 *
 * 注意：不要在 [initializer] 内同步访问返回委托的值。
 * Eg: `val x: Int by selfReferenced { x + 1 }` 是错误的用法，会引发异常.
 *
 * ### Example
 * 下面是如何使用它的示例:
 *
 * ```kotlin
 * import kotlinx.coroutines.flow.*
 * import kotlinx.coroutines.*
 *
 * class Demo {
 *   private val trigger = MutableSharedFlow<Unit>()
 *   private val scope = CoroutineScope(Dispatchers.Default)
 *
 *   val intStateFlow: StateFlow<Int?> by selfReferenced {
 *     merge(
 *       flow {
 *        var c = 0
 *        while (true) { emit(c++); delay(300) }
 *       },
 *       trigger.mapNotNull {
 *         println("access to $intStateFlow")
 *         intStateFlow.value?.minus(1)
 *       }
 *     )
 *     .stateIn(scope, SharingStarted.Eagerly, null)
 *   }
 *
 *   fun trigger() = scope.launch { trigger.emit(Unit) }
 * }
 *
 * fun main(): Unit = runBlocking {
 *   val demo = Demo()
 *   val job = demo.intStateFlow
 *      .onEach(::println)
 *      .launchIn(this)
 *
 *   delay(1_000)
 *   demo.trigger()
 *
 *   delay(500)
 *   demo.trigger()
 *
 *   delay(500)
 *   job.cancel()
 *
 *   // null
 *   // 0
 *   // 1
 *   // 2
 *   // 3
 *   // access to kotlinx.coroutines.flow.ReadonlyStateFlow@2cfeac11
 *   // 2
 *   // 4
 *   // access to kotlinx.coroutines.flow.ReadonlyStateFlow@2cfeac11
 *   // 3
 *   // 5
 *   // 6
 * }
 * ```
 */
fun <T> selfReferenced(initializer: () -> T) = SelfReference(initializer())

class Demo {
    private val trigger = MutableSharedFlow<Unit>()
    private val scope = CoroutineScope(Dispatchers.Default)

    val intStateFlow: StateFlow<Int?> by selfReferenced {
        merge(
            flow {
                var c = 0
                while (true) {
                    emit(c++);
                    delay(300)
                }
            },
            trigger.map {
                println("access to ${intStateFlow.value}")
//                println("access value ${intStateFlow.value}")
//                intStateFlow.value?.minus(1)
                intStateFlow.value
            }
        )
            .stateIn(scope, SharingStarted.Lazily, null)
    }



    fun trigger() = scope.launch { trigger.emit(Unit) }
}

fun main() {
    runBlocking {
        val demo = Demo()
        val job = demo.intStateFlow
            .onEach(::println)
            .launchIn(this)

        delay(1000)
        demo.trigger()

        delay(500)
        demo.trigger()

        delay(500)
        job.cancel()
    }
}




