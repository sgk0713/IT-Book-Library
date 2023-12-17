package com.sunguk.itbooklibrary.ui.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.sunguk.itbooklibrary.databinding.FragmentBookdetailBinding
import com.sunguk.itbooklibrary.ui.base.BaseFragment
import com.sunguk.itbooklibrary.ui.bookdetail.intent.BookDetailEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailFragment : BaseFragment<FragmentBookdetailBinding>() {

    private val viewModel: BookDetailViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookdetailBinding =
        FragmentBookdetailBinding::inflate


    override fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) {
        initUi()
        initBinding()
        requireArguments().getString(isbn13Key)?.let { viewModel.start(it) }
    }

    override fun initUi() {

    }

    override fun initBinding() {
        lifecycleScope.launch {
            launch {
                viewModel.stateFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { state ->

                    }
            }
            launch {
                viewModel.eventFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { event ->
                        when (event) {
                            BookDetailEvent.NavToBack -> TODO()
                            is BookDetailEvent.ShowToast -> TODO()
                        }
                    }
            }
        }
    }

    companion object {
        const val isbn13Key = "isbn13"
        fun newInstance(isbn13: String) = BookDetailFragment().apply {
            arguments = bundleOf(
                isbn13Key to isbn13
            )
        }
    }

}