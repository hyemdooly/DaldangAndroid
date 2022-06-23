package io.feliz.android.daldang_android.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.devs.vectorchildfinder.VectorChildFinder
import io.feliz.android.daldang_android.MeasureBrixDTO
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.RetrofitService
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.selector.torch
import kotlinx.android.synthetic.main.activity_camera.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private lateinit var fotoapparat: Fotoapparat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val file = File(Environment.getExternalStorageDirectory().toString() + "/daldang/", "pic.jpg")
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        fotoapparat = Fotoapparat(
            context = this,
            view = camera_view,
            cameraConfiguration = CameraConfiguration(flashMode = torch())
        )
        fotoapparat.start()

        btn_capture.setOnClickListener{
            layout_wait.visibility = View.VISIBLE
            val photoResult = fotoapparat.takePicture()
            lateinit var bitmapCrop: Bitmap
            photoResult.toBitmap().whenAvailable {
                    bitmapPhoto ->
                bitmapCrop = ThumbnailUtils.extractThumbnail(
                    bitmapPhoto!!.bitmap, camera_view.width, camera_view.width,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                )
                val matrix = Matrix()
                matrix.postRotate(90F)
                val bitmapRotated = Bitmap.createBitmap(bitmapCrop, 0, 0, bitmapCrop.width, bitmapCrop.height, matrix, true)

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmapRotated.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val bitmapData = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
                requestSendImage()
                layout_wait.visibility = View.INVISIBLE

            }
        }

    }

    private fun requestSendImage() {

        val retrofit = Retrofit.Builder().baseUrl("https://daldang.cf/").addConverterFactory(
            GsonConverterFactory.create()).build()
        val server = retrofit.create(RetrofitService::class.java)

        val file = File(Environment.getExternalStorageDirectory().toString() + "/daldang/", "pic.jpg")
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, requestFile)


        val sf = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sf.getString("id", "")

        val intent = Intent(applicationContext, ReportActivity::class.java)
        server.measureBrix(id, image).enqueue(object: Callback<MeasureBrixDTO> {
            override fun onFailure(call: Call<MeasureBrixDTO>, t: Throwable) {
                layout_wait.visibility = View.INVISIBLE
                Log.d("response", t.message)
                Toast.makeText(applicationContext, "다시 찍어주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<MeasureBrixDTO>,
                response: Response<MeasureBrixDTO>
            ) {
                layout_wait.visibility = View.INVISIBLE
                if(response.body()!!.StatusCode != 200) {
                    Toast.makeText(applicationContext, response.body()!!.Message, Toast.LENGTH_SHORT).show()

                } else {
                    intent.putExtra("brix", response.body()!!.Brix)
                    intent.putExtra("average", response.body()!!.MeanBrix)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.putExtra("color", Color.rgb(response.body()!!.Red, response.body()!!.Green, response.body()!!.Blue))
                    } else {
                        intent.putExtra("color", Color.rgb(response.body()!!.Red.toInt(), response.body()!!.Green.toInt(), response.body()!!.Blue.toInt()))
                    }
                    startActivity(intent)
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
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
