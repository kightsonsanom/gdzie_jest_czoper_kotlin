package pl.tolichwer.czoperkotlin.ui.loginView

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.api.ApiResource
import pl.tolichwer.czoperkotlin.databinding.LoginActivityBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import pl.tolichwer.czoperkotlin.ui.NavigationActivity
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var viewModel: LoginActivityViewModel
    private var loginDisposables = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginActivityViewModel::class.java)
        loginDisposables.add(viewModel.isUserLoggedIn()
            .subscribe {
                if (it.isNotEmpty()) {
                    enterApplication()
                }
            })

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        binding.username.error = viewModel.validateUsername(username)
        binding.password.error = viewModel.validatePassword(password)

        if (viewModel.areCredentialsValidated()) {
            getUsers()
        }
    }

    private fun getUsers() {
        loginDisposables.add(viewModel.getUsers()
            .subscribe {
                when (it) {
                    is ApiResource.Success -> {
                        enterApplication()
                    }
                    is ApiResource.Failure -> {
                        Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun enterApplication() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginDisposables.dispose()
    }
}