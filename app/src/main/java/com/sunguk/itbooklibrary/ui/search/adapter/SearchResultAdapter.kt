package com.sunguk.itbooklibrary.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.databinding.ItemSeachResultBinding
import com.sunguk.itbooklibrary.databinding.ItemSearchLoadMoreBinding
import com.sunguk.itbooklibrary.ui.search.item.SearchResultBook
import com.sunguk.itbooklibrary.util.createView

class SearchResultAdapter constructor(
    private val controller: SearchResultController,
) : RecyclerView.Adapter<SearchResultViewHolder>() {

    private val books = SortedList(
        SearchResultBook::class.java,
        object : SortedListAdapterCallback<SearchResultBook>(this) {
            override fun compare(o1: SearchResultBook, o2: SearchResultBook): Int =
                o1.orderIndex.compareTo(o2.orderIndex)

            override fun areContentsTheSame(
                oldItem: SearchResultBook,
                newItem: SearchResultBook,
            ): Boolean = (oldItem == newItem)

            override fun areItemsTheSame(
                item1: SearchResultBook,
                item2: SearchResultBook,
            ): Boolean = (item1 == item2)
        }
    )

    fun replaceItems(items: Set<SearchResultBook>) {
        books.replaceAll(items)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.onBind(books.get(position), controller)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchResultViewHolder {
        return when (viewType) {
            SearchResultBook.ViewType.RESULT.ordinal -> {
                val view = parent.createView(R.layout.item_seach_result)
                ResultViewHolder(ItemSeachResultBinding.bind(view))
            }
            SearchResultBook.ViewType.LOAD_MORE.ordinal -> {
                val view = parent.createView(R.layout.item_search_load_more)
                LoadingViewHolder(ItemSearchLoadMoreBinding.bind(view))
            }
            else -> throw IllegalArgumentException()

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (books.get(position)) {
            is SearchResultBook.Result -> SearchResultBook.ViewType.RESULT.ordinal
            is SearchResultBook.LoadMore -> SearchResultBook.ViewType.LOAD_MORE.ordinal
        }
    }

    override fun getItemCount(): Int {
        return books.size()
    }
}