package com.sunguk.itbooklibrary.ui.search.item

data class SearchedBook(
    val title: String,// "MongoDB in Action, 2nd Edition"
    val subtitle: String, //"Covers MongoDB version 3.0",
    val isbn13: String,// "9781617291609",
    val price: String,// "$19.99"
    val imageUrl: String, // "https://itbook.store/img/books/9781617291609.png",
    val url: String,
)