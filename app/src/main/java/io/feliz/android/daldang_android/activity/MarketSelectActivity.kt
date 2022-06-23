package io.feliz.android.daldang_android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.feliz.android.daldang_android.*
import io.feliz.android.daldang_android.adapter.MarketListAdapter
import kotlinx.android.synthetic.main.activity_market_select.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarketSelectActivity : AppCompatActivity(), OnItemClickMarket {
    override fun onClick(marketName: String) {
        val intent = Intent().apply {
            putExtra("market_name", marketName)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_select)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)
        server.getMarketList().enqueue(object: Callback<MarketListDTO>{
            override fun onFailure(call: Call<MarketListDTO>, t: Throwable) {
                Toast.makeText(applicationContext, "로딩에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MarketListDTO>, response: Response<MarketListDTO>) {
                if(response.body()!!.StatusCode == 200) {
                    recycler_market.adapter = MarketListAdapter(response.body()!!.Market, this@MarketSelectActivity)
                    recycler_market.layoutManager = LinearLayoutManager(applicationContext)
                    recycler_market.adapter!!.notifyDataSetChanged()
                }
            }

        })

    }

}
