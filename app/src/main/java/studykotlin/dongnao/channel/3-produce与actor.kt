package studykotlin.dongnao.channel

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * produce()与actor()
 *
 * 1.构造生产者与消费者的'便捷方法'。
 * 2.我们可以通过produce()方法启动一个生产者协程，并返回一个ReceiverChannel，
 * 其他协程就可以使用这个channel接收数据了。反过来，我们可以使用actor启动一个消费者协程。
 *
 */

fun main() {
    println("************ 使用produce()快速构建一个生产者协程 ***************")
    fastProduceChannel()

    println("************ 使用actor()快速构建一个消费协程 ***************")
    fastActorChannel()
}

fun fastProduceChannel() = runBlocking {
    val receiveChannel: ReceiveChannel<Int> = produce {
        // 每隔100毫秒，执行10次发送数据
        repeat(6) {
            delay(100)
            send(it)
        }
    }
    //开始消费数据
    for (i in receiveChannel) {
        println("Received $i")
    }
}


fun fastActorChannel() = runBlocking {
    val produceChannel: SendChannel<Int> = actor<Int> {
        val iterator = iterator()
        while (iterator.hasNext()) {
            println(iterator.next())
        }
    }

    (1..5).forEach {
        delay(100)
        produceChannel.send(it)
    }
}




