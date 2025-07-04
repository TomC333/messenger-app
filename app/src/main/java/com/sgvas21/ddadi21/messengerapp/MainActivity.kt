package com.sgvas21.ddadi21.messengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        if (savedInstanceState == null) {
            if (SessionManager.isSignedIn(this)) {
                val username = SessionManager.getUsername(this)
                Log.d("MainActivity", "Active user: $username")

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