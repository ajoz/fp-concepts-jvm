package io.github.ajoz.capability.exposing

class BarUninitialized {
    fun initialize(): BarInitialized =
            BarInitialized()

    // other things that can be done with uninitialized Bar
}

class BarInitialized {
    fun release(): BarUninitialized =
            BarUninitialized()

    fun doSomething() {
    }
}