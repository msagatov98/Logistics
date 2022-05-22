package kz.logistics.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val userEmail = MutableStateFlow(firebaseAuth.currentUser?.email.orEmpty())

    fun onExitClicked() {
        firebaseAuth.signOut()
    }
}