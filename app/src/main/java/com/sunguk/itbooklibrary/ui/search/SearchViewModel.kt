package com.sunguk.itbooklibrary.ui.search

import com.sunguk.domain.usecase.SearchBook
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
) : BaseViewModel<SearchState, SearchEvent>(SearchState()),
    SearchResultController,
    Pageable<SearchBook.Response> {

    private val loadMoreItem = SearchResultBook.LoadMore()

    override val itemsPerPage: Int = 20
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
                isSearchingBook = true,
                showNoResult = false,
                searchKeyword = query,
            ).let {
                updateState(it)
            }
            loadSuccessPageSet.clear()
            loadSuccessPageSet.add(1)
            when (val result = onNewPageRequest(1)) {
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
    }


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
                    sendEvent(
                        SearchEvent.ShowToast(
                            message = it.throwable.message ?: ""
                        )
                    )
                    state.copy(
                        searchedBook = state.searchedBook + loadMoreItem,
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