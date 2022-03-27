package com.cryptrax.trx_miner.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.dakuinternational.common.domain.model.DataContent
import com.dakuinternational.common.domain.model.Response
import com.dakuinternational.common.ui.ActivityViewModel
import com.dakuinternational.common.ui.base.BaseActivity
import com.dakuinternational.common.ui.binding.viewBinding
import com.dakuinternational.common.ui.utils.showToast
import com.google.gson.Gson
import com.cryptrax.trx_miner.R
import com.cryptrax.trx_miner.databinding.ActivityMainBinding
import com.cryptrax.trx_miner.presentation.adapter.DashboardAdapter
import com.cryptrax.trx_miner.presentation.fragment.DashboardFragment
import com.cryptrax.trx_miner.presentation.fragment.DashboardFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), DashboardAdapter.OnItemClickListener {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel by viewModels<ActivityViewModel>()

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
    }

    private val navController get() = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getData(DATABASE_NAME)
        viewModel.uiState.observe(this){
            when(it){
                is Response.Loading -> showLoading(it.isLoading)
                is Response.Error -> showToast(it.message)
                is Response.Success ->  shareData(it.data)
                else -> {
                    //no - op
                }
            }
        }
    }

    private fun shareData(data: List<DataContent>) {
        intent.putExtra(DashboardFragment.LIST_CONTENT, Gson().toJson(data))
        navController.setGraph(R.navigation.navigation_graph, intent.extras)
    }

    companion object{
        private val DATABASE_NAME = "tron_mining"
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onItemClick(data: DataContent, position: Int) {
        val direction = DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(Gson().toJson(data), position)
        navController.navigate(direction)
    }
}