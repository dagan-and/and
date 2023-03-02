package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.RsaKeyModel
import com.daquv.hub.data.respository.Repository
import io.reactivex.rxjava3.core.Single

class GetRSAKeyUseCase(private val repository: Repository) {
    operator fun invoke(): Single<RsaKeyModel> {
        return repository.getRSAKeyRepo()
    }
}