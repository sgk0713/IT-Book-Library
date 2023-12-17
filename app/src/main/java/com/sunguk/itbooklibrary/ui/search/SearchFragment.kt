package com.sunguk.itbooklibrary.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunguk.itbooklibrary.databinding.FragmentSearchBinding
import com.sunguk.itbooklibrary.ui.base.BaseFragment
import com.sunguk.itbooklibrary.ui.search.adapter.SearchResultAdapter
import com.sunguk.itbooklibrary.util.Pager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        FragmentSearchBinding::inflate

    private val searchResultAdapter by lazy { SearchResultAdapter(viewModel) }

    override fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) {
        initUi()
        initBinding()
    }

    override fun initUi() {
        with(binding) {
            resultRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = searchResultAdapter
                itemAnimator = null
            }
            Pager.initPaging(resultRecyclerView, viewModel)
        }
    }

    override fun initBinding() {
    }


    companion object {
        fun newInstance() = SearchFragment()
    }

}