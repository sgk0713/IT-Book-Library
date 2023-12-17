package com.sunguk.itbooklibrary.ui.bookdetail.intent

import com.sunguk.domain.entity.BookDetail

data class BookDetailState(
    val book: BookDetail? = null,
    val isbn13: String = "",
    val isInitialized: Boolean = false,
    val isLoadingContent: Boolean = false,
    val showRefreshButton: Boolean = false,
)