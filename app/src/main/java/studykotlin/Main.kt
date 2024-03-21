package studykotlin

import kotlinx.coroutines.*

fun main(){
    CoroutineScope(Dispatchers.IO).launch {
        println("hello")
    }.start()
    println("hello")
}