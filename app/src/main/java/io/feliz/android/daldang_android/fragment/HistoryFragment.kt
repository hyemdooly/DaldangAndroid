package io.feliz.android.daldang_android.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.ReportDTO
import io.feliz.android.daldang_android.RetrofitService
import io.feliz.android.daldang_android.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_history.view.text_empty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val sf = root.context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sf.getString("id", "")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)
        server.getReports(id).enqueue(object: Callback<ReportDTO>{
            override fun onFailure(call: Call<ReportDTO>, t: Throwable) {
                Toast.makeText(root.context.applicationContext, "측정 이력 불러오기에 실패했습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ReportDTO>, response: Response<ReportDTO>) {
                if(response.body()!!.StatusCode == 200) {
                    if(response.body()!!.Report.isEmpty()) {
                        text_empty.visibility = View.VISIBLE
                    }
                    root.recycler_history.adapter = HistoryAdapter(response.body()!!.Report)
                    root.recycler_history.layoutManager = LinearLayoutManager(root.context)
                    root.recycler_history.adapter!!.notifyDataSetChanged()
                }else Toast.makeText(root.context.applicationContext, response.body()!!.Message, Toast.LENGTH_LONG).show()
            }
        })

        return root
    }
}