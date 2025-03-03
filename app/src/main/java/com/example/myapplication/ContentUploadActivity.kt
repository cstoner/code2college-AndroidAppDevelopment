package com.example.myapplication

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityContentUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ContentUploadActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityContentUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchAllPosts(binding)

        binding.uploadContentSubmitButton.setOnClickListener {
            val textContent = binding.uploadContentTextBox.text.toString()

            val databaseRef = database.reference
            val postId = UUID.randomUUID().toString()

            database.reference.child("users")
                .child(auth.currentUser?.uid!!)
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    val username = dataSnapshot.child("username").value.toString()
                    val profileUrl = dataSnapshot.child("profileUrl").value.toString()

                    databaseRef.child("posts")
                        .child(postId)
                        .updateChildren(
                            mapOf(
                                "text" to textContent,
                                "user" to username,
                                "profileUrl" to profileUrl
                            )
                        )
                }

            binding.uploadContentTextBox.setText("")
        }
    }

    private fun fetchAllPosts(binding: ActivityContentUploadBinding) {
        val postsRef = database.reference.child("posts")
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // clear existing posts
                    binding.linearLayoutPosts.removeAllViews()
                    snapshot.children.forEach { childSnapshot ->
                        val postText = childSnapshot.child("text").value.toString()
                        val postUser = childSnapshot.child("user").value.toString()
                        val profileUrl = childSnapshot.child("profileUrl").value?.toString()

                        val post = createPost(postText, postUser, profileUrl)
                        // Add the TextView to your layout
                        binding.linearLayoutPosts.addView(post)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createPost(text: String, username: String, profileUrl: String?): CardView {
        val postLayout = LinearLayout(this@ContentUploadActivity)
        postLayout.orientation = LinearLayout.VERTICAL

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 16, 16, 16)

        val cardView = CardView(this@ContentUploadActivity)
        cardView.radius = 15f
        cardView.setCardBackgroundColor(Color.parseColor("#ffffff"))
        cardView.setContentPadding(36, 36, 36, 36)
        cardView.layoutParams = params
        cardView.cardElevation = 30f

        val cardContent = TextView(this@ContentUploadActivity)
        cardContent.text = text
        cardContent.textSize = 24f
        cardContent.setTextColor(Color.DKGRAY)
        cardContent.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)

        val name = TextView(this@ContentUploadActivity)
        name.textSize = 16f
        name.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC)
        name.setTextColor(Color.DKGRAY)
        name.text = username

        postLayout.addView(cardContent)

        profileUrl?.let {
            val imageView = ImageView(this@ContentUploadActivity)
            Glide.with(this@ContentUploadActivity)
                .load(it) // URL from the post data
                .into(imageView) // ImageView in your post item layout
            postLayout.addView(imageView)
        }

        postLayout.addView(name)

        val replyButton = Button(this@ContentUploadActivity)
        replyButton.text = "Reply"
        replyButton.setOnClickListener {
            Log.d("Clicked reply button", "clicked reply button")
        }
        postLayout.addView(replyButton)

        cardView.addView(postLayout)

        return cardView
    }
}