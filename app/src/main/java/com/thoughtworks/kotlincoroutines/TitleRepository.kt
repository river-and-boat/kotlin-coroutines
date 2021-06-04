package com.thoughtworks.kotlincoroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.thoughtworks.kotlincoroutines.util.FakeNetworkError
import com.thoughtworks.kotlincoroutines.util.FakeNetworkSuccess

class TitleRepository(
    private val network: MainNetwork,
    private val titleDao: TitleDao
) {
    val title: LiveData<String>
        get() = Transformations.map(titleDao.loadTitle()) { it?.title }

    fun refreshTitle(onStateChanged: TitleStateListener) {
        onStateChanged(RefreshState.Loading)
        val call = network.fetchNewWelcome()
        call.addOnResultListener { result ->
            when (result) {
                is FakeNetworkSuccess<String> -> {
                    BACKGROUND.submit {
                        titleDao.insertTitle(Title(result.data))
                    }
                    onStateChanged(RefreshState.Success)
                }
                is FakeNetworkError -> {
                    onStateChanged(RefreshState.Error(TitleRefreshError(result.error)))
                }
            }
        }
    }

    sealed class RefreshState {
        object Loading : RefreshState()

        object Success : RefreshState()

        class Error(val error: Throwable) : RefreshState()
    }
}

typealias TitleStateListener = (TitleRepository.RefreshState) -> Unit

class TitleRefreshError(cause: Throwable) : Throwable(cause.message, cause)