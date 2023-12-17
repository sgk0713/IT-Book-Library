package com.sunguk.itbooklibrary.ui.search.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.databinding.ItemSeachResultBinding
import com.sunguk.itbooklibrary.databinding.ItemSearchLoadMoreBinding
import com.sunguk.itbooklibrary.ui.search.item.SearchResultBook

abstract class SearchResultViewHolder(
    binding: ViewBinding,
) : ViewHolder(binding.root) {
    abstract fun onBind(result: SearchResultBook, controller: SearchResultController)
}

class ResultViewHolder(
    private val binding: ItemSeachResultBinding,
) : SearchResultViewHolder(binding) {

    override fun onBind(result: SearchResultBook, controller: SearchResultController) {
        require(result is SearchResultBook.Result)
        with(binding) {
            Glide.with(imageView)
                .load(result.book.imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
            title.text = result.book.title
            subtitle.text = result.book.subtitle
            subtitle.isVisible = result.book.subtitle.isNotBlank()
            price.text = result.book.price
            isbn13.text = itemView.resources.getString(R.string.isbn_13_info, result.book.isbn13)
            openLinkButton.setOnClickListener {
                controller.onOpenLinkClicked(result.book.url)
            }
            itemView.setOnClickListener {
                controller.onItemClicked(result.book.isbn13)
            }
        }
    }
}

class LoadingViewHolder(
    private val binding: ItemSearchLoadMoreBinding,
) : SearchResultViewHolder(binding) {
    override fun onBind(result: SearchResultBook, controller: SearchResultController) {
        with(binding) {
            loadMoreButton.setOnClickListener {
                controller.onLoadMoreClicked()
            }
        }
    }
}