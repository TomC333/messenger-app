package com.sgvas21.ddadi21.messengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.sgvas21.ddadi21.messengerapp.ui.MainFragment
import com.sgvas21.ddadi21.messengerapp.ui.auth.SigninFragment
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Remove system bards from the view
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())


        if (savedInstanceState == null) {
            if (SessionManager.isSignedIn(this)) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, MainFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SigninFragment())
                    .commit()
            }
        }
    }

    fun handleSignOut() {
        SessionManager.saveSignedIn(this, false)
        SessionManager.clear(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, SigninFragment())
            .commit()
    }
}