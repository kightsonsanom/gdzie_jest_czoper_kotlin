package com.example.czoperkotlin.ui.loginView

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.czoperkotlin.R
import com.example.czoperkotlin.databinding.LoginActivityBinding
import com.example.czoperkotlin.db.ssot.ApiResource
import com.example.czoperkotlin.di.ViewModelFactory
import com.example.czoperkotlin.ui.NavigationActivity
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var viewModel: LoginActivityViewModel
    lateinit var usersDisposable: Disposable

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginActivityViewModel::class.java)
        usersDisposable = viewModel.isUserLoggedIn()
            .subscribe {
                if (it.isNotEmpty()) {
                    enterApplication()
                }
            }

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        binding.loginButton.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        binding.username.error = null
        binding.password.error = null
        var cancelLoginAttempt = false

        if (viewModel.validateUsername(binding.username.text.toString())) {
            binding.username.error = getString(R.string.credentials_input_error)
            cancelLoginAttempt = true
        }

        if (viewModel.validatePassword(binding.password.text.toString())) {
            binding.password.error = getString(R.string.credentials_input_error)
            cancelLoginAttempt = true
        }

        if (!cancelLoginAttempt) {

            usersDisposable = viewModel.getUsers(binding.username.text.toString(), binding.password.text.toString())
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
                }
        }
    }

    private fun enterApplication() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        usersDisposable.dispose()
    }
}