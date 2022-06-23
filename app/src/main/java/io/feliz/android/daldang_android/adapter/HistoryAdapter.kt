package io.feliz.android.daldang_android.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devs.vectorchildfinder.VectorChildFinder
import io.feliz.android.daldang_android.ItemReport
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(val list: MutableList<ItemReport>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_history, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vector = VectorChildFinder(holder.imgBrix.context, R.drawable.ic_apple, holder.imgBrix)
            val path = vector.findPathByName("path_apple_color")
            path.fillColor = Color.rgb(list[position].Red, list[position].Green, list[position].Blue)
            holder.imgBrix.invalidate()
        } else {
            val vector = VectorChildFinder(holder.imgBrix.context, R.drawable.ic_apple, holder.imgBrix)
            val path = vector.findPathByName("path_apple_color")
            path.fillColor = Color.rgb(list[position].Red.toInt(), list[position].Green.toInt(), list[position].Blue.toInt())
            holder.imgBrix.invalidate()
        }
        holder.textBrix.text = list[position].Brix.toString()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgBrix = itemView.img_apple
        val textBrix = itemView.text_brix_num
    }
}