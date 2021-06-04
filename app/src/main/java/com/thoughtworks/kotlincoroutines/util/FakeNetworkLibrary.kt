package com.thoughtworks.kotlincoroutines.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.*
import java.util.concurrent.Executors

private const val ONE_SECOND = 1000L
private const val ERROR_RATE = 0.3

private val executor = Executors.newCachedThreadPool()
private val uiHandler = Handler(Looper.getMainLooper())

fun fakeNetworkLibrary(from: List<String>): FakeNetworkCall<String> {
    assert(from.isNotEmpty()) { "You must pass at least one result string" }
    val result = FakeNetworkCall<String>()
    Log.d("FAKE HTTP", "before submit http")
    executor.submit {
        Thread.sleep(ONE_SECOND)
        if (shouldThrowException()) {
            result.onError(FakeNetworkException("Error contacting the network"))
        } else {
            result.onSuccess(from[Random().nextInt(from.size)])
        }
        Log.d("FAKE HTTP", "finish http")
    }
    Log.d("FAKE HTTP", "async submit http")
    return result
}

fun shouldThrowException(): Boolean {
    return Random().nextFloat() < ERROR_RATE
}

class FakeNetworkCall<T> {
    private var result: FakeNetworkResult<T>? = null
    private val listeners = mutableListOf<FakeNetworkListener<T>>()

    fun addOnResultListener(listener: FakeNetworkListener<T>) {
        trySendResult(listener)
        listeners += listener
    }

    fun onSuccess(data: T) {
        result = FakeNetworkSuccess(data)
        sendResultToAllListeners()
    }

    fun onError(throwable: Throwable) {
        result = FakeNetworkError(throwable)
        sendResultToAllListeners()
    }

    private fun sendResultToAllListeners() = listeners.map { trySendResult(it) }

    private fun trySendResult(listener: FakeNetworkListener<T>) {
        val thisResult = result
        thisResult?.let {
            uiHandler.post {
                listener(thisResult)
            }
        }
    }
}

// success result
sealed class FakeNetworkResult<T>
class FakeNetworkSuccess<T>(val data: T) : FakeNetworkResult<T>()
class FakeNetworkError<T>(val error: Throwable) : FakeNetworkResult<T>()
class FakeNetworkException(message: String) : Throwable(message)

// listener, 匿名函数别名
typealias FakeNetworkListener<T> = (FakeNetworkResult<T>) -> Unit


