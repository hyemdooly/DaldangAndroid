package io.feliz.android.daldang_android.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.RetrofitService
import io.feliz.android.daldang_android.SignUpDTO
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class SignUpActivity : AppCompatActivity() {
    private var img: Bitmap? = null
    private var imageProfile: File = File(Environment.getExternalStorageDirectory().toString() + "/daldang/temp.jpeg")
    private var x: Double = 0.0
    private var y: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)

        img_profile.setOnClickListener{
            selectImage()
        }

        edit_location.setOnClickListener {
            val intent = Intent(this, LocationSearchActivity::class.java)
            startActivityForResult(intent, 2)
        }

        edit_market.setOnClickListener {
            val intent = Intent(this, MarketSelectActivity::class.java)
            startActivityForResult(intent, 3)
        }


        btn_sign_up.setOnClickListener {
            // 서버에 정보 전송하는 코드
            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()
            val orchard = edit_orchard_name.text.toString()
            val location = edit_location.text.toString()
            val market = edit_market.text.toString()
            val kind = edit_kind.text.toString()
            val description = edit_description.text.toString()
            val requestImage = if(imageProfile != null) RequestBody.create(MediaType.parse("multipart/form-data"), imageProfile) else null
            val fileToUpload = if(requestImage != null) MultipartBody.Part.createFormData("profileImage", imageProfile!!.name, requestImage) else null
            Log.d("FILE", fileToUpload.toString())
            if(location.isEmpty() || market.isEmpty() || id.isEmpty() || pw.isEmpty() || orchard.isEmpty()) {
                Toast.makeText(applicationContext, "모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show()
            }else {
                server.postSignUp(id, pw, orchard, location, kind, "풋사과", description, market, y, x, fileToUpload).enqueue(object : Callback<SignUpDTO>{
                    override fun onFailure(call: Call<SignUpDTO>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<SignUpDTO>, response: Response<SignUpDTO>) {
                        Toast.makeText(applicationContext, response.body()!!.Message, Toast.LENGTH_SHORT).show()
                        Log.d("TAG : ", response.body().toString())
                        if(response.body()!!.StatusCode == 200) finish()
                    }
                })
            }
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

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK) {
            when(requestCode)
            {
                1-> {
                    try {
                        val inputStr = contentResolver.openInputStream(data?.data!!)
                        val inputStrCopy = contentResolver.openInputStream(data?.data!!)
                        copyStreamToFile(inputStrCopy, imageProfile)
                        img = BitmapFactory.decodeStream(inputStr)
                        inputStr!!.close()
                        img_profile.scaleType = ImageView.ScaleType.CENTER_CROP
                        img_profile.setImageBitmap(img)

                    } catch (e: Exception){
                        Toast.makeText(applicationContext, "에러 발생", Toast.LENGTH_LONG).show()
                        Log.d("ERROR : ", e.toString())
                    }
                }
                2-> {
                    edit_location.setText(data!!.getStringExtra("address_name"))
                    x = data!!.getDoubleExtra("x", 0.0)
                    y = data!!.getDoubleExtra("y", 0.0)
                }
                3-> {
                    edit_market.setText(data!!.getStringExtra("market_name"))
                }
            }
        }

    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }


}
