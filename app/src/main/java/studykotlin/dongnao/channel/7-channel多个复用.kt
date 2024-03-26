package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 *
 * 复用多个Channel
 * 跟 onAwait()类似，会收到相应最快的那个channel消息。
 *
 */

fun main() {
    println("************ 复用多个Channel,得到相应最快的那个Channel ***************")
    testSelectChannel()
}

fun testSelectChannel() = runBlocking {
    val channelList = listOf<Channel<String>>(Channel<String>(), Channel<String>())

    val job = GlobalScope.launch {
        delay(100)
        channelList[0].send("每隔100ms发送消息")
    }

    val job2 = GlobalScope.launch {
        delay(50)
        channelList[1].send("每隔50ms发送消息")
    }

    val result = select<String?> {
        channelList.forEach { channel ->
            channel.onReceive {
                it
            }
        }
    }
    println(result)
}

