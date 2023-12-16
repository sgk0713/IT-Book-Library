package com.sunguk.domain.repository

import com.sunguk.domain.entity.Book
import com.sunguk.domain.entity.BookDetail

interface BookRepository {
    suspend fun getSearchedBooks(
        keyword: String,
        page: Int,
    ): List<Book>

    suspend fun getBookDetails(
        isbn13: String,
    ): BookDetail
}