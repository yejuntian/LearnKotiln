package studykotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * suspend关键词作用：使用suspend关键字修饰的函数叫做挂起函数。
 * 挂起函数：只能在"协程代码块"或"其他挂起函数"中调用
 *
 * inline关键词作用：把调用inline代码的这一块，直接替换成其代码内容。
 * inline函数：适合具有函数类型的参数
 */
fun main() {
    runBlocking {
        val test = testSuspend()
        println (test)
        println("********测试inline函数*********")
        println(testInline())
    }
}


suspend fun testSuspend(): Int {
    delay(2000)
    return 1
}

inline fun testInline(): String {
    println("inline--test")
    return "inline"
}

