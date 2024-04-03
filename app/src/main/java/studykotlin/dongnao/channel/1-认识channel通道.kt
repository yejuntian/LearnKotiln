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
 * channel特点
 * 1.每个消息只有一个订阅者可以收到。
 * 解释：在 Channel 中，每个发送的消息只能被一个订阅者（消费者）接收。
 * 这意味着每个消息只会被 Channel 中的一个消费者消费，避免了消息被多个消费者同时处理的情况。
 *
 * 2.第一个订阅者可以收到 collect 之前的事件:
 * 解释：在 Channel 中，第一个订阅者（消费者）可以收到在其开始收集之前发送到 Channel 的事件。
 * 这意味着即使第一个订阅者在 Channel 中启动之前，它也可以收到之前发送的事件
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