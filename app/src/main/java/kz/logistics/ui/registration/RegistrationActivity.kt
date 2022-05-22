package kz.logistics.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import kz.logistics.R
import kz.logistics.databinding.RegistrationPageBinding
import kz.logistics.ui.navigation.NavigationActivity
import kz.logistics.util.Util.hideKeyboard
import kz.logistics.util.Util.showToast
import kz.logistics.util.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity(R.layout.registration_page) {

    private val binding by viewBinding(RegistrationPageBinding::bind)
    private val viewModel by viewModel<RegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectViewModel()
        setupView()
    }

    private fun collectViewModel() = with(binding) {
        collect(viewModel.enabled, registrationButton::setEnabled)
        collect(viewModel.loading, progressLayout::isVisible::set)
        collect(viewModel.isPasswordSame) { isPasswordSame ->
            isNotSameText.isVisible = !isPasswordSame
            if (!isPasswordSame) {
                passwordInputEditText.setBackgroundResource(R.drawable.bg_input_error_edit_text)
                confirmPasswordInputEditText.setBackgroundResource(R.drawable.bg_input_error_edit_text)
            } else {
                passwordInputEditText.setBackgroundResource(R.drawable.bg_input_edit_text)
                confirmPasswordInputEditText.setBackgroundResource(R.drawable.bg_input_edit_text)
            }
        }
        collect(viewModel.registrationEvent) {
            when (it) {
                RegistrationEvent.HideKeyword -> hideKeyboard()
                RegistrationEvent.RegistrationFailure -> showToast("Регистрация невазможна")
                RegistrationEvent.RegistrationSuccess -> {
                    Intent(this@RegistrationActivity, NavigationActivity::class.java)
                        .apply(::startActivity)
                    finish()
                }
            }
        }
    }

    private fun setupView() = with(binding) {
        inputEmail.doAfterTextChanged(viewModel::setEmail)
        passwordInputEditText.doAfterTextChanged(viewModel::setPassword)
        confirmPasswordInputEditText.doAfterTextChanged(viewModel::setPasswordConfirmation)
        registrationButton.setOnClickListener { viewModel.onRegisterClicked() }
    }
}