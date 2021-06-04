package com.thoughtworks.kotlincoroutines

import com.thoughtworks.kotlincoroutines.util.FAKE_RESULTS
import com.thoughtworks.kotlincoroutines.util.FakeNetworkCall
import com.thoughtworks.kotlincoroutines.util.fakeNetworkLibrary

interface MainNetwork {
    fun fetchNewWelcome(): FakeNetworkCall<String>
}

object MainNetworkImp: MainNetwork {
    override fun fetchNewWelcome(): FakeNetworkCall<String> = fakeNetworkLibrary(FAKE_RESULTS)
}