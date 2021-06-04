package com.thoughtworks.kotlincoroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _snackBar = MutableLiveData<String>()

    val snackbar: LiveData<String>
        get() = _snackBar

    fun onMainViewClicked() {
        BACKGROUND.submit {
            Thread.sleep(1000)
            _snackBar.postValue("Hello, from threads!")
        }
    }

    fun onSnackbarShown() {
        _snackBar.value = null
    }
}