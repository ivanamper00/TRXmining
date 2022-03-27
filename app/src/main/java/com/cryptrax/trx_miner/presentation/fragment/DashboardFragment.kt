package com.cryptrax.trx_miner.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.dakuinternational.common.domain.model.DataContent
import com.dakuinternational.common.ui.base.BaseFragment
import com.dakuinternational.common.ui.binding.viewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.cryptrax.trx_miner.R
import com.cryptrax.trx_miner.databinding.FragmentDashboardBinding
import com.cryptrax.trx_miner.presentation.adapter.DashboardAdapter

class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {
    private val binding by viewBinding(FragmentDashboardBinding::bind)

    private val args: DashboardFragmentArgs by navArgs()

    private val dashboardAdapter by lazy {
        DashboardAdapter(requireActivity() as DashboardAdapter.OnItemClickListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerView){
            adapter = dashboardAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        val list: List<DataContent> = Gson().fromJson(args.listContent, object:
            TypeToken<List<DataContent>>(){}.type)
        dashboardAdapter.setList(list.sortedBy { it.position })
    }

    companion object{
        const val LIST_CONTENT = "listContent"
    }
}