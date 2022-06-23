package io.feliz.android.daldang_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.feliz.android.daldang_android.OnItemClickMarket
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.item_list_market.view.*


class MarketListAdapter(private val list: MutableList<String>, val listener: OnItemClickMarket): RecyclerView.Adapter<MarketListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textMarket = itemView.text_market
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_list_market, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textMarket.text = list[position]
        holder.itemView.setOnClickListener {
            listener.onClick(list[position])
        }
    }
}