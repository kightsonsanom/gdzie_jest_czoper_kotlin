package pl.tolichwer.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import pl.tolichwer.czoperkotlin.model.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userList: List<User>)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers (): List<User>
}