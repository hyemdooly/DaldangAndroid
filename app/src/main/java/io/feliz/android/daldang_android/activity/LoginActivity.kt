package io.feliz.android.daldang_android.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.RetrofitService
import io.feliz.android.daldang_android.SignInDTO
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val folder = File(Environment.getExternalStorageDirectory().toString() + "/daldang")
        if(!folder.exists()) folder.mkdir()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/") // ip입력
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        btn_login.setOnClickListener {
            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()
            server.postSignIn(id, pw).enqueue(object : Callback<SignInDTO>{
                override fun onFailure(call: Call<SignInDTO>, t: Throwable) {
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    Log.d("Failure", t.message)
                }

                override fun onResponse(
                    call: Call<SignInDTO>,
                    response: Response<SignInDTO>
                ) {
                    if(response.body()!!.StatusCode == 200) {
                        Toast.makeText(applicationContext, response.body()!!.Message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        val sf = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor = sf.edit()
                        editor.putString("id", id)
                        editor.apply()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, response.body()!!.Message, Toast.LENGTH_SHORT).show()
                    }
                }
            })


        }

        btn_sign_up.setOnClickListener {
            val intent = Intent(this, CheckPolicyActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
