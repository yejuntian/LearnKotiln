package studykotlin.dongnao

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.study.learnkotiln.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * CoroutineScope:它会跟踪所有协程，同样可以取消由它启动的所有协程。
 *
 * 常用的API
 * 1.GlobalScope：
 * 生命周期是process级别的，即使Activity或Fragment已经被销毁，协程仍然在执行。
 *
 * 2.MainScope:
 * 在Activity中使用，可以在onDestroy()中取消协程。
 *
 * 3.viewModelScope:
 * 只能在ViewModel中使用，绑定ViewModel生命周期。
 *
 * 4.lifecycleScope:
 * 只能在Activity、Fragment中使用，会绑定Activity和Fragment的生命周期。
 *
 */

class MainScopeActivity2 : AppCompatActivity(), CoroutineScope by MainScope() {

    private var submitButton: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscope)

        submitButton = findViewById<Button>(R.id.submitButton)

        submitButton?.setOnClickListener {
            launch {
                try {
                    val user = getUser()
                } catch (e: Exception) {
                    //协程取消内部会抛异常：kotlinx.coroutines.JobCancellationException: Job was cancelled; job=SupervisorJobImpl{Cancelling}@df0cd55
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun getUser(): String {
        //模拟请求耗时10s
        delay(10000)
        return "张三"
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}