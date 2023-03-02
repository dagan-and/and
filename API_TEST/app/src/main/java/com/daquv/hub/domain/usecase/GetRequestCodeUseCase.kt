package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.ArgumentModel
import com.daquv.hub.data.model.RequestCodeModel
import com.daquv.hub.data.model.TTSTResponse
import com.daquv.hub.data.respository.Repository
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class GetRequestCodeUseCase(private val repository: Repository) {
    operator fun invoke(url : String, param: JsonObject): Single<RequestCodeModel> {
        return repository.getRequestCode(url, param)
    }

}