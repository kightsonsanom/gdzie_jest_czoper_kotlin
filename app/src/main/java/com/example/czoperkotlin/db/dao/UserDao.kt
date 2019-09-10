package com.example.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.czoperkotlin.model.User
import io.reactivex.Flowable

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userList: List<User>)

    @Query("SELECT * FROM user")
    fun getAllUsers (): Flowable<List<User>>
}