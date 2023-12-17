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

    data class LoadMore(
        override val orderIndex: Int = Int.MAX_VALUE,
    ) : SearchResultBook(orderIndex)
}
