package com.example.totalsample

import android.content.Intent
import android.os.Bundle
import com.example.totalsample.custom.view.CustomViewActivity
import com.example.totalsample.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

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
        }


    }
}