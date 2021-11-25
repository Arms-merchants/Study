package com.arms.flowview


import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.window.SplashScreenView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.arms.flowview.ktx.KtxTest
import com.arms.flowview.view.ColorChangeTextView
import com.arms.flowview.vp.TestVp2Activity
import com.arms.flowview.vp.VpTestActivity
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_1 = findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener {
                val intent = Intent(this@MainActivity, RvListActivity::class.java)
                startActivity(intent)
            }
        }
        findViewById<TextView>(R.id.tv_3).setOnClickListener {
            val intent = Intent(this@MainActivity, VpTestActivity::class.java)
            startActivity(intent)
        }
        //这里就是只要你够快在线程检查之前去修改那么就没有问题，这里就是要查看Activity的启动流程
        Thread {
            tv_1.text = "子线程修改"
        }.start()

        findViewById<View>(R.id.tv_2).setOnClickListener {
            val intent = Intent(this, TestVp2Activity::class.java)
            startActivity(intent)
        }

        val view = findViewById<ColorChangeTextView>(R.id.mtv)

        view.setOnClickListener {
            ObjectAnimator.ofFloat(view, "percent", 0f, 1f).setDuration(5000).start()
        }
    }
}