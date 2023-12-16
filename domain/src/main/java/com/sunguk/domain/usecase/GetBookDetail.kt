package com.sunguk.domain.usecase

import com.sunguk.domain.entity.BookDetail
import com.sunguk.domain.repository.BookRepository
import com.sunguk.domain.usecase.base.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * @param isbn13 ISBN(International Standard Book Number).
 * It must be a 13-digit number, and should not include any other characters, such as hyphens (-).
 */
class GetBookDetail @Inject constructor(
    private val bookRepository: BookRepository,
) : CoroutineUseCase<String, BookDetail> {

    override suspend fun invoke(parameter: String): BookDetail = withContext(Dispatchers.Default) {
        require(parameter.matches(Regex("\\d{13}"))) {
            "Incorrect ISBN13"
        }
        bookRepository.getBookDetails(parameter)
    }

}