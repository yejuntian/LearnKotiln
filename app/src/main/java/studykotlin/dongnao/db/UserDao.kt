package studykotlin.dongnao.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import studykotlin.dongnao.api.User

@Dao
interface UserDao {
    //定义冲突策略
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    //Flow 不需要加suspend关键词，编译的时候默认加上协程作用域
    @Query("select * from UserEntity")
    fun getAllUser(): Flow<List<UserEntity>>
}