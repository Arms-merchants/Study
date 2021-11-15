package com.arms.flowview


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arms.flowview.view.ColorChangeTextView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv_1 = findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener {
                val intent = Intent(this@MainActivity, TvColorChangeActivity::class.java)
                startActivity(intent)
            }
        }
        Thread {
            tv_1.text = "子线程修改" }.start()

        findViewById<View>(R.id.tv_2).setOnClickListener {
            val intent = Intent(this, TestFishActivity::class.java)
            startActivity(intent)
        }

        val view = findViewById<ColorChangeTextView>(R.id.mtv)

        view.setOnClickListener {
            ObjectAnimator.ofFloat(view, "percent", 0f, 1f).setDuration(5000).start()
        }
    }
}