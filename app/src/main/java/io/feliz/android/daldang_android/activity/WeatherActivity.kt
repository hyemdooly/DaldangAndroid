package io.feliz.android.daldang_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.feliz.android.daldang_android.*
import io.feliz.android.daldang_android.adapter.DayWeatherAdapter
import io.feliz.android.daldang_android.adapter.HourWeatherAdapter
import kotlinx.android.synthetic.main.activity_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

class WeatherActivity : AppCompatActivity() {
    private lateinit var futureList: MutableList<ItemFuture>
    private lateinit var nowList: MutableList<ItemNow>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        text_rain_percent_main.text = intent.getStringExtra("rainPercent")
        text_area.text = intent.getStringExtra("location")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)

        server.weekWeather("test1234").enqueue(object: Callback<WeekWeatherDTO>{
            override fun onFailure(call: Call<WeekWeatherDTO>, t: Throwable) {
                Toast.makeText(applicationContext, "에러 발생", Toast.LENGTH_LONG).show()
                Log.d("ERROR", t.message)
            }

            override fun onResponse(
                call: Call<WeekWeatherDTO>,
                response: Response<WeekWeatherDTO>
            ) {
                Log.d("OK", response.body().toString())
                text_day_of_week.text = response.body()!!.Weather.Now[0].DayOfWeek + "요일"

                futureList = response.body()!!.Weather.Future
                nowList = response.body()!!.Weather.Now
                recycler_day_weather.adapter = DayWeatherAdapter(futureList)
                recycler_day_weather.layoutManager = LinearLayoutManager(applicationContext)
                recycler_day_weather.adapter!!.notifyDataSetChanged()

                recycler_hour_weather.adapter = HourWeatherAdapter(nowList)
                recycler_hour_weather.layoutManager = LinearLayoutManager(applicationContext).apply { orientation = LinearLayoutManager.HORIZONTAL }
                recycler_hour_weather.adapter!!.notifyDataSetChanged()

                text_current_temp.text = round(nowList[0].Temp).toInt().toString() + "°"
            }
        })
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
