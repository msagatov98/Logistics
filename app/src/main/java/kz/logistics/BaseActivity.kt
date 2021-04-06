package kz.logistics

import androidx.appcompat.app.AppCompatActivity


interface BaseActivity {

    val app: App
        get() = getInstance().application as App

    private fun getInstance() = this as? AppCompatActivity
        ?: throw NotExtendedActivityException(this::class.java.simpleName)

    private class NotExtendedActivityException(
        className: String
    ) : IllegalStateException("class $className must extend android.app.Activity")
}