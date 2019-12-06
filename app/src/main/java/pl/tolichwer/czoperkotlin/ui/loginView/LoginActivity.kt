package pl.tolichwer.czoperkotlin.ui.loginView

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.LoginActivityBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import pl.tolichwer.czoperkotlin.ui.NavigationActivity
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        with(viewModel) {
            //TODO: check if token is available or not
            usersFromDB.observe(this@LoginActivity, Observer {
                if (it.isNotEmpty()) {
                    enterApplication()
                }
            })

            //TODO: get the token instead of users. Users and other initial data should be downloaded after successful login
            users.observe(this@LoginActivity, Observer {
                if (it.isNotEmpty()) {
                    enterApplication()
                }
            })
        }

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.username.setText("Tomek")
        binding.password.setText("tomek")
    }

    private fun attemptLogin() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        binding.username.error = viewModel.validateUsername(username)
        binding.password.error = viewModel.validatePassword(password)

        if (viewModel.areCredentialsValidated()) {
            viewModel.getUsers()
        }
    }

    private fun enterApplication() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}