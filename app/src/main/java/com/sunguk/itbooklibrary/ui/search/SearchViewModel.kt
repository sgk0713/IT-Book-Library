package com.sunguk.itbooklibrary.ui.search

import com.sunguk.domain.usecase.CheckNetworkAvailability
import com.sunguk.domain.usecase.SearchBook
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.ui.base.BaseViewModel
import com.sunguk.itbooklibrary.ui.search.adapter.SearchResultController
import com.sunguk.itbooklibrary.ui.search.intent.SearchEvent
import com.sunguk.itbooklibrary.ui.search.intent.SearchState
import com.sunguk.itbooklibrary.ui.search.item.SearchResultBook
import com.sunguk.itbooklibrary.ui.search.item.SearchedBook
import com.sunguk.itbooklibrary.util.Pageable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBook: SearchBook,
    private val checkNetworkAvailability: CheckNetworkAvailability,
) : BaseViewModel<SearchState, SearchEvent>(SearchState()),
    SearchResultController,
    Pageable<SearchBook.Response> {

    private val loadMoreItem = SearchResultBook.LoadMore()

    override val itemsPerPage: Int = 10
    override val loadSuccessPageSet: HashSet<Int> = hashSetOf()

    fun start() {
        launch {
            sendEvent(SearchEvent.FillTextWith(state.lastInputText))
        }
    }

    override suspend fun onNewPageRequest(page: Int): Pageable.Result<SearchBook.Response> {
        runCatching {
            val keyword = state.searchKeyword
            searchBook(SearchBook.Request(page, keyword))
        }.fold(
            onSuccess = {
                return Pageable.Result.Success(it)
            },
            onFailure = {
                return Pageable.Result.Failure(page, it)
            }
        )
    }

    private fun loadInitPage(query: String) {
        launch {
            state.copy(
                searchKeyword = query,
            ).let {
                updateState(it)
            }
            loadSuccessPageSet.clear()
            loadSuccessPageSet.add(1)
            loadPage(1)
        }
    }

    private suspend fun loadPage(page: Int) {
        state.copy(
            isSearchingBook = true,
            showNoResult = false,
            isInitialized = true,
        ).let {
            updateState(it)
        }
        when (val result = onNewPageRequest(page)) {
            is Pageable.Result.Success -> {
                resultCallback.invoke(result)
            }

            is Pageable.Result.Failure -> {
                result.throwable.printStackTrace()
                loadSuccessPageSet.remove(result.requestedPage)
                resultCallback.invoke(result)

            }
        }
    }


    fun updateKeyword(text: String) {
        launch {
            if (text.isBlank() && state.lastInputText != text) {
                sendEvent(SearchEvent.FillTextWith(text))
            }
            state.copy(
                lastInputText = text,
                isClearButtonVisible = text.isNotBlank()
            ).let {
                updateState(it)
            }
        }
    }

    fun onActionSearch(query: String) {
        if (query.isNotBlank()) {
            updateKeyword(query)
            launch {
                state.copy(
                    searchedBook = emptySet(),
                    showNoResult = false,
                ).let {
                    updateState(it)
                }
            }
            displayViewBlocker(false)
            loadInitPage(query)
        } else {
            launch {
                sendEvent(SearchEvent.ShowToast(R.string.keyword_blank_error))
            }
        }
    }

    fun displayViewBlocker(visible: Boolean) {
        launch {
            state.copy(
                isViewBlockerVisible = visible
            ).let {
                updateState(it)
            }
            if (visible.not()) {
                sendEvent(SearchEvent.HideKeyboard)
            }
        }
    }

    override fun onLoadMoreClicked() {
        launch {
            state.copy(
                searchedBook = state.searchedBook - loadMoreItem,
            ).let {
                updateState(it)
            }
            loadPage(loadMoreItem.lastRequestedPage)
        }
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

    override val resultCallback: (Pageable.Result<SearchBook.Response>) -> Unit = {
        launch {
            when (it) {
                is Pageable.Result.Success -> {
                    state.copy(
                        searchedBook = state.searchedBook + it.result.list.mapIndexed { index, book ->
                            SearchResultBook.Result(
                                orderIndex = (it.result.requestedPage - 1) * itemsPerPage + index,
                                book = SearchedBook(
                                    book.title,
                                    book.subtitle,
                                    book.isbn13,
                                    book.price,
                                    book.imageUrl,
                                    book.url
                                )
                            )
                        },
                        isSearchingBook = false,
                        showNoResult = state.searchedBook.isEmpty() && it.result.list.isEmpty(),
                    ).let {
                        updateState(it)
                    }
                }
                is Pageable.Result.Failure -> {
                    if (checkNetworkAvailability.invoke(Unit).not()) {
                        sendEvent(SearchEvent.ShowToast(R.string.network_error))
                    }
                    state.copy(
                        searchedBook = state.searchedBook + loadMoreItem.apply {
                            lastRequestedPage = it.requestedPage
                        },
                        isSearchingBook = false,
                    ).let {
                        updateState(it)
                    }
                }
            }
        }
    }

    override fun getPageByPosition(itemPosition: Int): Int {
        return (itemPosition / itemsPerPage) + 1
    }
}