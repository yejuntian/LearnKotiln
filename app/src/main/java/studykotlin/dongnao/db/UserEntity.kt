package studykotlin.dongnao.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserEntity(
    @PrimaryKey
    val userId: Int?,
    @ColumnInfo(name = "firstName")
    val firstName: String?,
    @ColumnInfo(name = "lastName")
    val lastName: String?
)