package com.sunguk.itbooklibrary.ui.search

import com.sunguk.itbooklibrary.ui.base.BaseViewModel
import com.sunguk.itbooklibrary.ui.search.adapter.SearchResultController
import com.sunguk.itbooklibrary.ui.search.intent.SearchEvent
import com.sunguk.itbooklibrary.ui.search.intent.SearchState
import com.sunguk.itbooklibrary.ui.search.item.SearchResultBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
) : BaseViewModel<SearchState, SearchEvent>(SearchState()), SearchResultController {

    private val loadMoreItem = SearchResultBook.LoadMore()


    override fun onLoadMoreClicked() {
        launch {
            state.copy(
                searchedBook = state.searchedBook - loadMoreItem
            ).let {
                updateState(it)
            }
        }
        // TODO: call search again
    }

    override fun onOpenLinkClicked(url: String) {
        launch {
            sendEvent(SearchEvent.OpenWebLink(url))
        }
    }

    override fun onItemClicked(isbn13: String) {
        launch {
            sendEvent(SearchEvent.NavToDetail(isbn13))
        }
    }
}