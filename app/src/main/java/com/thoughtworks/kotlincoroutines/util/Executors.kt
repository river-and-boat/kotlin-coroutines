package com.thoughtworks.kotlincoroutines

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val BACKGROUND: ExecutorService = Executors.newFixedThreadPool(2)