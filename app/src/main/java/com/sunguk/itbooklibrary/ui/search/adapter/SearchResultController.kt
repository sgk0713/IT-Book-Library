package com.sunguk.itbooklibrary.ui.search.adapter


interface SearchResultController {
    fun onOpenLinkClicked(url: String)
    fun onItemClicked(isbn13: String)
    fun onLoadMoreClicked()
}