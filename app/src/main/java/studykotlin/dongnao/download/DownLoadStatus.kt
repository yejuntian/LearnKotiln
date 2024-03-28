package studykotlin.dongnao.download

import java.io.File

/**
 * DownLoadStatus:下载状态
 *
 * sealed： 里面的成员都是它的子类
 */
sealed class DownLoadStatus {
    //下载进度
    data class Progress(val value: Int) : DownLoadStatus()

    //下载失败
    data class Error(val throwable: Throwable) : DownLoadStatus()

    //下载完成
    data class Done(val file: File) : DownLoadStatus()

    //什么也没有
    object None : DownLoadStatus()
}
