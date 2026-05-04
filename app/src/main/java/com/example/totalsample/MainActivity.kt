package com.example.totalsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.totalsample.custom.view.CustomViewActivity
import com.example.totalsample.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    val tag = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding.apply {
            btnOne.setOnClickListener {
                startActivity(Intent(this@MainActivity, IncrementActivity::class.java))
            }

            btnTwo.setOnClickListener {
                startActivity(Intent(this@MainActivity, MessengerActivity::class.java))
            }

            btnThree.setOnClickListener {
                startActivity(Intent(this@MainActivity, CustomViewActivity::class.java))
            }

            btnFour.setOnClickListener {
                sum(100).also {
                    Log.i(tag, "get the sum:$it")
                }
            }
            btnFive.setOnClickListener {
                startActivity(Intent(this@MainActivity, NaviActivity::class.java))
            }
        }
    }

    // 输入数字n，救1-n相加的和
    fun sum(n: Int): Int {
        val median = n / 2
        val divisible = n % 2 == 0
        var temp = 0
        for (i in 0 until n) {
            temp += (n - i) + (n - (n - 1 - i))
            Log.i(tag, "The sum of the $i: $temp ")
            if (i == median - 1) {
                if (divisible) {
                    break
                } else {
                    temp += n - median
                    Log.i(tag, "Add the median ${n - median},sum:$temp")
                    break
                }
            }
        }

        return temp
    }
}
