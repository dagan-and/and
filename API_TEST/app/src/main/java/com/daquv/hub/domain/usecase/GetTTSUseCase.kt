package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.TTSTResponse
import com.daquv.hub.data.respository.Repository
import io.reactivex.rxjava3.core.Single

class GetTTSUseCase(private val repository: Repository) {
    operator fun invoke(keyword : String): Single<TTSTResponse> {
        return repository.getTTSRepo(keyword)
    }

}