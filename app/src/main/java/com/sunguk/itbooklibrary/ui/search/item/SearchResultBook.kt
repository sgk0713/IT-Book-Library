package com.sunguk.itbooklibrary.ui.search.item

sealed class SearchResultBook(
    open val orderIndex: Int,
) {
    enum class ViewType {
        RESULT, LOAD_MORE;
    }

    data class Result(
        override val orderIndex: Int,
        val book: SearchedBook,
    ) : SearchResultBook(orderIndex)

    class LoadMore(
        override val orderIndex: Int = Int.MAX_VALUE,
        var lastRequestedPage: Int = 1,
    ) : SearchResultBook(orderIndex)
}
