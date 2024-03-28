package studykotlin.dongnao.api

import retrofit2.http.GET
import retrofit2.http.Query
import studykotlin.dongnao.entity.ArticleEntity

interface ArticleApi {
    @GET("article")
    suspend fun searchArticle(@Query("key") key: String): List<ArticleEntity>
}