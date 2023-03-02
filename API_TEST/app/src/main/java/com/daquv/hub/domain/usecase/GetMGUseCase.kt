package com.daquv.hub.domain.usecase

import com.daquv.hub.data.respository.Repository
import retrofit2.Call

class GetMGUseCase(private val repository: Repository) {
    operator fun invoke(url : String): Call<String> {
        return repository.getMgFileRepo(url)
    }

}