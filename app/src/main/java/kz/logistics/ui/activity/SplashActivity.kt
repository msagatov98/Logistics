package kz.logistics.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kz.logistics.BaseActivity

class SplashActivity : AppCompatActivity(), BaseActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(2000)

        if (app.mAuth.currentUser != null)
            startActivity(Intent(this, NavigationActivity::class.java))
        else
            startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}