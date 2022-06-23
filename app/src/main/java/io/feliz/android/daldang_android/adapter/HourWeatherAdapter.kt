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
import io.feliz.android.daldang_android.ItemNow
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.item_hour_weather.view.*
import kotlin.math.round

class HourWeatherAdapter(private val list: MutableList<ItemNow>): RecyclerView.Adapter<HourWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_hour_weather, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position == 0) {
            holder.textHour.text = "지금"
            holder.textHour.setTextColor(Color.parseColor("#fc594c"))
            holder.imgWeatherIcon.setColorFilter(Color.parseColor("#fc594c"))
            holder.textTemp.setTextColor(Color.parseColor("#fc594c"))
        }else {
            when {
                list[position].Hour > 12 -> holder.textHour.text = "오후 "+ (list[position].Hour - 12).toString() + "시"
                list[position].Hour == 12 -> holder.textHour.text = "오후 12시"
                else -> holder.textHour.text = "오전 "+ list[position].Hour + "시"
            }
            holder.imgWeatherIcon.setColorFilter(Color.parseColor("#707070"))
        }
        holder.textTemp.text = round(list[position].Temp).toInt().toString() + "°"
        Log.d("category : ", list[position].Category.toString())
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

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHour: TextView = itemView.text_hour
        val imgWeatherIcon: ImageView = itemView.img_weather_icon
        val textTemp: TextView = itemView.text_temp
    }
}