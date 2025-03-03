package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
   override fun onCreate( savedInstanceState: Bundle? ) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.menu_upload_content -> {
                startActivity(Intent(this, ContentUploadActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}