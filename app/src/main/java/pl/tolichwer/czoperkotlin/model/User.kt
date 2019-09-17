package pl.tolichwer.czoperkotlin.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @NonNull
    val userID: Int,
    val name: String,
    val login: String,
    val password: String
)