package io.feliz.android.daldang_android.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import com.devs.vectorchildfinder.VectorChildFinder
import io.feliz.android.daldang_android.R
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val color = intent.getIntExtra("color", Color.parseColor("#FFFFFF"))
        val brix = intent.getFloatExtra("brix", 0F)
        val average = intent.getFloatExtra("average", 0F)

        val vector = VectorChildFinder(applicationContext, R.drawable.ic_apple, img_apple_color)
        val path = vector.findPathByName("path_apple_color")
        path.fillColor = color
        img_apple_color.invalidate()

        when {
            brix >= 14 -> text_grade.text = "특급"
            brix >= 12 -> text_grade.text = "상"
            else -> text_grade.text = "일반"
        }

        text_my_apple_brix_num.text = brix.toString()
        text_average_brix_num.text = average.toString()
        text_my_brix_average.text = String.format("측정값 : %.2f Brix | 평균 : %.2f Brix", brix, average)

        /* set graphs width */
        val myAppleBrixParams = graph_my_apple.layoutParams as ConstraintLayout.LayoutParams
        myAppleBrixParams.matchConstraintPercentWidth = brix / 20.0.toFloat()

        val averageBrixParams = graph_average.layoutParams as ConstraintLayout.LayoutParams
        averageBrixParams.matchConstraintPercentWidth = average / 20.0.toFloat()

        graph_my_apple.requestLayout()
        graph_average.requestLayout()

        btn_go_home.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        btn_measure_new.setOnClickListener {
            finish()
        }


    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
