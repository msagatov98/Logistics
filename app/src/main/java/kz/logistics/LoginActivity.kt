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
import kz.logistics.databinding.LoginPageBinding

class LoginActivity : AppCompatActivity(), BaseActivity {

    private val binding by viewBinding(LoginPageBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) {
                binding.run {
                    val email = inputEmail.text.toString().isNotEmpty()
                    val password = passwordInputEditText.text.toString().isNotEmpty()
                    loginButton.isEnabled = email && password
                }
            }

        }

        binding.run {
            inputEmail.addTextChangedListener(textWatcher)
            passwordInputEditText.addTextChangedListener(textWatcher)
            registrationText.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }

            loginButton.setOnClickListener {
                hideKeyboard()
                progressLayout.visibility = View.VISIBLE
                loginButton.isEnabled = false
                val email = inputEmail.text.toString()
                val password = passwordInputEditText.text.toString()
                app.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
                        finish()
                    } else {
                        showToast("Неверный логин или пароль")
                        loginButton.isEnabled = true
                        progressLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}