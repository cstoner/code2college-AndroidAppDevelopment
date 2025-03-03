package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSubmitButton.setOnClickListener {
            val emailTextView = findViewById<TextView>(R.id.emailTextBox)
            val passwordTextView = findViewById<TextView>(R.id.userPasswordTextBox)

            val email = emailTextView.text.toString()
            val password = passwordTextView.text.toString()

            // Inside the OnClickListener for signUpButton
            if (isValidEmail(email) && isPasswordStrong(password)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                if (!isValidEmail(email)) {
                    Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
                }
                if (!isPasswordStrong(password)) {
                    Toast.makeText(this, "Password is not strong enough", Toast.LENGTH_SHORT).show()
                }
            }
            if (email.isNotEmpty() && password.isNotEmpty()) {

            } else {
                Toast.makeText(
                    this,
                    "Username and password must not be blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.returnToLoginTextView.setOnClickListener {
            navigateToLogin()
        }
    }

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun isValidEmail(email: String): Boolean {
        // Add regular expression for email validation
        return email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
    }
    private fun isPasswordStrong(password: String): Boolean {
        // Add conditions for strong password (e.g., length, contains numbers/special characters)
        return password.length > 6 // Add more conditions as needed
    }


}