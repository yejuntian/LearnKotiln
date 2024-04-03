package studykotlin.dongnao.download

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

object DownLoadManager {
    fun downLoad(url: String, file: File): Flow<DownLoadStatus> {
        return flow {
            val request = Request.Builder().url(url).get().build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()
            if (response.isSuccessful) {
                response.body!!.let {
                    val total = it.contentLength()
                    //文件读写操作
                    file.outputStream().use { fileOutputStream ->
                        val inputStream = it.byteStream()
                        // 缓冲区大小为 8KB
                        val buffer = ByteArray(8 * 1024)
                        // 已下载的字节数
                        var emitProgress: Long = 0
                        var bytesCopied: Int

                        //also 是一个标准库扩展函数，它对一个对象执行给定的代码块，并返回该对象本身。
                        // 这个函数通常用于执行某些操作，同时保持链式调用的上下文。
                        //also { bytesCopied = it } 是一个 also 代码块，它接收 inputStream.read(buffer) 的返回值作为 it（隐式名称的 lambda 参数），
                        // 并将其赋值给 bytesCopied 变量。
                        while (inputStream.read(buffer).also { bytesCopied = it } != -1) {
                            fileOutputStream.write(buffer, 0, bytesCopied)
                            emitProgress += bytesCopied
                            // 计算百分比
                            val progress = (emitProgress * 100 / total).toInt()
                            println("Download progress: $progress%")
                            // 发送进度更新事件
                            emit(DownLoadStatus.Progress(progress))
                        }
                        // 刷新输出流以确保所有数据都被写入文件
                        fileOutputStream.flush()
                    }
                }
                emit(DownLoadStatus.Done(file))
            } else {
                throw IOException("IO异常: $response")
            }
        }   //捕获flow上游异常
            .catch {
                file.delete()
                emit(DownLoadStatus.Error(it))
            }
            //IO操作
            .flowOn(Dispatchers.IO)
    }
}