package com.sgvas21.ddadi21.messengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepositoryImpl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val userRepository = UserRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        testUserCreation()
    }

    private fun testUserCreation() {
        val t = User(
            username = "toma",
            password = "hashed_salted_password",
            profession = "SWE",
            profileImageUrl = "",
            timestamp = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            try {

                userRepository.addUser(t)
                Log.d("MainActivity", "User added")

                val ft = userRepository.getUser(t.username)
                Log.d("MainActivity", "Test user fetched successfully, '${ft}'")

                val updatedT = t.copy(profession = "SRE")
                userRepository.updateUser(updatedT)
                Log.d("MainActivity", "Test user update successfully")

                val fut = userRepository.getUser(t.username)
                Log.d("MainActivity", "Update test user fetched successfully, '${fut}'")

            }catch (e: Exception) {
                Log.e("MainActivity", "Operation failed '${e.message}'", e)
            }
        }
    }
}