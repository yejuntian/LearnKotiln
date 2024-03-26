package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Channel的关闭
 *
 * 1.produce和actor返回的Channel都会随着对应协程执行完毕而关闭，正是这样Channel才被称为热数据流。
 *
 * 2.对于一个Channel，如果我们调用了它的close()方法，它会立即停止接收新元素，这时它的isClosedForSend会立即返回true。
 * 由于Channel缓冲区的存在，这时候还有一些元素没有处理完，因此所有的元素都被读取之后isCloseForReceive才会返回true.
 *
 * 3.Channel的生命周期最好是由主导方来维护，建议'主导方一方实现关闭'。
 *
 */

fun main() {
    println("************ Channel的关闭 ***************")
    closeChannel()
}


fun closeChannel() = runBlocking {
    val channel = Channel<Int>(3)

    //生产者
    val producer = GlobalScope.launch {
        List(3) {
            channel.send(it)
            println("sen $it")
        }
        channel.close()
        println("close channel -ClosedForSend: ${channel.isClosedForSend} -ClosedForReceive: ${channel.isClosedForReceive}")
    }

    //消费者
    val consumer = GlobalScope.launch {
        for (element in channel) {
            println("receive $element")
            delay(1000)
        }
        println("After Consuming -ClosedForSend: ${channel.isClosedForSend} -ClosedForReceive: ${channel.isClosedForReceive}")
    }
    joinAll(producer, consumer)
}



