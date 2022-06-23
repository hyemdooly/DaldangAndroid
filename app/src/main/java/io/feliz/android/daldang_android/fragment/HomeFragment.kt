package io.feliz.android.daldang_android.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import io.feliz.android.daldang_android.*
import io.feliz.android.daldang_android.activity.WeatherActivity
import kotlinx.android.synthetic.main.chart_marker.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.line_chart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var lineChart: LineChart
    private lateinit var mContext: Context
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        lineChart = root.line_chart
        mContext = root.context
        val sf = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        id = sf.getString("id", "")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)

        server.getUser(id).enqueue(object : Callback<UserDTO>{
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Toast.makeText(root.context, "회원 정보 가져오기에 실패했습니다.\n앱 종료 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                root.text_hello.text = "안녕하세요,\n"+response.body()!!.Result.Orchard+"님!"
                root.text_count_number.text = response.body()!!.Result.MeasureCount.toString()
                root.text_location.text = response.body()!!.Result.Location
                root.text_chart_name.text = response.body()!!.Result.Market + " 일주일간 가격 추이"

                val sf = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE)
                if(sf.getString("orchard", "") == "" || sf.getString("location", "") == ""){
                    val editor = sf.edit()
                    editor.putString("orchard", response.body()!!.Result.Orchard)
                    editor.putString("location", response.body()!!.Result.Location)
                    editor.apply()
                }
            }
        })

        server.getNowWeather(id).enqueue(object: Callback<NowWeatherDTO>{
            override fun onFailure(call: Call<NowWeatherDTO>, t: Throwable) {
                Toast.makeText(root.context, "날씨 정보 가져오기에 실패했습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NowWeatherDTO>, response: Response<NowWeatherDTO>) {
                if(response.body()!!.StatusCode == 200) {
                    root.text_temp.text = response.body()!!.Weather.Temp
                    root.text_rain_percent_main.text = "강수 확률 : " + response.body()!!.Weather.RainPercent
                    when(response.body()!!.Weather.Category) {
                        1->{ // 맑음
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_sunny)
                        }
                        2->{ // 구름
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_cloudy)
                        }
                        3->{ // 비
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_raining)
                        }
                        4->{ // 눈
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_snow)
                        }
                        5->{ // 천둥
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_lightning)
                        }
                        6->{ // 안개, 바람
                            root.img_weather_icon.setBackgroundResource(R.drawable.ic_fog)
                        }
                    }
                } else {
                    Toast.makeText(mContext, response.body()!!.Message, Toast.LENGTH_LONG).show()
                }
            }
        })

        server.getPest().enqueue(object: Callback<PestDTO>{
            override fun onFailure(call: Call<PestDTO>, t: Throwable) {
                Toast.makeText(root.context, "병해충 정보 가져오기에 실패했습니다.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PestDTO>, response: Response<PestDTO>) {
                if(response.body()!!.StatusCode == 200) {
                    root.text_pest_name.text = response.body()!!.PestInfo[0].Name
                    root.layout_pest.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.PestInfo[0].RelationVideo))
                        startActivity(intent)
                    }
                } else Toast.makeText(mContext, response.body()!!.Message, Toast.LENGTH_LONG).show()

            }

        })

        root.layout_weather.setOnClickListener {
            val intent = Intent(root.context, WeatherActivity::class.java)
            intent.putExtra("rainPercent", root.text_rain_percent_main.text)
            intent.putExtra("location", root.text_location.text)
            startActivity(intent)
        }



        server.getApplePrice(id).enqueue(object: Callback<ApplePriceDTO>{
            override fun onFailure(call: Call<ApplePriceDTO>, t: Throwable) {
                Log.d("getApplePrice : ", t.message)
            }

            override fun onResponse(call: Call<ApplePriceDTO>, response: Response<ApplePriceDTO>) {
                Log.d("getApplePrice : ", response.body().toString())
                if(response.body()!!.StatusCode == 200) {
                    setLineChart(response.body()!!.Market)
                } else {
                    Toast.makeText(root.context, response.body()!!.Message, Toast.LENGTH_LONG).show()
                }
            }
        })

        return root
    }

    private fun setLineChart(list: MutableList<ItemPrice>) {
        lineChart.invalidate()
        lineChart.clear()

        lineChart.setViewPortOffsets(4f, 4f, 4f, 4f)

        val values = ArrayList<Entry>()
        val labels = ArrayList<String>()
        var i = 0
        for(item in list) {
            values.add(Entry(i.toFloat(), item.Price.toFloat()))
            labels.add(item.Date.replace('-', '.'))
            i = i+1
        }

        val lineDataSet = LineDataSet(values, "Price")

        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(mContext, R.drawable.back_chart_gradient)
        lineDataSet.setDrawValues(false) // 값 표시
        lineDataSet.setDrawCircles(true) // 값 원형 ic
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.color = ContextCompat.getColor(mContext, R.color.colorPrimary)
        lineDataSet.setCircleColor(Color.parseColor("#ffffff"))
        lineDataSet.circleRadius = 4.5F
        lineDataSet.circleHoleRadius = 2.5F
        lineDataSet.circleHoleColor = Color.parseColor("#48f0463d")

        lineDataSet.enableDashedHighlightLine(8f, 7f, 0f)
        lineDataSet.highLightColor = Color.parseColor("#f0463d")
        lineDataSet.setDrawHorizontalHighlightIndicator(false)

        val elevationMarker = ChartMarker(mContext, labels)
        lineChart.marker = elevationMarker

        elevationMarker.setOffset(-165F, -200F)
        lineChart.setDrawMarkers(true)

        lineChart.setPinchZoom(false)
        lineChart.setScaleEnabled(false)
        lineChart.animateY(1000)

        val lineData = LineData()
        lineData.addDataSet(lineDataSet)

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        xAxis.valueFormatter = object: ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }
        }
        xAxis.setLabelCount(7, true) // 최대 몇개까지 보여줄건지
        xAxis.textColor = Color.parseColor("#de000000")

        val yAxisLeft = lineChart.axisLeft

        yAxisLeft.setDrawLabels(false)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.axisMaximum = 100000f
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = lineChart.axisRight
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawAxisLine(false)
        yAxisRight.setDrawGridLines(false)

        lineChart.legend.isEnabled = false
        lineChart.description = null
        lineChart.data = lineData

        lineChart.invalidate()

    }
    class ChartMarker(context: Context?, labels: ArrayList<String>, layoutResource: Int = R.layout.chart_marker): MarkerView(context, layoutResource) {
        private val dateView = text_date
        private val priceView = text_price
        private val labels = labels
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            dateView.text = labels[e?.x!!.toInt()]
            val price = String.format("%d원", e.y.toInt())
            priceView.text = price
        }
    }
}