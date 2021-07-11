package com.muddasarajmal.a_eye_android

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class Profile : AppCompatActivity() {
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var db:FirebaseFirestore
    private lateinit var dialogView : View
    private lateinit var filePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(topAppBar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences("shared_PREF", Context.MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()

        readData()
        val editButton:ImageButton = findViewById(R.id.uploadProfilePictureBtn)
        val confirmBtn = findViewById<Button>(R.id.confirm_button)
        confirmBtn.visibility = View.GONE
        editButton.setOnClickListener {


            chooseProfilePicture()
            confirmBtn.visibility = View.VISIBLE
        }
        confirmBtn.setOnClickListener {
            uploadProfilePicture()
                confirmBtn.visibility = View.GONE
        }
    }
    fun readData() {
        val userEmail = sharedPreferences.getString("Email", "")
        val userName = findViewById<TextView>(R.id.userName)
        val userAge = findViewById<TextView>(R.id.userAge)
        val guideName = findViewById<TextView>(R.id.guideName)
        val userGender = findViewById<TextView>(R.id.userGender)
        db.collection("users")
            .get()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (userEmail.equals(document.id)){
                            userName.text =document.data.getValue("userName").toString()
                            userAge.text =document.data.getValue("userAge").toString()
                            guideName.text =document.data.getValue("guideName").toString()
                            userGender.text =document.data.getValue("userGender").toString()

                        }
                        else{
                            Toast.makeText(this, "No data retched", Toast.LENGTH_LONG).show()
                        }

                    }

                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
        val profilePicture = findViewById<ImageView>(R.id.guideDP)
        if (requestCode==456 && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!
           var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            profilePicture.setImageBitmap(bitmap)
        }
  }
    private fun chooseProfilePicture(){

        var intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 456)

    }

    private fun uploadProfilePicture(){
        val userEmail = sharedPreferences.getString("Email", "")
        val imageUrl = (Math.random() * 9000).toInt() + 1000
        val pathString = "images/$imageUrl.jpg"
        if(filePath != null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()
            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child(pathString)
            imageRef.putFile(filePath)
                .addOnSuccessListener { p0->
                    val map = mutableMapOf<String,Any>()
                    map["dpUrl"] = pathString
                    db.collection("users")
                        .document("$userEmail").update(map)
                    pd.dismiss()
                    Toast.makeText(applicationContext,
                        "Profile Picture Uploaded",
                        Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{ p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener { p0->
                    var progress:Double = (100.0 * p0.bytesTransferred)/p0.totalByteCount
                    pd.setMessage("Uploading ${progress.toInt()}%")
                }

        }
    }
    private fun setDp(){
        
        val userEmail = sharedPreferences.getString("Email", "")
        db.collection("users")
            .document("$userEmail")
            .get()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                }

        val storageRef = FirebaseStorage.getInstance().reference.child()
    }
}