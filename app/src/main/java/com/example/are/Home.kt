package com.example.are

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.are.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var dbauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnlogout.setOnClickListener {
            dbauth = Firebase.auth
            dbauth.signOut()
            var intent:Intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}