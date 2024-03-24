package studykotlin.dongnao

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.study.learnkotiln.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CoroutineExceptionHandlerActivity : AppCompatActivity() {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("catch Exception : $exception")

    }

    @SuppressLint("MissingInflatedId", "LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception_handler)

        findViewById<Button>(R.id.catchButton).also {
            it.setOnClickListener {
                val scope = CoroutineScope(handler)
                scope.launch {
                    //制造一个异常进行抛出
                    Log.e("CoroutineExceptionHandlerActivity", "clicked")
                    "dfafda".substring(20)
                }
            }
        }
    }
}