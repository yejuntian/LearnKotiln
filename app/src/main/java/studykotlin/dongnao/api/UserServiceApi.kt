package studykotlin.dongnao.api

import retrofit2.http.GET
import retrofit2.http.Path

interface UserServiceApi {
    @GET("users/{name}")
    suspend fun getUser(@Path("name") name: String): User
}