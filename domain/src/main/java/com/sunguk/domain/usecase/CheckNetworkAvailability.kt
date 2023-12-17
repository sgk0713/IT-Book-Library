package com.sunguk.domain.usecase

import com.sunguk.domain.repository.NetworkStateRepository
import com.sunguk.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class CheckNetworkAvailability @Inject constructor(
    private val networkStateRepository: NetworkStateRepository,
) : CoroutineUseCase<Unit, Boolean> {
    override suspend fun invoke(parameter: Unit): Boolean {
        return networkStateRepository.isNetworkConnectionAvailable()
    }
}