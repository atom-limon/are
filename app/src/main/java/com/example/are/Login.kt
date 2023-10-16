package com.example.are

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.are.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var dbauth:FirebaseAuth
    private lateinit var binding:ActivityLoginBinding
    override fun onStart() {
        super.onStart()
        dbauth = Firebase.auth
        val currentUser = dbauth.currentUser
        if(currentUser==null) return
        else{
            var intent:Intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener{
            dbauth = Firebase.auth
            dbauth.signInWithEmailAndPassword(binding.etEmail.text.toString().trim(),binding.etPassword.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        var intent:Intent = Intent(this,Home::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.tvSignup.setOnClickListener {
            var intent:Intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
    }
}