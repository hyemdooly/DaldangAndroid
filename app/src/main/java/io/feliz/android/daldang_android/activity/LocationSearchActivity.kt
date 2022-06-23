package io.feliz.android.daldang_android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.feliz.android.daldang_android.*
import io.feliz.android.daldang_android.adapter.LocationAdapter
import kotlinx.android.synthetic.main.activity_location_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent


class LocationSearchActivity : AppCompatActivity(), OnItemClickAddress{
    override fun onClick(addressName: String, x: Double, y: Double) {
        val intent = Intent().apply {
            putExtra("address_name", addressName)
            putExtra("x", x)
            putExtra("y", y)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(KakaoRetrofitService::class.java)

        btn_search.setOnClickListener {
                server.searchLocation(edit_search.text.toString()).enqueue(object: Callback<LocationDTO>{
                    override fun onFailure(call: Call<LocationDTO>, t: Throwable) {
                        Log.d("SEARCH : ", t.message)
                    }

                    override fun onResponse(
                        call: Call<LocationDTO>,
                        response: Response<LocationDTO>
                    ) {
                        Log.d("SEARCH : ", response.body().toString())
                        if(response.code() == 200){
                            val result = response.body()
                            if(result == null) Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_LONG).show()
                            else {
                                recycler_location.adapter = LocationAdapter(result.documents, this@LocationSearchActivity)
                                recycler_location.layoutManager =LinearLayoutManager(applicationContext)
                                recycler_location.adapter!!.notifyDataSetChanged()
                            }
                        } else Toast.makeText(applicationContext, "다시 검색해주세요", Toast.LENGTH_LONG).show()

                    }
                })


        }
    }
}
