package com.sunguk.itbooklibrary.ui.search.intent

sealed interface SearchEvent {
    class OpenWebLink(val url: String) : SearchEvent
    class NavToDetail(val isbn13: String) : SearchEvent
    class ShowToast(val messageRes: Int) : SearchEvent
    object HideKeyboard : SearchEvent
    class FillTextWith(val text: String) : SearchEvent
}