package com.josus.car.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.josus.car.R
import com.josus.car.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    //Declaring binding object
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    //OnCreate - Start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_login)

        //Setting the layout with data binding
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth= FirebaseAuth.getInstance()

        //T&C clicks
        binding.txtTnC.setOnClickListener {
            Toast.makeText(this, "Terms & Conditions Accepted", Toast.LENGTH_LONG).show()
        }

        //Button send OTP clicks
        binding.btnOtp.setOnClickListener {

            //Getting the entered number from the user (Edit Text)
            val enteredNumber = binding.etPhone.text.toString()

            if (enteredNumber.length == 10 && enteredNumber.isNotEmpty()) {
                if (binding.cbTnC.isChecked) {
                    //Putting the entered number in intent
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtra("mobile", enteredNumber)
                    startActivity(intent)
                } else {
                    Snackbar.make(binding.cbTnC, "Accept Terms & Condition", Snackbar.LENGTH_LONG)
                        .show()
                }
            } else {
                setToast("Enter 10 digit Mobile Number", 1)
            }
        }
    }
    //onCreate - End

    override fun onBackPressed() {
        val check = intent?.extras?.getBoolean("check")
        if (check!!) {
            setSnackBar("Click Get OTP", 0, binding.btnOtp)
        } else {
            super.onBackPressed()
        }

    }

    //Default Toast Function
    private fun setToast(msg: String, length: Int) {
        Toast.makeText(this, msg, length).show()
    }
    //Default SnackBar Function
    private fun setSnackBar(msg: String, length: Int, view: View) {
        Snackbar.make(view, msg, length).show()
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        val currentUser=auth.currentUser
        val user=auth.currentUser?.email
        if (currentUser != null){
            reload(user)
        }

    }
    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        val check = data?.extras?.getBoolean("check")
        val checkNum = check.toString().isEmpty()
        if (checkNum) {
            binding.mobTxt.visibility = View.VISIBLE
            binding.txtEnterNewMo.visibility = View.INVISIBLE
            binding.txtEnterNewNum.visibility = View.INVISIBLE
            binding.cbTnC.isChecked = false
        } else {
            binding.mobTxt.visibility = View.INVISIBLE
            binding.txtEnterNewMo.visibility = View.VISIBLE
            binding.txtEnterNewNum.visibility = View.VISIBLE
            binding.cbTnC.isChecked = true
        }
    }


    private fun reload(user:String?){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("currentUser", user)
        startActivity(intent)
    }

}
