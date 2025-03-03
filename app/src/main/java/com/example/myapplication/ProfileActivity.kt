package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()

    // Obtain an instance of the Firebase Realtime Database
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileSubmitButton.setOnClickListener {
            val fullName = binding.fullNameTextBox.text.toString()
            val username = binding.usernameProfileTextBox.text.toString()
            val email = binding.emailProfileTextBox.text.toString()

            database.getReference("users")
                .child(auth.currentUser?.uid!!)
                .updateChildren(
                    mapOf(
                        "fullName" to fullName,
                        "username" to username,
                        "email" to email
                    )
                ).addOnSuccessListener {
                    Toast.makeText(baseContext, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}