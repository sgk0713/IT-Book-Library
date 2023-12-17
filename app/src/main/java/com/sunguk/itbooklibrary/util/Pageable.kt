package com.sunguk.itbooklibrary.util

interface Pageable<T> {
    val itemsPerPage: Int
    val resultCallback: ((Result<T>) -> Unit)
    val loadSuccessPageSet: HashSet<Int>

    suspend fun onNewPageRequest(page: Int): Result<T>

    fun getPageByPosition(itemPosition: Int): Int

    sealed class Result<out T> {
        class Success<T>(val result: T) : Result<T>()
        class Failure<T>(val requestedPage: Int, val throwable: Throwable) : Result<T>()
    }
}
