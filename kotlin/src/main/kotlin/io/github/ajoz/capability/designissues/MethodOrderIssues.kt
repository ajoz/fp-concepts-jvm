package io.github.ajoz.capability.designissues

/*
 Another thing that always bothers me is the order in which
 the methods should be called. On the surface level of the
 API it is not clear when a method can or cannot be called
 most of the time.

 We often have classes that have methods that can be called
 only once or can be called if other methods were called first.

 This makes things a bit problematic as in the usual oop world
 of java, kotlin, c#, cpp or any mainstream languages you just
 cannot say anything about the timing just looking at the class
 interface.

 At least not without overly complicating the design?
 */

class Foo {
    fun initialize() {
        // should be called before using the class
    }

    fun release() {
        // should be called after using the class
    }

    // some other important methods here...
}

fun main() {
    val foo = Foo()
    // although we've created Foo we cannot use it :(
    // but nothing in this world prevents us from it
    foo.initialize()
    // now we can use it without a sudden runtime exception
    // as soon as we release Foo we cannot use it :(
    foo.release()
    // but nothing in this world prevents us from it
}


