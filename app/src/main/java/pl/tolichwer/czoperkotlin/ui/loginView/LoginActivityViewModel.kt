package pl.tolichwer.czoperkotlin.ui.loginView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.Flowable
import kotlinx.coroutines.launch
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.User
import javax.inject.Inject

class LoginActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val app: Application
) : AndroidViewModel(app) {

    private var credentialsValidated = false

    private lateinit var username: String

    private val _usersFromDB = MutableLiveData<List<User>>()
    val usersFromDB: LiveData<List<User>>
        get() = _usersFromDB

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private lateinit var password: String

    init {
        getUsersFromDB()
    }

    fun validateUsername(username: String): String? {
        this.username = username
        return if (username.isEmpty() || username.length < 4) {
            credentialsValidated = false
            app.applicationContext.getString(R.string.username_validation_error)
        } else {
            credentialsValidated = true
            null
        }
    }

    fun validatePassword(password: String): String? {
        this.password = password
        return if (password.isEmpty() || password.length < 4) {
            credentialsValidated = false
            app.applicationContext.getString(R.string.password_validation_error)
        } else {
            credentialsValidated = true
            null
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            _users.value = repository.getUsers(username, password)
        }
    }

    fun getUsersFromDB() {
        viewModelScope.launch {
            _usersFromDB.value = repository.getUsersFromDB()
        }
    }

    fun areCredentialsValidated(): Boolean {
        return credentialsValidated
    }
}

