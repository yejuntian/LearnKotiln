package studykotlin.dongnao

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * Android中全局异常处理
 *
 * 1.全局异常处理可以获取到所有协程未处理异常，虽然‘不能阻止应用程序崩溃’，
 * 全局异常在程序调试和异常上报场景仍然有很大作用。
 * 2.需要在classpath下面创建META-INF/services目录，并在其中创建名为
 * kotlinx.coroutines.CoroutineExceptionHandler文件，文件内容就是我们
 * 全局异常处理器的全类名。
 *
 */

open class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("exception", "unHandled exception $exception")
    }

}