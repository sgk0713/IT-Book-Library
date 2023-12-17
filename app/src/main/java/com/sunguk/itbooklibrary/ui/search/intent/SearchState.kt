package com.sunguk.itbooklibrary.ui.search.intent

import com.sunguk.itbooklibrary.ui.search.item.SearchResultBook

data class SearchState(
    val searchedBook: Set<SearchResultBook> = emptySet(),
    val isSearchingBook: Boolean = false,
    val isClearButtonVisible: Boolean = false,
    val isViewBlockerVisible: Boolean = false,
    val lastInputText: String = "",
    val showNoResult: Boolean = false,
    val noResultKeyword: String = "",
)