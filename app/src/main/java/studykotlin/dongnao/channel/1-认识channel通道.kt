package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 认识channel
 * channel实际上是一个'并发安全的队列'，它可以连接协程，实现不同协程的通信。
 *
 * 实现简单协程通信案例：
 * @see knowChannel
 */

fun main() {
    println("************ 认识通道案例：生产消费 ***************")
    knowChannel()
}


fun knowChannel() = runBlocking {
    val channel = Channel<Int>()

    //生产者
    val product = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            channel.send(++i)
            println("send $i")
        }
    }

    //消费者
    val consumer = GlobalScope.launch {
        while (true) {
            val receive = channel.receive()
            println("receive $receive")
        }
    }

    joinAll(product, consumer)
}