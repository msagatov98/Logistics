package kz.logistics.ui.registration

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RegistrationViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val passwordConfirmation = MutableStateFlow("")

    val loading = MutableStateFlow(false)
    val registrationEvent = MutableSharedFlow<RegistrationEvent>()
    val isPasswordSame = combine(password, passwordConfirmation) { password, confirmation ->
        password == confirmation
    }
    val enabled = combine(email, password, passwordConfirmation) { email, password, confirmation ->
        email.isNotEmpty()
                && password.length >= 6
                && confirmation.length >= 6
                && password == confirmation
    }

    fun setEmail(value: Editable?) {
        email.value = value.toString()
    }

    fun setPassword(value: Editable?) {
        password.value = value.toString()
    }

    fun setPasswordConfirmation(value: Editable?) {
        passwordConfirmation.value = value.toString()
    }

    fun onRegisterClicked() = viewModelScope.launch {
        loading.value = true
        registrationEvent.emit(RegistrationEvent.HideKeyword)
        firebaseAuth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener {
                loading.value = false
                viewModelScope.launch {
                    registrationEvent.emit(
                        if (it.isSuccessful) RegistrationEvent.RegistrationSuccess
                        else RegistrationEvent.RegistrationFailure
                    )
                }
            }
    }

}

sealed interface RegistrationEvent {
    object HideKeyword : RegistrationEvent
    object RegistrationSuccess : RegistrationEvent
    object RegistrationFailure : RegistrationEvent
}