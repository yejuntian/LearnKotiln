package studykotlin.dongnao.repository

import studykotlin.dongnao.net.RetrofitClient.userServiceApi
import studykotlin.dongnao.api.User

class UserRepository {
    suspend fun getUser(name: String): User {
        return userServiceApi.getUser(name)
    }
}