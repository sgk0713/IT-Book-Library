package com.sunguk.itbooklibrary.ui.bookdetail.intent

sealed interface BookDetailEvent {
    object NavToBack : BookDetailEvent
    class ShowToast(val message: String) : BookDetailEvent
    class OpenWebLink(val url: String) : BookDetailEvent
}