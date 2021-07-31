package com.josus.car.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.josus.car.R
import com.josus.car.databinding.ActivityOtpBinding
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "OTPActivity"
    }

    //Declaring data binding object
    lateinit var binding: ActivityOtpBinding

    //Declaring FirebaseAuth instance
    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String?=null


    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_otp)

        //Setting the layout with data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        if (storedVerificationId == null && savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState)
        }

        //Initialising Firebase Auth
        auth = Firebase.auth

        //Setting the language of the message sent
        auth.useAppLanguage()



        //Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Phone Verification")
        progressDialog.setCanceledOnTouchOutside(false)

        //Getting the entered mobile number from intent
        val enteredNumber = intent?.extras?.getString("mobile").toString()

        //Setting the mobile number - "+91 ${enteredNumber}"
        "+91 ${enteredNumber}".also { binding.txtPhoneEnteredNumber.text = it }

        //Changing the mobile number
        binding.txtChangeMob.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("check", true)
            startActivity(intent)
        }

        //Getting numbers from OTP edit Texts
        val otpNum1 = binding.etOtpNum1.text.toString()
        val otpNum2 = binding.etOtpNum2.text.toString()
        val otpNum3 = binding.etOtpNum3.text.toString()
        val otpNum4 = binding.etOtpNum4.text.toString()
        val otpNum5 = binding.etOtpNum5.text.toString()
        val otpNum6 = binding.etOtpNum6.text.toString()


        //Moving the Edit text OTP Focus
        moveOtpNumber()



        // Initialize phone auth callbacks-[start]
        callBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: $credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Log.w(TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException){
                    setToast("Invalid OTP",1)
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                progressDialog.dismiss()
               // super.onCodeSent(verificationId, token)
            }

        }
        //phone auth callbacks-[End]


        //Starting the Verification-sending the phone number-inside onCreate
        val number = "+91 ${enteredNumber}"
       // startPhoneNumberVerification(number)


        //Resend OTP click[START]
        binding.txtResendOtp.setOnClickListener {
            val number = "+91${enteredNumber}"
           // resendVerificationCode(number,resendToken)
            setToast("Ok, Sure",0)
        }
        //Resend OTP click [END]

        //Button Proceed Click-[START]
        binding.btnProceed.setOnClickListener {

            /*
               val enteredCode=otpNum1+otpNum2+otpNum3+otpNum4+otpNum5+otpNum6
               if (storedVerificationId.isNullOrEmpty()){
                binding.txtErrorMsgs.text="Verification id is null"
            }
            else{
                verifyPhoneNumberWithCode(storedVerificationId,enteredCode)
                Log.d(TAG,storedVerificationId.toString())
            }
             */

            val intent = Intent(this, GmailActivity::class.java)
            startActivity(intent)
        }
        //BTN Proceed [END]

    }
    //OnCreate- [END]

    //Default Back Press - is disabled
    override fun onBackPressed() {
    }

    //Default Toast Function
    private fun setToast(msg: String, length: Int) {
        Toast.makeText(this, msg, length).show()
    }

    //Fun to check OTP numbers- to enable Proceed Button
    private fun checkNumber(): Boolean {
        //Getting numbers from OTP edit Texts
        val otpNum1 = binding.etOtpNum1.text.toString()
        val otpNum2 = binding.etOtpNum2.text.toString()
        val otpNum3 = binding.etOtpNum3.text.toString()
        val otpNum4 = binding.etOtpNum4.text.toString()
        val otpNum5 = binding.etOtpNum5.text.toString()
        val otpNum6 = binding.etOtpNum6.text.toString()
        return (otpNum1.isNotEmpty() && otpNum2.isNotEmpty() && otpNum3.isNotEmpty() && otpNum4.isNotEmpty() && otpNum5.isNotEmpty()
                && otpNum6.isNotEmpty())


    }

    //Fun to move numbers in edit text
    private fun moveOtpNumber() {
        //Edit Text OTP 1
        binding.etOtpNum1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (enteredNumber!!.isNotEmpty()) {
                    binding.etOtpNum2.requestFocus()
                }
                binding.btnProceed.isEnabled = checkNumber()

            }

            override fun afterTextChanged(number: Editable?) {

            }
        })

        //Edit Text OTP 2
        binding.etOtpNum2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (enteredNumber!!.isNotEmpty()) {
                    binding.etOtpNum3.requestFocus()
                }
                binding.btnProceed.isEnabled = checkNumber()
            }

            override fun afterTextChanged(number: Editable?) {
                if (number.toString().isEmpty()) {
                    binding.etOtpNum1.requestFocus()
                }
            }
        })

        //Edit Text OTP 3
        binding.etOtpNum3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (enteredNumber!!.isNotEmpty()) {
                    binding.etOtpNum4.requestFocus()
                }
                binding.btnProceed.isEnabled = checkNumber()
            }

            override fun afterTextChanged(number: Editable?) {
                if (number.toString().isEmpty()) {
                    binding.etOtpNum2.requestFocus()
                }
            }
        })

        //Edit Text OTP 4
        binding.etOtpNum4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                enteredNumber: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (enteredNumber!!.isNotEmpty()) {
                    binding.etOtpNum5.requestFocus()
                }
                binding.btnProceed.isEnabled = checkNumber()
            }

            override fun afterTextChanged(number: Editable?) {
                if (number.toString().isEmpty()) {
                    binding.etOtpNum3.requestFocus()
                }
            }
        })

        //Edit Text OTP 5
        binding.etOtpNum5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                enteredNumber: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (enteredNumber!!.isNotEmpty()) {
                    binding.etOtpNum6.requestFocus()
                }
                binding.btnProceed.isEnabled = checkNumber()
            }

            override fun afterTextChanged(number: Editable?) {
                if (number.toString().isEmpty()) {
                    binding.etOtpNum4.requestFocus()
                }
            }
        })

        //Edit Text OTP 6
        binding.etOtpNum6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                enteredNumber: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(enteredNumber: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnProceed.isEnabled = checkNumber()
            }

            override fun afterTextChanged(number: Editable?) {
                if (number.toString().isEmpty()) {
                    binding.etOtpNum5.requestFocus()
                }
            }
        })
    }
    //Fun to move numbers in edit text- End

    //Fun to send the ph number for verification[START]- 1
    private fun startPhoneNumberVerification(phoneNumber: String) {
        progressDialog.setMessage("Getting the OTP..")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callBacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    //Fun to send the ph number for verification[END]

    // [START resend_verification]-4
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        progressDialog.setMessage("Resending OTP..")
        progressDialog.show()

        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callBacks)
        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]

    //Verifying Code [START]-2
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        try {
            progressDialog.setMessage("Verifying Phone Number..")
            progressDialog.show()
            val credential = PhoneAuthProvider.getCredential(verificationId!!,code)
            signInWithPhoneAuthCredential(credential)
        }
        catch (e:Exception){
            setToast("verifyPhoneNumberWithCode error",1)
            binding.txtErrorMsgs.text="verifyPhoneNumberWithCode error"
            Log.d(TAG,e.toString())
        }

    }
    //Verifying Code [END]

     // [START sign_in_with_phone]-3
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
         //  FirebaseAuth.getInstance().
         auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user=task.result?.user?.phoneNumber
                   // val intent = Intent(this, MainActivity::class.java)
                    //startActivity(intent)
                }
                else{
                    // Sign in failed, display a message and update the UI
                        progressDialog.dismiss()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException){
                        progressDialog.dismiss()
                        setToast("Invalid OTP",1)
                    }
                }
            }
    }
    // [END sign_in_with_phone]
}