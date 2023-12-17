package com.sunguk.data.api

import com.sunguk.data.dto.BookDetailData
import com.sunguk.data.dto.SearchBookResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ItBookApi {

    @GET("search/{query}/{page}")
    suspend fun searchBook(
        @Path("query") searchWord: String,
        @Path("page") page: Int,
    ): SearchBookResponse


    @GET("books/{isbn13}")
    suspend fun getBookDetail(
        @Path("isbn13") isbn13: String,
    ): BookDetailData

}