package com.example.are

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.are.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var dbauth:FirebaseAuth
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignup.setOnClickListener {
            dbauth = Firebase.auth
            dbauth.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(),binding.etPassword.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        var intent: Intent = Intent(this,Login::class.java)
                        startActivity(intent)
                    }
                }
        }
    }
}