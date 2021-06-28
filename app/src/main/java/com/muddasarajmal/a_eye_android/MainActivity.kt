package com.muddasarajmal.a_eye_android

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.muddasarajmal.a_eye_android.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var authetication: FirebaseAuth
    private lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var db:FirebaseFirestore
    private lateinit var drawerMenu: Menu
    private lateinit var filePath:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        authetication = Firebase.auth
        db = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("shared_PREF", Context.MODE_PRIVATE)
        setDrawer()
        loadProfile()

    }

    fun setDrawer() {
        navigationView = binding.navView
        drawerLayout = binding.drawerLayout
        setSupportActionBar(topAppBar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
//        NavigationUI.setupWithNavController(binding.navView, navController)

//        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navHistory -> startActivity(Intent(this, History::class.java))
                R.id.navTakeADemo -> startActivity(Intent(this, SignIn::class.java))
                R.id.navAbout -> startActivity(Intent(this, SignIn::class.java))
                R.id.navSetting -> startActivity(Intent(this, Setting::class.java))
                R.id.navSignIn -> startActivity(Intent(this, SignIn::class.java))
                R.id.navLogout -> {
                    logOut()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        return NavigationUI.navigateUp(navController, drawerLayout)
//    }
fun loadProfile() {
    val userEmail = sharedPreferences.getString("Email", "")
    val headerView = navigationView.getHeaderView(0)
    val guideName = headerView.findViewById<TextView>(R.id.guideName)
    val currentUserEmail = headerView.findViewById<TextView>(R.id.guideEmail)
    currentUserEmail.setText(userEmail)
    db.collection("users")
        .get()
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    if (userEmail.equals(document.id)){
                        guideName.text =document.data.getValue("guideName").toString()
                    }

                }
            }
        }

}




//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val profilePicture = headerView.findViewById<ImageView>(R.id.guideDP)
//        if (requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
//            filePath = data.data!!
//           var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
//            profilePicture.setImageBitmap(bitmap)
//        }
//    }
    private fun chooseProfilePicture(){

        var intent = Intent()
        intent.setType("Images/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"), 111)

    }

    private fun uploadProfilePicture(){
        if(filePath != null){
            var pd = ProgressDialog(this)
           pd.setTitle("Uploading")
            pd.show()
            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images/pic.jpg")
            imageRef.putFile(filePath)
                .addOnSuccessListener {p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"Profile Picture Uploaded",Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {p0->
                     var progress:Double = (100.0 * p0.bytesTransferred)/p0.totalByteCount
                    pd.setMessage("Uploading ${progress.toInt()}%")
                }

        }
    }


    fun logOut() {
        val currentUser = authetication.currentUser
        val editor = sharedPreferences.edit()
        if (currentUser != null) {
            Firebase.auth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            editor.clear()
             editor.apply()
            loadProfile()
            finish()
        } else {
            Toast.makeText(this, "User is not loged in", Toast.LENGTH_LONG).show()
        }


//        val editor = sharedPreferences.edit()
//        Firebase.auth.signOut()
//        startActivity(Intent(this, SignIn::class.java))
//        editor.clear()
//        editor.apply()
//        Toast.makeText(this, "User loged out successfully", Toast.LENGTH_SHORT).show()
    }


}
