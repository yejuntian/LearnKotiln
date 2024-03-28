package studykotlin.dongnao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import studykotlin.dongnao.entity.ArticleEntity
import studykotlin.dongnao.net.RetrofitClient

class ArticleViewModel(app: Application) : AndroidViewModel(app) {
    //为了解决Flow嵌套问题，我们使用livedata解决嵌套问题
    val article = MutableLiveData<List<ArticleEntity>>()

    fun searchArticles(key: String) = viewModelScope.launch {
        flow {
            val list = RetrofitClient.articleApi.searchArticle(key)
            emit(list)
        }.flowOn(Dispatchers.IO)
            .catch {
                it.printStackTrace()
            }
            .collect { result: List<ArticleEntity> ->
                article.value = result
            }
    }
}