package studykotlin.dongnao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import studykotlin.dongnao.db.AppDataBase
import studykotlin.dongnao.db.UserEntity

/**
 * 处理数据库操作
 */
class UserViewModel(app: Application) : AndroidViewModel(app) {
    fun insertUser(userId: String?, firstName: String?, lastName: String?) {
        viewModelScope.launch {
            AppDataBase.getInstance(getApplication())
                .userDao()
                .insert(UserEntity(userId?.toInt(), firstName, lastName))
        }
    }

    fun getAllUser(): Flow<List<UserEntity>> {
        return AppDataBase.getInstance(getApplication())
            .userDao()
            .getAllUser()
            .catch {
                it.printStackTrace()
            }.flowOn(Dispatchers.IO)
    }
}