package io.feliz.android.daldang_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.feliz.android.daldang_android.ItemDocuments
import io.feliz.android.daldang_android.OnItemClickAddress
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.item_location.view.*


class LocationAdapter(private val list: MutableList<ItemDocuments>, val listener: OnItemClickAddress): RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textLocation = itemView.text_location
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_location, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textLocation.text = list[position].address_name
        holder.itemView.setOnClickListener {
            listener.onClick(list[position].address_name, list[position].x, list[position].y)
        }
    }
}