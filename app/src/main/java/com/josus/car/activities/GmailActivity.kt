package com.josus.car.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.josus.car.R
import com.josus.car.databinding.ActivityGmailBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class GmailActivity : AppCompatActivity() {

    lateinit var binding: ActivityGmailBinding
    lateinit var auth: FirebaseAuth

    //onCreate[START]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_gmail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gmail)

        auth = FirebaseAuth.getInstance()
        binding.btnMailProceed.setOnClickListener {
            val enteredMail = binding.etMail.text.toString()
            val enteredPass = binding.etPass.text.toString()
            loginUser(enteredMail,enteredPass)
        }


    }
    //omCreate[END]


    //[START]Fun to check if the mail is valid or not
    private fun isEmailValid(mail: CharSequence): Boolean {
        val expression = "^[\\\\w\\\\.-]+@([\\\\w\\\\-]+\\\\.)+[A-Z]{2,4}\$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(mail)
        //&& matcher.matches()

        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
    //[END]Fun to check if the mail is valid or not


    //Fun to Login the user and validate[START]
    private fun loginUser(enteredMail:String,enteredPass:String) {


        if (checkGmail(enteredMail)) {
            if (checkPass(enteredPass)) {
                //auth.createUserWithEmailAndPassword(enteredMail,enteredPass)
                     auth.signInWithEmailAndPassword(enteredMail, enteredPass)
                         .addOnCompleteListener(this){task->

                                 if (task.isSuccessful){
                                     val user = auth.currentUser?.email
                                     reload(user)
                                 }
                                 else if(auth.currentUser == null){
                                     registerUser(enteredMail,enteredPass)

                                 }
                             else if(task.exception is FirebaseAuthException){
                                     setToast("Try Again",1)
                                     // setToast("Email not Registered", 1)
                                 }



                         }
            }
        }
    }
    // Fun to Login the user and validate[END]

    //Default Toast Function
    private fun setToast(msg: String, length: Int) {
        Toast.makeText(this, msg, length).show()
    }

    //[START]Fun to set Error on Wrong mail
    private fun setGmailError(error: Boolean, msg: String?) {
        binding.etlMail.isErrorEnabled = error
        binding.etlMail.error = msg
    }
    //[END]Fun to set Error on Wrong mail

    //[START]Fun to set Error on Wrong pass
    private fun setPassError(error: Boolean, msg: String?) {
        binding.etlPass.isErrorEnabled = error
        binding.etlPass.error = msg
    }
    //[END]Fun to set Error on Wrong pass

    //[START]fun to check Mail individually
    private fun checkGmail(mail: String): Boolean {
        val check: Boolean
        if (mail.isEmpty()) {
            setGmailError(true, "Enter Gmail id")
            check = false
        } else if (isEmailValid(mail)) {
            setGmailError(false, null)
            check = true
        } else {
            setGmailError(true, "Invalid Gmail id")
            check = false
        }
        return check
    }
    //[END]fun to check Mail individually

    private fun checkPass(pass: String): Boolean {
        val check: Boolean
        if (pass.isEmpty()) {
            setPassError(true, "Enter Password")
            check = false
        } else if (pass.length >= 7) {
            setPassError(false, null)
            check = true
        } else {
            setPassError(true, "Password must be 7 digit or more")
            check = false
        }
        return check
    }

    //[START]fun to enter the user with mail
    private fun reload(user:String?){
        val intent = Intent(this@GmailActivity, MainActivity::class.java)
        intent.putExtra("currentUser", user)
        startActivity(intent)
    }
    //[END]fun to enter the user with mail

    //[START]fun to register a User
    private fun registerUser(email:String,pass: String){
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    val user = auth.currentUser?.email
                    reload(user)
                }
                else {
                    setToast("Try Again",1)
                }
            }
    }
    //[END]fun to register a User


}