package com.example.czoperkotlin.ui.loginView

import androidx.lifecycle.ViewModel
import com.example.czoperkotlin.db.Repository
import com.example.czoperkotlin.db.ssot.ApiResource
import com.example.czoperkotlin.model.User
import io.reactivex.Flowable
import javax.inject.Inject

class LoginActivityViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun validateUsername(username: String): Boolean = (username.isEmpty() || username.length < 4)

    fun validatePassword(password: String): Boolean = (password.isEmpty() || password.length < 4)

    fun getUsers(login: String, password: String): Flowable<ApiResource<List<User>>> {

        return repository.getUsers(login, password)
    }

    fun isUserLoggedIn(): Flowable<List<User>>{
        return repository.getUsersFromDB()
    }
}
