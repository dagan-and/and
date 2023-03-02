package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.ArgumentModel
import com.daquv.hub.data.model.TTSTResponse
import com.daquv.hub.data.model.VerifyCodeModel
import com.daquv.hub.data.respository.Repository
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single

class GetVerifyCodeUseCase(private val repository: Repository) {
    operator fun invoke(url : String, param: JsonObject): Single<VerifyCodeModel> {
        return repository.getVerifyCode(url, param)
    }

}