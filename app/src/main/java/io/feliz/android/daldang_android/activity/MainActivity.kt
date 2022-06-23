package io.feliz.android.daldang_android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.fragment.HistoryFragment
import io.feliz.android.daldang_android.fragment.HomeFragment
import io.feliz.android.daldang_android.fragment.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_main, HomeFragment()).commitAllowingStateLoss()

        navView.setOnNavigationItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()
            when(item.itemId) {
                R.id.navigation_home -> {
                    transaction.replace(R.id.layout_main, HomeFragment()).commitAllowingStateLoss()
                    true
                }
                R.id.navigation_camera -> {
                    val intent = Intent(applicationContext, CameraActivity::class.java)
                    startActivity(intent)
                    false
                }
                R.id.navigation_history -> {
                    transaction.replace(R.id.layout_main, HistoryFragment()).commitAllowingStateLoss()
                    true
                }
                R.id.navigation_settings -> {
                    transaction.replace(R.id.layout_main, SettingsFragment()).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }


    }


}
