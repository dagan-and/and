package com.daquv.hub.domain.usecase

import com.daquv.hub.data.model.LoginTokenModel
import com.daquv.hub.data.request.REQ_SVC_LOGIN
import com.daquv.hub.data.respository.Repository
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class GetLoginTokenUseCase(private val repository: Repository) {
    operator fun invoke(id : String, pwd : String, company : String): Single<Response<LoginTokenModel>> {
        val reqData = REQ_SVC_LOGIN(id,pwd,company)
        return repository.getLoginTokenRepo(reqData.getGsonObject())

    }
}