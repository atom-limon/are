package com.example.are

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.are.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var dbauth:FirebaseAuth
    private lateinit var binding:ActivityLoginBinding
    override fun onStart() {    //checking if user is already sign in then
        super.onStart()     //go to direct to main activity
        dbauth = Firebase.auth
        val currentUser = dbauth.currentUser
        if(currentUser==null) return
        else{   //going to home activity
            var intent:Intent = Intent(this,Home::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnlogin.setOnClickListener{    //if login button click
            dbauth = Firebase.auth  //sign in using email and password
            //check for empty credentials
            binding.pbar.visibility=ProgressBar.VISIBLE
            binding.btnlogin.visibility=Button.INVISIBLE
            //checking if credentials are empty
            var ref = Register()
            if(!ref.checkempty(binding.etEmail.text.toString().trim(),binding.etPassword.text.toString().trim())){
                binding.etEmail.setError("empty credentials")
                binding.pbar.visibility=ProgressBar.INVISIBLE
                binding.btnlogin.visibility=Button.VISIBLE
            }
            else{
                dbauth.signInWithEmailAndPassword(binding.etEmail.text.toString().trim(),binding.etPassword.text.toString().trim())
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){    //if login Successfull then go to home activity
                            Toast.makeText(applicationContext, "LogIn Successfull", Toast.LENGTH_SHORT).show()
                            binding.pbar.visibility= ProgressBar.INVISIBLE
                            binding.btnlogin.visibility=Button.VISIBLE
                            var intent:Intent = Intent(this,Home::class.java)
                            startActivity(intent)
                            finish()    //finish the activities clear the backstack
                        }
                    }.addOnFailureListener {exception->
                        binding.pbar.visibility= ProgressBar.INVISIBLE
                        binding.btnlogin.visibility=Button.VISIBLE
                        Toast.makeText(applicationContext, "Login Failed check credentials", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        binding.create.setOnClickListener {
            var intent:Intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
    }
}