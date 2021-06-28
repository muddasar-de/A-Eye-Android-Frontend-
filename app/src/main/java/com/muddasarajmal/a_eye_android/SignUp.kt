package com.muddasarajmal.a_eye_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.muddasarajmal.a_eye_android.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var authetication: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        authetication = Firebase.auth
        setSupportActionBar(topAppBar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.signUpButtonId.setOnClickListener {
            signUp()
            // hide keypad
            hideKeypad()
        }
        binding.signInText.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }



    private fun signUp() {
        val userName = binding.userNameId
        val userAge = binding.userAgeId
        val maleBtn = binding.male
        val femaleBtn = binding.female
        val guideName = binding.guideNameId
        val guideEmail = binding.guideEmailId
        var userPassword = binding.passwordId
        var confirmPassword = binding.confirmPasswordId

        if (userName.text.toString().isEmpty()) {
            userName.error = "Please enter user name"
            userName.requestFocus()
            return
        }
        if (userAge.text.toString().isEmpty()) {
            userAge.error = "Please enter the user age"
            userAge.requestFocus()
            return
        }
        if (guideName.text.toString().isEmpty()) {
            guideName.error = "Please enter the guide name"
            guideName.requestFocus()
            return
        }


        var userGender:String
        if (femaleBtn.isChecked){
            userGender = "Female"
        }
        else{
            userGender = "Male"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(guideEmail.text.toString()).matches()) {
            guideEmail.error = "Please enter valid email"
            guideEmail.requestFocus()
            return
        }

        if (userPassword.text.toString().isEmpty()) {
            userPassword.error = "Please enter password"
            userPassword.requestFocus()
            return
        }
        if (!userPassword.text.toString().equals(confirmPassword.text.toString())) {
            confirmPassword.error = "Password doesn't match"
            confirmPassword.requestFocus()
            return
        }

        authetication.createUserWithEmailAndPassword(
            guideEmail.text.toString(),
            userPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    authetication.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                saveData(
                                    userName.text.toString(),
                                    userAge.text.toString(),
                                    userGender,
                                    guideName.text.toString(),
                                    guideEmail.text.toString()
                                )
                                Toast.makeText(
                                    baseContext, "Verification Email link is sent",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, SignIn::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //   ----------------------------------------------- Saving Data to Cloud Database --------------------------------------- //
    fun saveData(
        userName: String,
        userAge: String,
        userGender:String,
        guideName: String,
        guideEmail: String

    ) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["userName"] = userName
        user["userAge"] = userAge
        user["userGender"] = userGender
        user["guideName"] = guideName


        db.collection("users")
            // given line of code add the record with the id of guideEmail
            .document(guideEmail)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data is saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data is saved successfully", Toast.LENGTH_SHORT).show()
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