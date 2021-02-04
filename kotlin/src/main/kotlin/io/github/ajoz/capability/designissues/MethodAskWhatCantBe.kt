package io.github.ajoz.capability.designissues

class MaybeABetterFoo {
    var initialized = false
        private set

    fun initialize(): Boolean {
        if (!initialized) {
            // do something
        }
        return initialized
    }

    fun release(): Boolean {
        if (initialized) {
            // do something
        }
        return initialized
    }
    // other important methods here ...
    fun canDoSth(): Boolean =
            TODO("A very important logic here!")

    fun doSth() {}
}

fun main() {
    val foo = MaybeABetterFoo()
    if (foo.initialize()) {
        // if we managed to initialize it
        // then do something else
        if (foo.initialized) {
            // we often need to double check if it was not released
            if (foo.canDoSth()) {
                // maybe there are still things that need to be checked
                foo.doSth() // our code looks like an arrow >
            }
        }
    }
}