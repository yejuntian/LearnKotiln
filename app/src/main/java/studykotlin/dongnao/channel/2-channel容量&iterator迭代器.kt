package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * channel容量
 *
 * channel是一个队列，队列存在缓冲区，一旦缓冲区满了，并且没人调用receive()函数取走，
 * send就需要挂起。故意让接收端放慢节奏，发现send发送端总是被挂起，直到调用receive接收端接收之后，
 * 才会往下执行。
 */

fun main() {
    println("************ 通道案例：生产消费者，故意放慢receive消费端节奏 ***************")
    //knowChannel2()

    println("************ 通过channel迭代器遍历 ***************")
    iteratorChannel()

    println("************ 通过channel迭代器遍历，通过for in 形式 ***************")
    iteratorChannel2()
}


fun knowChannel2() = runBlocking {
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
            delay(2000)
            val receive = channel.receive()
            println("receive $receive")
        }
    }
    joinAll(product, consumer)
}

fun iteratorChannel() = runBlocking {
    var channel = Channel<Int>(Channel.UNLIMITED)

    val product = GlobalScope.launch {
        for (i in 1..5) {
            channel.send(i * i)
            println("Send ${i * i}")
        }
    }

    val consume = GlobalScope.launch {
        val iterator = channel.iterator()
        while (iterator.hasNext()) {
            println("receive = ${iterator.next()}")
        }
    }
    joinAll(product, consume)
}

fun iteratorChannel2() = runBlocking {
    var channel = Channel<Int>(Channel.UNLIMITED)

    val product = GlobalScope.launch {
        for (i in 1..5) {
            channel.send(i * i)
            println("Send ${i * i}")
        }
    }

    val consume = GlobalScope.launch {
        for (element in channel) {
            println("receive = $element")
        }
    }
    joinAll(product, consume)
}

