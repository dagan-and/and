package com.daquv.hub.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daquv.hub.domain.usecase.GetTTSUseCase

class MainViewModelFactory (
    private val getTTSUseCase: GetTTSUseCase
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(getTTSUseCase) as T
    }
}