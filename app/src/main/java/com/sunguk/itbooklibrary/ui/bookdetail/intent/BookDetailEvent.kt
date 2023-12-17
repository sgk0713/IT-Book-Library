package com.sunguk.itbooklibrary.ui.bookdetail.intent

sealed interface BookDetailEvent {
    object NavToBack : BookDetailEvent
    class ShowToast(val message: String) : BookDetailEvent
}