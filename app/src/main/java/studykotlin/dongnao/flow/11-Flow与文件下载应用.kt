package studykotlin.dongnao.flow

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.study.learnkotiln.R
import kotlinx.coroutines.launch
import studykotlin.dongnao.download.DownLoadManager
import studykotlin.dongnao.download.DownLoadStatus
import java.io.File

/**
 * 使用Flow完成文件下载案例
 */

class DownLoadActivity : AppCompatActivity() {
    private var mProgressBar: ProgressBar? = null
    private var mTxtProgress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_download)
        mProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        mTxtProgress = findViewById<TextView>(R.id.tv_progress)

        lifecycleScope.launch {
            val downLoadUrl = "https://apk.obwhatsappdownload.com/OBWhatsApp.apk"
            val file = getExternalFilesDir(null)?.path?.let { File(it, "myApp.apk") }
            val downLoadFlow = file?.let { DownLoadManager.downLoad(downLoadUrl, it) }
            downLoadFlow?.collect { status ->
                when (status) {
                    is DownLoadStatus.Error -> {
                        Toast.makeText(this@DownLoadActivity, "下载错误", Toast.LENGTH_SHORT).show()
                    }

                    is DownLoadStatus.Progress -> {
                        val progress = status.value
                        mProgressBar?.progress = progress
                        mTxtProgress?.text = "${progress}%"
                    }

                    is DownLoadStatus.Done -> {
                        mProgressBar?.progress = 100
                        mTxtProgress?.text = "100%"
                        Toast.makeText(this@DownLoadActivity, "下载完成", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Log.d("DownLoadActivity", "下载失败")
                    }
                }
                println()
            }
        }
    }

}


