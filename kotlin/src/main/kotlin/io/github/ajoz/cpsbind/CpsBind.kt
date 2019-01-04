package io.github.ajoz.cpsbind

/*
  Those pesky Monads are everywhere!

  Let's look at the simple composition of two functions:
  f :: A -> B
  g :: B -> C


 */
fun <A, B, C> compose(f: (A) -> B,
                      g: (B) -> C): (A) -> C =
        { a: A -> g(f(a)) }

val size: (String) -> Int = String::length
val is42: (Int) -> Boolean = { it == 42 }

val isStr42 = compose(size, is42)

typealias SizeContinuation = (Int) -> Unit
typealias Is42Continuation = (Boolean) -> Unit

// CPS = Continuation Passing Style
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

class Cont<A>(val run: (Continuation<A>) -> Unit)

val sizeRun: (String) -> Cont<Int> = { string ->
    Cont { continuationA: Continuation<Int> -> continuationA(string.length) }
}

val is42Run: (Int) -> Cont<Boolean> = { int ->
    Cont { it(int == 42) }
}

fun <A, B, C> composeCont(f: (A) -> Cont<B>,
                          g: (B) -> Cont<C>): (A) -> Cont<C> =
        { a: A ->
            Cont { cc ->
                f(a).run { b: B ->
                    g(b).run(cc)
                }
            }
        }

val isStr42Run = composeCont(sizeRun, is42Run)

fun <A, B> bindRun(r: Cont<A>, f: (A) -> Cont<B>): Cont<B> =
        Cont { cb: Continuation<B> ->
            r.run { a: A ->
                f(a).run(cb)
            }
        }

fun <A, B> Cont<A>.bind(f: (A) -> Cont<B>): Cont<B> =
        bindRun(this, f)

fun <A> returnRun(value: A): Cont<A> =
        Cont { it(value) }



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
    isStr42Run("JUG Łódź CPS HOF Cont!").run { isJUGStr42 ->
        println("Is JUG string size equal 42? HOF - Cont style! $isJUGStr42")
    }

    // continuation passing composition with intermediate wrapping type bind
    val isStr42Bind = bindRun(
            sizeRun("JUG Łódź CPS HOF Cont with Bind!  fluently!"),
            is42Run
    )

    isStr42Bind.run { isJUGStr42 ->
        println("Is JUG string size equal 42? HOF - Cont style but Bound! $isJUGStr42")
    }

    // continuation passing composition with intermediate wrapping type fluently
    returnRun("JUG Łódź CPS HOF Cont with Bind!  fluently!")
            .bind(sizeRun)
            .bind(is42Run)
            .run {
                println("Is is finally 42? $it")
            }

    // Is it familiar? No?!

    // what if I would rename the Cont<A> to Observable<A>
    // bind to flatMap, run to subscribe?
    // you need to squint your eyes cause of lack of error handling

    // what if I would rename the Cont<A> to Maybe<A>
    // bind to flatMap, run to ifSome?
    // you need to squint your eyes cause of lack of nothing handling

    // what if I would rename the Cont<A> to Try<A>
    // bind to flatMap, run to ifSuccess?
    // you need to squint your eyes cause of lack of failure handling

    // what if I would rename the Cont<A> to Promise<A>
    // bind to then, run to done?
    // you need to squint your eyes cause Promise starts processing only after
    // done is invoked
}