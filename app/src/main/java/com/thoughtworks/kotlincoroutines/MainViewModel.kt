package com.thoughtworks.kotlincoroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoughtworks.kotlincoroutines.util.singleArgViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel(private val repository: TitleRepository) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }

    private val _snackBar = MutableLiveData<String>()

    val snackbar: LiveData<String>
        get() = _snackBar

    val title = repository.title

    private val _spinner = MutableLiveData<Boolean>()

    val spinner: LiveData<Boolean>
        get() = _spinner

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onMainViewClicked() {
        refreshTitle()
    }

    fun onSnackbarShown() {
        _snackBar.value = null
    }

    fun refreshTitle() {
        repository.refreshTitle { state ->
            when(state) {
                is TitleRepository.RefreshState.Loading -> _spinner.postValue(true)
                is TitleRepository.RefreshState.Success -> _spinner.postValue(false)
                is TitleRepository.RefreshState.Error -> {
                    _spinner.postValue(false)
                    _snackBar.postValue(state.error.message)
                }
            }
        }
    }
}