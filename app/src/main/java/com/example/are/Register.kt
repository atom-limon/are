package com.example.are

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.are.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.EventListener

class Register : AppCompatActivity() {
    private lateinit var dbauth:FirebaseAuth
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var db:FirebaseDatabase
    private lateinit var dbrefrence:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)   //view binder
        setContentView(binding.root)
        //clicking already have an account
        binding.already.setOnClickListener {
            //transitioning to signup activity
            var intent:Intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {  //clicking on sign up button
            db = FirebaseDatabase.getInstance() //creating instance of realtime database
            dbrefrence = db.getReference().child("userinfo") //getting refrence of root/userinfo
            binding.pbarreg.visibility = ProgressBar.VISIBLE
            binding.btnSignup.visibility = Button.INVISIBLE
            //if credentials are empty
            if(!checkempty(binding.etusername.text.toString().trim(), binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim())){
                binding.pbarreg.visibility = ProgressBar.INVISIBLE
                binding.btnSignup.visibility = Button.VISIBLE
                binding.etusername.setError("username can not be empty")    //error on username
                binding.etEmail.setError("email required")  //error on username
                binding.etPassword.setError("empty password")   //error on password
            }
            else if(TextUtils.getTrimmedLength(binding.etPassword.text.toString())<8){
                binding.pbarreg.visibility = ProgressBar.INVISIBLE
                binding.btnSignup.visibility = Button.VISIBLE
                binding.etPassword.setError("atleast 8 character")  //password should be 8 character
            }
            else{
                dbrefrence.orderByChild("username").equalTo(binding.etusername.text.toString().trim())
                    .addListenerForSingleValueEvent(object : ValueEventListener{    //checking if username already exist or not
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){  //if exists then show error
                                binding.etusername.setError("username already taken")
                                binding.pbarreg.visibility = ProgressBar.INVISIBLE
                                binding.btnSignup.visibility = Button.VISIBLE
                            }
                            else{   //if not then create user
                                dbauth = Firebase.auth
                                dbauth.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(),binding.etPassword.text.toString().trim())
                                    .addOnCompleteListener {
                                        if(it.isSuccessful){    //user created successfully then
                                            //store userinfo in database
                                            var dbcell:DatabaseReference = dbrefrence.child(dbauth.currentUser!!.uid)//refrence of current user uid
                                            dbcell.child("username").setValue(binding.etusername.text.toString().trim())//storing usernmae
                                            dbcell.child("mail").setValue(binding.etEmail.text.toString().trim())//storing mail
                                            //toasting Signup successfull
                                            Toast.makeText(applicationContext, "signup successfull", Toast.LENGTH_SHORT).show()
                                            //first signout of the account
                                            dbauth.signOut()    //switching user to login activity as of first sign up
                                            //now pass intent of login
                                            binding.pbarreg.visibility = ProgressBar.INVISIBLE
                                            binding.btnSignup.visibility = Button.VISIBLE
                                            var intent:Intent = Intent(this@Register,Login::class.java)
                                            startActivity(intent);
                                        }
                                        else{
                                            //all cases are checked else duplicate email so if not successfull
                                            //the it must be email problem
                                            binding.etEmail.setError("Email already exists")
                                            binding.pbarreg.visibility = ProgressBar.INVISIBLE
                                            binding.btnSignup.visibility = Button.VISIBLE
                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
                    })
            }
        }
    }
    fun checkempty(username:String,email:String,password:String):Boolean{
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
            return false
        else
            return true
    }

    fun checkempty(email: String, password: String): Boolean {
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)) return false
        return true
    }
}