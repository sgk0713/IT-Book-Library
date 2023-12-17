package com.sunguk.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.sunguk.domain.entity.BookDetail
import com.sunguk.domain.entity.PdfInfo

@JsonClass(generateAdapter = true)
data class BookDetailData(
    @Json(name = "error") val error: String,                  // "0"
    @Json(name = "title") val title: String,                  // "Securing DevOps"
    @Json(name = "subtitle") val subtitle: String,               // "Security in the Cloud"
    @Json(name = "authors") val authors: String,                // "Julien Vehent"
    @Json(name = "publisher") val publisher: String,              // "Manning"
    @Json(name = "language") val language: String,               // "English"
    @Json(name = "isbn10") val isbn10: String,                 // "1617294136"
    @Json(name = "isbn13") val isbn13: String,                 // "9781617294136"
    @Json(name = "pages") val pages: String,                  // "384"
    @Json(name = "year") val year: String,                   // "2018"
    @Json(name = "rating") val rating: String,                 // "4"
    @Json(name = "desc") val desc: String,                   // "An application running in the cloud can benefit from incredible efficiencies, but they come with unique security threats too. A DevOps team's highest priority is understanding those risks and hardening the system against them.Securing DevOps teaches you the essential techniques to secure your c..."
    @Json(name = "price") val price: String,                  // "$39.65"
    @Json(name = "image") val image: String,                  // "https://itbook.store/img/books/9781617294136.png"
    @Json(name = "url") val url: String,                    // "https://itbook.store/books/9781617294136"
    @Json(name = "pdf") val pdf: Map<String, String>?,        // {"Chapter 2": "https://itbook.store/files/9781617294136/chapter2.pdf", "Chapter 5": "https://itbook.store/files/9781617294136/chapter5.pdf"}
)

fun BookDetailData.toEntity() = BookDetail(
    title = title,
    subtitle = subtitle,
    authors = authors,
    publisher = publisher,
    language = language,
    isbn10 = isbn10,
    isbn13 = isbn13,
    pages = pages,
    year = year,
    rating = rating,
    desc = desc,
    price = price,
    imageUrl = image,
    detailPageUrl = url,
    pdfs = pdf?.map {
        PdfInfo(
            it.key,
            it.value
        )
    } ?: emptyList()
)
