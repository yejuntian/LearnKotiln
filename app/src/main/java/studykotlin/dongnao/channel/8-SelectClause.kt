package studykotlin.dongnao.channel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 *
 * SelectClause
 *
 * (1)、我们怎么知道哪些事件可以被select呢？其实能够被select事件的都是SelectClauseN类型。
 *
 * 包括：
 * 1.SelectClause0:对应事件没有返回值，例如join没有返回值，那么onJoin就是SelectClauseN类型。
 * 使用时onJoin的参数是一个无参函数。
 *
 * 2.SelectClause1:对应事件有返回值，前面的onAwait和onReceive都是类似情况。
 *
 * 3.SelectClause2：对应事件有返回值，此外还需要一个额外的参数，例如Channel.onSend有两个参数，
 * 第一个是Channel数据类型的值，表示即将发送的数值；第二个是发送成功的回调函数函数。
 *
 * (2)、如果我们想知道挂起函数是否支持select，只需要查看其'是否存在对应的SelectClauseN类型'可回调即可。
 *
 */

fun main() {
    println("************ SelectClause0没有返回值案例 ***************")
    testSelectClause0()

    println("************ SelectClause2有返回值并需要额外参数案例 ***************")
    testSelectClause2()
}

fun testSelectClause0() = runBlocking {
    //延时100ms执行
    val job1 = GlobalScope.launch {
        delay(100)
        println("Job1 execute")
    }

    //延时50ms执行
    val job2 = GlobalScope.launch {
        delay(50)
        println("Job2 execute")
    }

    select<Unit> {
        job1.onJoin {
            println("job1 onJoin")
        }

        job2.onJoin {
            println("job2 onJoin")
        }
    }
    joinAll(job1, job2)
}


fun testSelectClause2() = runBlocking<Unit> {
    val channels = arrayListOf(Channel<Int>(), Channel<Int>())
    println(channels)

    launch(Dispatchers.IO) {
        select<Unit?> {
            channels[1].onSend(200) { sentChannel ->
                println("sent on $sentChannel")
            }
            channels[0].onSend(100) { sentChannel ->
                println("sent on $sentChannel")
            }
        }
    }
    // 接收首先接收数据的通道的数据
    GlobalScope.launch {
        channels[0].receive()
    }

    GlobalScope.launch {
        channels[1].receive()
    }
    // 等待足够的时间以确保消息被发送和接收
    delay(1000)
    // 关闭所有通道
    channels.forEach {
        it.close()
    }
}


