package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.STTResultModel
import com.daquv.hub.data.request.REQ_SVC_STT
import com.daquv.hub.data.respository.Repository

import io.reactivex.rxjava3.core.Single

class GetSTTResultUseCase (private val repository: Repository) {
    operator fun invoke(sttWord : String): Single<STTResultModel> {
        val reqData = REQ_SVC_STT(sttWord)
        return repository.getSttResultRepo(reqData.gsonObject)
    }
}