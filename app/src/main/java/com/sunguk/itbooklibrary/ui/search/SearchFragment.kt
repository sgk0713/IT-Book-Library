package com.sunguk.itbooklibrary.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunguk.itbooklibrary.databinding.FragmentSearchBinding
import com.sunguk.itbooklibrary.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        FragmentSearchBinding::inflate


    override fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) {
        initUi()
        initBinding()
    }

    override fun initUi() {

    }

    override fun initBinding() {

    }


    companion object {
        fun newInstance() = SearchFragment()
    }

}