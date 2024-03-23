package studykotlin.dongnao

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import studykotlin.dongnao.api.UserServiceApi

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://your.api.base.url/") // 替换为你的 API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userServiceApi: UserServiceApi = retrofit.create(UserServiceApi::class.java)
}