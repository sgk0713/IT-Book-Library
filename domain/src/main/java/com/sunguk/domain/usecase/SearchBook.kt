package com.sunguk.domain.usecase

import com.sunguk.domain.entity.Book
import com.sunguk.domain.repository.BookRepository
import com.sunguk.domain.usecase.base.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * There are 2 types of operator.
 *
 * ## 1. `|(or)`
 * Search for results related to X or Y. It will work with very first operator, and the others will be ignored.
 * - example)
 *      - "android|java" : search "android" and "java"
 *      - "android|java|kotlin" : search "android" and "java|kotlin"
 *
 * ## 2. `-(hyphen)`
 * Search for results that don’t mention a word or phrase.
 *
 * - example)
 *      - `"android-game"` : search "android" not including "game"
 *
 * You can also use both operators all together.
 * - example)
 *      - "android-game|apple-watch"  : this gives you results of "android" not mentioning "game", or results of "apple" not mentioning "watch"
 */
class SearchBook @Inject constructor(
    private val bookRepository: BookRepository,
) : CoroutineUseCase<SearchBook.Request, SearchBook.Response> {

    private val maxSearchWords = 2

    override suspend fun invoke(parameter: Request): Response = runCatching {
        withContext(Dispatchers.Default) {
            val pageNumber = parameter.page
            val originalInput = parameter.keyword

            val uniqueQueries = originalInput.split("|", limit = maxSearchWords)
                .map {
                    val splits = it.split("-", limit = 2).map(String::trim)

                    val searchKeyword = splits.getOrElse(0) { "" }
                    val excludedKeyword = splits.getOrElse(1) { "" }

                    searchKeyword to excludedKeyword
                }.toSet()

            uniqueQueries
                .filter { (searchKeyword, _) -> searchKeyword.isNotBlank() }
                .map { (searchKeyword, excludedKeywords) ->
                    async {
                        bookRepository.getSearchedBooks(
                            keyword = searchKeyword,
                            page = pageNumber
                        ).filterNot { book ->
                            excludedKeywords.isNotBlank() && book.title.contains(
                                excludedKeywords,
                                ignoreCase = true
                            )
                        }
                    }
                }.map { it.await() }
                .flatten()
                .distinctBy {
                    it.isbn13
                }
        }
    }.fold(
        onSuccess = {
            Response.Success(
                searchedKeyword = parameter.keyword,
                list = it,
                requestedPage = parameter.page
            )
        },
        onFailure = {
            Response.Failure(it)
        }
    )


    class Request(
        val page: Int,
        val keyword: String,
    )

    sealed class Response {
        class Failure(
            val throwable: Throwable,
        ) : Response()

        class Success(
            val searchedKeyword: String,
            val list: List<Book>,
            val requestedPage: Int,
        ) : Response()
    }
}