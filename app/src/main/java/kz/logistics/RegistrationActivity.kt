package kz.logistics

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kz.logistics.Util.hideKeyboard
import kz.logistics.Util.showToast
import kz.logistics.Util.viewBinding
import kz.logistics.databinding.RegistrationPageBinding

class RegistrationActivity : AppCompatActivity(), BaseActivity {

    private val binding by viewBinding(RegistrationPageBinding::inflate)

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
            Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
            Unit

        override fun afterTextChanged(s: Editable?) {
            binding.run {
                val isEmailNotEmpty = inputEmail.text.isNotEmpty()
                val isPasswordNotEmpty = passwordInputEditText.text.toString().isNotEmpty()
                val isConfirmPasswordNotEmpty = confirmPasswordInputEditText.text.toString().isNotEmpty()

                registrationButton.isEnabled = isEmailNotEmpty && isPasswordNotEmpty && isConfirmPasswordNotEmpty
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            inputEmail.addTextChangedListener(textWatcher)
            passwordInputEditText.addTextChangedListener(textWatcher)
            confirmPasswordInputEditText.addTextChangedListener(textWatcher)

            registrationButton.setOnClickListener {
                hideKeyboard()
                registrationButton.isEnabled = true
                progressLayout.visibility = View.VISIBLE
                val email = inputEmail.text.toString()
                val password = passwordInputEditText.text.toString()
                app.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressLayout.visibility = View.GONE
                        startActivity(Intent(this@RegistrationActivity, NavigationActivity::class.java))
                        finish()
                    } else {
                        showToast("Регистрация невазможна")
                        registrationButton.isEnabled = true
                        progressLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}