package com.sunguk.itbooklibrary.ui.bookdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.databinding.FragmentBookdetailBinding
import com.sunguk.itbooklibrary.ui.base.BaseFragment
import com.sunguk.itbooklibrary.ui.bookdetail.adapter.PDFsAdapter
import com.sunguk.itbooklibrary.ui.bookdetail.intent.BookDetailEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailFragment : BaseFragment<FragmentBookdetailBinding>() {

    private val viewModel: BookDetailViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookdetailBinding =
        FragmentBookdetailBinding::inflate

    private val pdfsAdapter by lazy { PDFsAdapter(viewModel) }


    override fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) {
        initUi()
        initBinding()
        requireArguments().getString(isbn13Key)?.let { viewModel.start(it) }
    }

    override fun initUi() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navToBack()
        }
        binding.openLinkButton.setOnClickListener {
            viewModel.openDetailLink()
        }
        binding.pdfRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = pdfsAdapter
            itemAnimator = null
        }
        binding.refreshButton.setOnClickListener {
            viewModel.reload()
        }
    }

    override fun initBinding() {
        lifecycleScope.launch {
            launch {
                viewModel.stateFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { state ->
                        state.book?.run {
                            Glide.with(binding.imageView)
                                .load(imageUrl)
                                .error(R.drawable.ic_launcher_foreground)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.imageView)
                            binding.publisherGroup.isVisible = publisher.isNotBlank()
                            binding.authorGroup.isVisible = authors.isNotBlank()
                            binding.languageGroup.isVisible = language.isNotBlank()
                            binding.descGroup.isVisible = desc.isNotBlank()
                            binding.subtitle.isVisible = subtitle.isNotBlank()
                            binding.pdfGroup.isVisible = pdfs.isNotEmpty()
                            pdfsAdapter.replaceItems(pdfs)

                            binding.title.text = title
                            binding.subtitle.text = subtitle
                            binding.authors.text = authors
                            binding.publisher.text = publisher
                            binding.language.text = language
                            binding.isbn13.text = getString(R.string.isbn_13_info, isbn13)
                            binding.isbn10.text = getString(R.string.isbn_10_info, isbn10)
                            binding.pagesValue.text = pages
                            binding.yearValue.text = year
                            binding.ratingValue.text = rating
                            binding.desc.text = desc
                            binding.price.text = price
                        }
                        binding.loadingView.isVisible = state.isLoadingContent
                        binding.refreshView.isVisible = state.showRefreshButton
                    }
            }
            launch {
                viewModel.eventFlow
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                    .collect { event ->
                        when (event) {
                            BookDetailEvent.NavToBack -> {
                                findNavController().popBackStack()
                            }
                            is BookDetailEvent.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
                                    event.messageRes,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is BookDetailEvent.OpenWebLink -> {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                                startActivity(intent)
                            }
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