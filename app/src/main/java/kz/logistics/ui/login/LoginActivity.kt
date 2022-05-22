package kz.logistics.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import kz.logistics.R
import kz.logistics.databinding.LoginPageBinding
import kz.logistics.ui.navigation.NavigationActivity
import kz.logistics.ui.registration.RegistrationActivity
import kz.logistics.util.Util.showToast
import kz.logistics.util.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(R.layout.login_page) {

    private val binding by viewBinding(LoginPageBinding::bind)
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectViewModel()
        setupView()
    }

    private fun collectViewModel() = with(binding) {
        collect(viewModel.enabled, loginButton::setEnabled)
        collect(viewModel.loading, progressLayout::isVisible::set)
        collect(viewModel.uiEvent) {
            when (it) {
                LoginEvent.LoginSuccess -> {
                    startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
                    finish()
                }
                LoginEvent.LoginFailure -> showToast("Неверный логин или пароль")
            }
        }
    }

    private fun setupView() = with(binding) {
        inputEmail.doAfterTextChanged(viewModel::setEmail)
        passwordInputEditText.doAfterTextChanged(viewModel::setPassword)
        loginButton.setOnClickListener { viewModel.onLoginClicked() }
        registrationText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }
}