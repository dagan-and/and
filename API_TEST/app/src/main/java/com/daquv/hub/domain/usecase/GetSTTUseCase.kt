package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.ArgumentModel
import com.daquv.hub.data.model.STTDataModel
import com.daquv.hub.data.model.TTSTResponse
import com.daquv.hub.data.respository.Repository
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class GetSTTUseCase(private val repository: Repository) {
    operator fun invoke(url : String, param: JsonObject): Single<STTDataModel> {
        return repository.getSTT(url, param)
    }

}