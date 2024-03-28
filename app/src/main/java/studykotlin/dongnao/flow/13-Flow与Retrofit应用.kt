package studykotlin.dongnao.flow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.study.learnkotiln.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import studykotlin.dongnao.viewmodel.ArticleViewModel

/**
 * 使用Flow与Retrofit应用
 */

class ArticleActivity : AppCompatActivity() {
    private var mEditSearch: EditText? = null

    //使用viewModel代理 创建对象
    private val articleViewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_retrofit)
        initView()
        initListener()
        initData()
    }

    private fun initData() {
        articleViewModel.article.observe(this) { articles ->
            println(articles)
            //TODO 返回结果，显示UI逻辑未实现
        }
    }

    private fun initView() {
        mEditSearch = findViewById<EditText>(R.id.edtSearch)
    }

    private fun initListener() {
        lifecycleScope.launch {
            mEditSearch?.textWatchFlow()
                ?.onEach { text ->
                    // 处理每次文本变化
                    println("Text changed: $text")
                    articleViewModel.searchArticles(text)
                }
                // launchIn替换collect，使用合适的作用域启动流收集
                ?.launchIn(lifecycleScope)
        }
    }

    /**
     * callbackFlow 构建器用于创建一个 Flow，我们在其中定义了一个 TextWatcher 并将其添加到 TextView 上。
     * 在 TextWatcher 的 afterTextChanged 方法中，我们使用 trySend 将变化后的文本发送到流中。
     *
     * awaitClose 被调用以确保当流的收集被取消时，我们可以清理资源，
     * 也就是移除 TextWatcher。
     */
    private fun TextView.textWatchFlow() = callbackFlow<String> {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // 当文本变化时，将新的文本发送到流中
                trySend(s.toString())
            }
        }
        addTextChangedListener(textWatcher)
        //当流收集取消时，移除文本变化监听
        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }
}


