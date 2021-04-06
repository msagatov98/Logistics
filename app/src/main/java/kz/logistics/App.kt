package kz.logistics

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class App : Application() {

    lateinit var mAuth : FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
    }
}