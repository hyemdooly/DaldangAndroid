package io.feliz.android.daldang_android.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.feliz.android.daldang_android.ItemFuture
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.item_day_weather.view.*
import kotlin.math.round

class DayWeatherAdapter(private val list: MutableList<ItemFuture>): RecyclerView.Adapter<DayWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_day_weather, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textDay.text = list[position].DayOfWeek
        holder.textMinTemp.text = "최저 " + round(list[position].MinTemp).toInt().toString() + "°"
        holder.textMaxTemp.text = "최고 " + round(list[position].MaxTemp).toInt().toString() + "°"
        when(list[position].Category) {
            1->{ // 맑음
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_sunny)
            }
            2->{ // 구름
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_cloudy)
            }
            3->{ // 비
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_raining)
            }
            4->{ // 눈
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_snow)
            }
            5->{ // 천둥
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_lightning)
            }
            6->{ // 안개, 바람
                holder.imgWeatherIcon.setImageResource(R.drawable.ic_fog)
            }
        }
        holder.imgWeatherIcon.setColorFilter(Color.parseColor("#707070"))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDay: TextView = itemView.text_day
        val imgWeatherIcon: ImageView = itemView.img_weather_icon
        val textMinTemp: TextView = itemView.text_min_temp
        val textMaxTemp: TextView = itemView.text_max_temp
    }
}