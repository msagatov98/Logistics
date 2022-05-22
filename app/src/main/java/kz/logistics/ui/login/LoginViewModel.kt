package kz.logistics.ui.login

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LoginViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    val uiEvent = MutableSharedFlow<LoginEvent>()
    val loading = MutableStateFlow(false)
    val enabled = combine(email, password) { email, password ->
        email.isNotEmpty() && password.isNotEmpty()
    }

    fun setEmail(value: Editable?) {
        email.value = value.toString()
    }

    fun setPassword(value: Editable?) {
        password.value = value.toString()
    }

    fun onLoginClicked() {
        loading.value = true
        firebaseAuth.signInWithEmailAndPassword(email.value, password.value).addOnCompleteListener {
            loading.value = false
            if (it.isSuccessful) {
                viewModelScope.launch { uiEvent.emit(LoginEvent.LoginSuccess) }
            } else {
                viewModelScope.launch { uiEvent.emit(LoginEvent.LoginFailure) }
            }
        }
    }

}

sealed interface LoginEvent {
    object LoginFailure : LoginEvent
    object LoginSuccess : LoginEvent
}