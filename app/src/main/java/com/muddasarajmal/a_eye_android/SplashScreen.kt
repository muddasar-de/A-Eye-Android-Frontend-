package com.muddasarajmal.a_eye_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {

    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

        val logo = findViewById<ImageView>(R.id.logo)
        logo.animate().apply {
            duration=1300
            rotationBy(360f)

        }.withEndAction {
            logo.animate().apply {
                duration=1300
                rotationBy(-360f)
        }
            .start()
    }
}
}