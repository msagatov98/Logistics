package kz.logistics

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kz.logistics.di.logisticsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(modules = logisticsModule)
        }
    }
}