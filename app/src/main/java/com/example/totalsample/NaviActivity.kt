package com.example.totalsample

import android.os.Bundle
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import com.example.totalsample.databinding.ActivityNavHostBinding
import kotlinx.serialization.Serializable

@Serializable
object NavHomeRoute

@Serializable
data class NavDetailRoute(val id: String)

class NaviActivity : BaseActivity<ActivityNavHostBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        // 使用 DSL 构建路由表
        navController.graph = navController.createGraph(startDestination = NavHomeRoute) {
            fragment<NavHomeFragment, NavHomeRoute>()
            fragment<NavDetailFragment, NavDetailRoute>()
        }
    }
}
