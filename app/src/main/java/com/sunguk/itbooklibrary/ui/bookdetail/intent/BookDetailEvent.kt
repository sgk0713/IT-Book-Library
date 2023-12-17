package com.sunguk.itbooklibrary.ui.bookdetail.intent

sealed interface BookDetailEvent {
    object NavToBack : BookDetailEvent
    class ShowToast(val messageRes: Int) : BookDetailEvent
    class OpenWebLink(val url: String) : BookDetailEvent
}