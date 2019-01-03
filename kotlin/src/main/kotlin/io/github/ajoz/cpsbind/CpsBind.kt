package io.github.ajoz.cpsbind

fun <A, B, C> compose(f: (A) -> B, g: (B) -> C): (A) -> C =
        { a: A -> g(f(a)) }

val size: (String) -> Int = String::length
val is42: (Int) -> Boolean = { it == 42 }

val isStr42 = compose(size, is42)

typealias SizeContinuation = (Int) -> Unit
typealias Is42Continuation = (Boolean) -> Unit

val sizeCPS: (String, SizeContinuation) -> Unit = { str, sc ->
    sc(str.length)
}

val is42CPS: (Int, Is42Continuation) -> Unit = { int, ic ->
    ic(int == 42)
}

typealias Continuation<A> = (A) -> Unit

fun <A, B, C> composeCPS(f: (A, Continuation<B>) -> Unit,
                         g: (B, Continuation<C>) -> Unit): (A, Continuation<C>) -> Unit =
        { a: A, continuationC: Continuation<C> ->
            f(a) { b: B ->
                g(b) { c: C ->
                    continuationC(c)
                }
            }
        }

val isStr42CPS = composeCPS(sizeCPS, is42CPS)

fun <A, B, C> ((A, B) -> C).curried(): (A) -> (B) -> C =
        { a: A ->
            { b: B ->
                this(a, b)
            }
        }

val sizeHOF = sizeCPS.curried()
val is42HOF = is42CPS.curried()

fun <A, B, C> composeCPSHOF(f: (A) -> (Continuation<B>) -> Unit,
                            g: (B) -> (Continuation<C>) -> Unit): (A) -> (Continuation<C>) -> Unit =
        { a: A ->
            { continuationC: Continuation<C> ->
                f(a)() { b: B ->
                    g(b)() { c: C ->
                        continuationC(c)
                    }
                }
            }
        }

val isStr42HOF = composeCPSHOF(sizeHOF, is42HOF)

class Run<A>(val run: (Continuation<A>) -> Unit)

val sizeRun: (String) -> Run<Int> = { string ->
    Run { continuationA: Continuation<Int> -> continuationA(string.length) }
}

val is42Run: (Int) -> Run<Boolean> = { int ->
    Run { it(int == 42) }
}

fun <A, B, C> composeRun(f: (A) -> Run<B>,
                         g: (B) -> Run<C>): (A) -> Run<C> =
        { a: A ->
            Run { cc ->
                f(a).run { b: B ->
                    g(b).run(cc)
                }
            }
        }

val isStr42Run = composeRun(sizeRun, is42Run)

fun <A, B> bindRun(r: Run<A>, f: (A) -> Run<B>): Run<B> =
        Run { cb: Continuation<B> ->
            r.run { a: A ->
                f(a).run(cb)
            }
        }

fun <A, B> Run<A>.bind(f: (A) -> Run<B>): Run<B> =
        bindRun(this, f)

fun <A> returnRun(value: A): Run<A> =
        Run { it(value) }

fun main(args: Array<String>) {
    // plain composition
    println("composition = ${isStr42("JUG Łódź!")}")

    // continuation passing composition
    isStr42CPS("JUG Łódź CPS!") { isJUGStr42 ->
        println("Is JUG string size equal 42? $isJUGStr42")
    }

    // continuation passing composition curried
    isStr42HOF("JUG Łódź CPS HOF!")() { isJUGStr42 ->
        println("Is JUG string size equal 42? HOF style! $isJUGStr42")
    }

    // continuation passing composition with intermediate wrapping type
    isStr42Run("JUG Łódź CPS HOF Run!").run { isJUGStr42 ->
        println("Is JUG string size equal 42? HOF - Run style! $isJUGStr42")
    }

    // continuation passing composition with intermediate wrapping type bind
    val isStr42Bind = bindRun(
            sizeRun("JUG Łódź CPS HOF Run with Bind!  fluently!"),
            is42Run
    )

    isStr42Bind.run { isJUGStr42 ->
        println("Is JUG string size equal 42? HOF - Run style but Bound! $isJUGStr42")
    }

    // continuation passing composition with intermediate wrapping type fluently
    returnRun("JUG Łódź CPS HOF Run with Bind!  fluently!")
            .bind(sizeRun)
            .bind(is42Run)
            .run {
                println("Is is finally 42? $it")
            }

    // Is it familiar? No?!

    // what if I would rename the Run<A> to Promise<A>
    // bind to then, run to done?

    // what if I would rename the Run<A> to Observable<A>
    // bind to flatMap, run to subscribe?

}