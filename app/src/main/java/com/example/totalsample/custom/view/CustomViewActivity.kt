package com.example.totalsample.custom.view

import android.os.Bundle
import android.widget.Toast
import com.example.totalsample.BaseActivity
import com.example.totalsample.databinding.ActivityCustomViewBinding

class CustomViewActivity : BaseActivity<ActivityCustomViewBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vBinding.circleIv.setOnClickListener {
            Toast.makeText(this, "Show Circle ImageView!!!", Toast.LENGTH_SHORT).show()
        }

    }
}