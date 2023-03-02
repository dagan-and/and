package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.CallModel
import com.daquv.hub.data.model.Entities
import com.daquv.hub.data.respository.Repository
import io.reactivex.rxjava3.core.Single

class GetCallNumberUseCase(private val repository: Repository) {
    operator fun invoke(
        sttWord: String,
        entities: ArrayList<Entities>
    ): Single<CallModel> {
        return repository.getCallNumberRepo(sttWord, entities[0].entity!!, entities[0].value!!)
    }
}