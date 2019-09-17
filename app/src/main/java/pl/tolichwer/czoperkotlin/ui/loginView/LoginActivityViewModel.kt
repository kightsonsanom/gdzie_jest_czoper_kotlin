package pl.tolichwer.czoperkotlin.ui.loginView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Flowable
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.api.ApiResource
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.User
import javax.inject.Inject

class LoginActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val app: Application
) : AndroidViewModel(app) {

    private var credentialsValidated = false
    private lateinit var username: String
    private lateinit var password: String

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

    fun getUsers(): Flowable<ApiResource<List<User>>> {

        return repository.getUsers(username, password)
    }

    fun isUserLoggedIn(): Flowable<List<User>> {

        return repository.getUsersFromDB()
    }

    fun areCredentialsValidated(): Boolean {
        return credentialsValidated
    }
}

