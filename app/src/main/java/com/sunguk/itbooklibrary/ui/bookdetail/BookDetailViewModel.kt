package com.sunguk.itbooklibrary.ui.bookdetail

import com.sunguk.domain.usecase.GetBookDetail
import com.sunguk.itbooklibrary.ui.base.BaseViewModel
import com.sunguk.itbooklibrary.ui.bookdetail.adapter.BookDetailController
import com.sunguk.itbooklibrary.ui.bookdetail.intent.BookDetailEvent
import com.sunguk.itbooklibrary.ui.bookdetail.intent.BookDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookDetail: GetBookDetail,
) : BaseViewModel<BookDetailState, BookDetailEvent>(BookDetailState()), BookDetailController {

    fun start(isbn13: String) {
        if (state.isInitialized.not()) {
            launch {
                state.copy(
                    isbn13 = isbn13,
                ).let {
                    updateState(it)
                }
                loadDetail(isbn13)
            }
        }
    }

    private suspend fun loadDetail(isbn13: String) {
        runCatching {
            state.copy(
                isLoadingContent = true
            ).let {
                updateState(it)
            }
            getBookDetail.invoke(isbn13)
        }.onSuccess {
            state.copy(
                book = it,
                isLoadingContent = false,
            ).let {
                updateState(it)
            }
        }.onFailure {
            sendEvent(
                BookDetailEvent.ShowToast("Error Occurred")
            )
            state.copy(
                showRefreshButton = true,
                isLoadingContent = false,
            ).let {
                updateState(it)
            }
        }
    }

    fun reload() {
        launch {
            state.copy(
                showRefreshButton = false
            ).let {
                updateState(it)
            }
            loadDetail(state.isbn13)
        }
    }

    fun navToBack() {
        launch {
            sendEvent(
                BookDetailEvent.NavToBack
            )
        }
    }

    fun openDetailLink() {
        launch {
            state.book?.detailPageUrl?.let {
                sendEvent(BookDetailEvent.OpenWebLink(it))
            }
        }
    }

    override fun onPdfLinkClicked(url: String) {
        launch {
            sendEvent(BookDetailEvent.OpenWebLink(url))
        }
    }
}