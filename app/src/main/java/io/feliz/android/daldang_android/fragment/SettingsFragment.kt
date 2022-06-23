package io.feliz.android.daldang_android.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.feliz.android.daldang_android.R
import io.feliz.android.daldang_android.RetrofitService
import io.feliz.android.daldang_android.UserDTO
import io.feliz.android.daldang_android.activity.LoginActivity
import io.feliz.android.daldang_android.activity.PolicyActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.btn_policy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.Policy

class SettingsFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val sf = root.context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id = sf.getString("id", "")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://daldang.cf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(RetrofitService::class.java)

        val orchard = sf.getString("orchard", "")
        val location = sf.getString("location", "")
        if(orchard == "" || location == "") {
            server.getUser(id).enqueue(object : Callback<UserDTO> {
                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(root.context, "회원 정보 가져오기에 실패했습니다.\n앱 종료 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if(response.body()!!.StatusCode != 200) Toast.makeText(root.context, response.body()!!.Message, Toast.LENGTH_LONG)
                    else {
                        root.text_user_name.text = response.body()!!.Result.Orchard
                        root.text_user_area.text = response.body()!!.Result.Location
                    }
                }
            })
        } else {
            root.text_user_name.text = orchard
            root.text_user_area.text = location
        }


        root.btn_my_subscribe_service.setOnClickListener {
            Toast.makeText(context, "준비중입니다.", Toast.LENGTH_LONG).show()
        }

        root.btn_policy.setOnClickListener {
            val intent = Intent(root.context, PolicyActivity::class.java)
            startActivity(intent)
        }

        root.btn_logout.setOnClickListener{
            val sf = root.context.applicationContext.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            val editor = sf.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(root.context.applicationContext, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(root.context.applicationContext, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        return root
    }
}