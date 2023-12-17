package com.sunguk.data.repository

import com.sunguk.data.api.ItBookApi
import com.sunguk.data.dto.toEntity
import com.sunguk.domain.entity.Book
import com.sunguk.domain.entity.BookDetail
import com.sunguk.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val itBookApi: ItBookApi,
) : BookRepository {

    // itbookapi gives 10 items per page
    override suspend fun getSearchedBooks(keyword: String, page: Int): List<Book> {
        return itBookApi.searchBook(keyword, page).books.map {
            it.toEntity()
        }
    }

    override suspend fun getBookDetails(isbn13: String): BookDetail {
        return itBookApi.getBookDetail(isbn13).toEntity()
    }
}