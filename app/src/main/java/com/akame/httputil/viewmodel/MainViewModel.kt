package com.akame.httputil.viewmodel

import androidx.lifecycle.*
import com.akame.httputil.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun getBanner() = repository.loadMainData1({
    }, {

    })

    fun getBannerList() = repository.loadMain2()

    fun getBannerListNoResult() {
        viewModelScope.launch {
            repository.loadMain3()
        }
    }
}

class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = MainViewModel(repository)
        return viewModel as T
    }
}
