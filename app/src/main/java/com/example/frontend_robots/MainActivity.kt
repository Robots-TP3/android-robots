package com.example.frontend_robots

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend_robots.search.SearchActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentToProfilePage = Intent(this, SearchActivity::class.java)
        startActivity(intentToProfilePage)
    }
}