package studykotlin.dongnao.repository

import studykotlin.dongnao.RetrofitClient.userServiceApi
import studykotlin.dongnao.api.User

class UserRepository {
    suspend fun getUser(name: String): User {
        return userServiceApi.getUser(name)
    }
}