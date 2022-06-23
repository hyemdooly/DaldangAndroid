package io.feliz.android.daldang_android.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import io.feliz.android.daldang_android.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sf = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        Handler().postDelayed({
            if(sf.getString("id", "") == "") {
                val intent = Intent(this, OrchardStartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }

    override fun onBackPressed() { // splash can't be skipped
    }



}
