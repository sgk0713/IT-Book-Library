package com.sunguk.itbooklibrary.ui.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.databinding.FragmentSearchBinding
import com.sunguk.itbooklibrary.ui.base.BaseFragment
import com.sunguk.itbooklibrary.ui.search.adapter.SearchResultAdapter
import com.sunguk.itbooklibrary.ui.search.intent.SearchEvent
import com.sunguk.itbooklibrary.util.Pager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        FragmentSearchBinding::inflate

    private val searchResultAdapter by lazy { SearchResultAdapter(viewModel) }

    override fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) {
        initUi()
        initBinding()
        viewModel.start()
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
            clearButton.setOnClickListener {
                viewModel.updateKeyword("")
            }
            searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateKeyword(text?.toString() ?: "")
            }
            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    viewModel.displayViewBlocker(true)
                }
            }
            searchEditText.setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.onActionSearch(v.text.toString())
                        true
                    }
                    else -> false
                }
            }
            viewBlocker.setOnClickListener {
                viewModel.displayViewBlocker(false)
            }
        }
    }

    override fun initBinding() {
        lifecycleScope.launch {
            launch {
                viewModel.stateFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { state ->
                        searchResultAdapter.replaceItems(state.searchedBook)
                        binding.viewBlocker.isVisible = state.isViewBlockerVisible
                        binding.clearButton.isVisible = state.isClearButtonVisible
                        binding.contentProgressBar.isVisible = state.isSearchingBook
                        binding.emptyResultView.isVisible = state.showNoResult
                        binding.emptyResultText.text =
                            getString(R.string.no_result_keyword, state.searchKeyword)
                    }
            }
            launch {
                viewModel.eventFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { event ->
                        when (event) {
                            is SearchEvent.OpenWebLink -> {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                                startActivity(intent)
                            }
                            is SearchEvent.NavToDetail -> {
                                // TODO: navigate to detail
                                Toast.makeText(
                                    requireContext(),
                                    "navToDetail : ${event.isbn13}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is SearchEvent.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
                                    event.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            SearchEvent.HideKeyboard -> {
                                hideKeyboard()
                                binding.searchEditText.clearFocus()
                            }
                            is SearchEvent.FillTextWith -> {
                                binding.searchEditText.setText(event.text)
                            }
                        }
                    }
            }
        }
    }

    private fun hideKeyboard() {
        val service = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE)
        val inputMethodManager = service as? InputMethodManager
        view?.run {
            inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

}