package com.example.totalsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.totalsample.databinding.ActivityIncrementBinding
import com.example.totalsample.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding.apply {
            btnOne.setOnClickListener {
                startActivity(Intent(this@MainActivity, IncrementActivity::class.java))
            }

            btnTwo.setOnClickListener {
                startActivity(Intent(this@MainActivity, MessagerActivity::class.java))
            }
        }


    }
}