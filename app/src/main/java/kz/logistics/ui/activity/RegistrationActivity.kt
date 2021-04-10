package kz.logistics.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kz.logistics.BaseActivity
import kz.logistics.R
import kz.logistics.util.Util.hideKeyboard
import kz.logistics.util.Util.showToast
import kz.logistics.util.Util.viewBinding
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
                val email = inputEmail.text
                val password = passwordInputEditText.text.toString()
                val confirmPassword = confirmPasswordInputEditText.text.toString()

                registrationButton.isEnabled = email.isNotEmpty() &&
                        password.isNotEmpty() &&
                        confirmPassword.isNotEmpty() &&
                        password.length >= 6 &&
                        confirmPassword.length >= 6 &&
                        password == confirmPassword

                if (password != confirmPassword) {
                    isNotSameText.visibility = View.VISIBLE
                    passwordInputEditText.setBackgroundResource(R.drawable.bg_input_error_edit_text)
                    confirmPasswordInputEditText.setBackgroundResource(R.drawable.bg_input_error_edit_text)
                } else {
                    isNotSameText.visibility = View.INVISIBLE
                    passwordInputEditText.setBackgroundResource(R.drawable.bg_input_edit_text)
                    confirmPasswordInputEditText.setBackgroundResource(R.drawable.bg_input_edit_text)
                }
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