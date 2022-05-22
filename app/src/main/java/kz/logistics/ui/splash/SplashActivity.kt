package kz.logistics.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kz.logistics.ui.login.LoginActivity
import kz.logistics.ui.navigation.NavigationActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(2000)

        if (firebaseAuth.currentUser != null)
            startActivity(Intent(this, NavigationActivity::class.java))
        else
            startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}