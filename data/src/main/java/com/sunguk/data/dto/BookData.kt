package com.sunguk.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.sunguk.domain.entity.Book

@JsonClass(generateAdapter = true)
data class BookData(
    @Json(name = "title") val title: String,                  // "MongoDB in Action, 2nd Edition"
    @Json(name = "subtitle") val subtitle: String,              // "Covers MongoDB version 3.0"
    @Json(name = "isbn13") val isbn13: String,                 // "9781617291609"
    @Json(name = "price") val price: String,                 // "$19.99"
    @Json(name = "image") val image: String,                 // "https://itbook.store/img/books/9781617291609.png"
    @Json(name = "url") val url: String,                    // "https://itbook.store/books/9781617291609"
)

fun BookData.toEntity(): Book = Book(
    title = title,
    subtitle = subtitle,
    isbn13 = isbn13,
    price = price,
    imageUrl = image,
    url = url
)