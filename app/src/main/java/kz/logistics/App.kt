package kz.logistics

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class App : Application() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("load")
    }
}