package studykotlin.dongnao.channel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *
 * 使用Flow实现多路复用
 * 多数情况下，我们可以通过构造合适的Flow实现多路复用的效果。
 *
 * 已经定义了两个 GlobalScope 扩展函数。但是，由于 getUserNameFromLocal 和 getUserNameFromServer 是扩展函数，
 * 它们不属于任何对象实例，因此不能直接使用对象函数引用的语法 ::。对象函数引用通常用于类的成员方法引用。
 *
 * 不过，你可以通过定义两个 suspend 函数来包装对 async 函数的调用，并在 CoroutineScope 中使用这些 suspend 函数引用。
 *
 * 案例：使用Flow实现多路复用效果,把两个操作合并到末端操作符上，但是和Select是有区别的
 * @see testFlowSelect
 *
 */

fun main() {
    println("****** 使用Flow实现多路复用效果,把两个操作合并到末端操作符上，但是和Select是有区别的 ******")
    testFlowSelect()

}

//声明为GlobalScope拓展函数
fun GlobalScope.getUserNameFromLocal(name: String) = async(Dispatchers.IO) {
    delay(1500)
    "zhangSan"
}

//声明为GlobalScope拓展函数
fun GlobalScope.getUserNameFromServer(name: String) = async(Dispatchers.IO) {
    delay(1000)
    "liSi"
}


suspend fun getLocalUserName(name: String): Deferred<String> {
    return GlobalScope.getUserNameFromLocal(name)
}

suspend fun getServerUserName(name: String): Deferred<String> {
    return GlobalScope.getUserNameFromServer(name)
}

/**
 * 2个函数对应 2个Flow 最后将2个Flow进行合并
 */
fun testFlowSelect() = runBlocking {
    // 2个函数-->协程async deferred-->2个Flow-->将2个Flow合并
    val name = "jack"

    val job = CoroutineScope(Dispatchers.Default).launch {
        val functionList = listOf(::getLocalUserName, ::getServerUserName)
        functionList.map { function ->
            function.invoke(name)
        }
            .map { deferred ->
                flow { emit(deferred.await()) }
            }
            //多个flow合并成一个flow
            .merge()
            .collect {
                println(it)
            }
    }
    job.join()
}




