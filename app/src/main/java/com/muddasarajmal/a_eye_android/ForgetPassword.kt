package com.muddasarajmal.a_eye_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.muddasarajmal.a_eye_android.databinding.ActivityForgetPasswordBinding
import com.muddasarajmal.a_eye_android.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_main.*

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var authetication: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        authetication = Firebase.auth
        setSupportActionBar(topAppBar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.resetPasswordButton.setOnClickListener {
            hideKeypad()
            forgetPassword()
        }
    }
    private fun forgetPassword(){
        var emailAddress = binding.emailAddress
        if (emailAddress.text.toString().isEmpty()) {
            emailAddress.error = "Please enter email"
            emailAddress.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress.text.toString()).matches()) {
            emailAddress.error = "Please enter valid email"
            emailAddress.requestFocus()
            return
        }
        authetication.sendPasswordResetEmail(emailAddress.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = authetication.currentUser
                    Toast.makeText(
                        this, "Email Sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this,SignIn::class.java))

                } else{
                    Toast.makeText(
                        this, "Invalide Email Address",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }
    fun hideKeypad() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}