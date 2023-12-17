package com.sunguk.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchBookResponse(
    @Json(name = "error") val error: String?,          // "0"
    @Json(name = "total") val total: String?,          // "333"
    @Json(name = "page") val page: String?,           // "1"
    @Json(name = "books") val books: List<BookData>,
)
