package com.sgvas21.ddadi21.messengerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance("https://messenger-app-400ae-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("testPath")

        // Write test value
        ref.setValue("Hello Firebase!")
            .addOnSuccessListener {
                Log.d("FirebaseTest", "Database write successful")
            }
            .addOnFailureListener {
                Log.e("FirebaseTest", "Database write failed", it)
            }
    }
}