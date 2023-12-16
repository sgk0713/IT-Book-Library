package com.sunguk.domain.usecase.base

interface CoroutineUseCase<in P, R> {
    suspend operator fun invoke(parameter: P): R
}