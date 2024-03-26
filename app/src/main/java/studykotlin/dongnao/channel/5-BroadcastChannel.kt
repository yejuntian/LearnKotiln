package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * BroadcastChannel
 *
 * 前面提到，发送端和接收端Channel存在一对多情形，从数据处理本身来讲，
 * 虽然有多个接收端，但是同一个元素只会被一个接收端读到。
 * 广播则不然，多个接收端不存在互斥行为'。
 *
 */

fun main() {
    println("************ 创建BroadcastChannel->方式1 ***************")
    testBroadcastChannel()

    println("************ 创建BroadcastChannel->方式2 ***************")
    testBroadcastChannel2()
}

@OptIn(ObsoleteCoroutinesApi::class)
fun testBroadcastChannel() = runBlocking {
    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    val producer = GlobalScope.launch {
        List(3) {
            delay(100)
            broadcastChannel.send(it)
        }
        //关闭发送
        broadcastChannel.close()
    }

    // 用于保存每个协程的 Job
    val jobList = mutableListOf<Job>()
    for (i in 0..3) {
        val receive = GlobalScope.launch {
            //订阅广播
            val receiveChannel = broadcastChannel.openSubscription()
            for (element in receiveChannel) {
                println("#[$i] receive $element")
            }
        }
        jobList.add(receive)
    }
    // 等待所有协程完成
    jobList.joinAll()
}

@OptIn(ObsoleteCoroutinesApi::class)
fun testBroadcastChannel2() = runBlocking {
    //val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    val channel = Channel<Int>()
    val broadcastChannel = channel.broadcast(3)
    val producer = GlobalScope.launch {
        List(3) {
            delay(100)
            broadcastChannel.send(it)
        }
        //关闭发送
        broadcastChannel.close()
    }

    // 用于保存每个协程的 Job
    val jobList = mutableListOf<Job>()
    for (i in 0..3) {
        val receive = GlobalScope.launch {
            //订阅广播
            val receiveChannel = broadcastChannel.openSubscription()
            for (element in receiveChannel) {
                println("#[$i] receive $element")
            }
        }
        jobList.add(receive)
    }
    // 等待所有协程完成
    jobList.joinAll()
}



