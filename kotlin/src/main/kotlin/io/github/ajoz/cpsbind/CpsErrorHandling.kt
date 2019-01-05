package io.github.ajoz.cpsbind

val div42by: (Int, Continuation<Double>, Continuation<String>) -> Unit =
        { b, onSuccess, onError ->
            when (b) {
                0 -> onError("Division by zero!")
                else -> onSuccess(42.toDouble() / b)
            }
        }

val square: (Double, Continuation<Double>, Continuation<String>) -> Unit =
        { value, onSuccess, onError ->
            if (value < 0) {
                onError("Square root of a negative number!")
            } else {
                onSuccess(Math.sqrt(value))
            }
        }

typealias ResultContinuation<A, B, E> = (A, Continuation<B>, Continuation<E>) -> Unit

fun <A, B, C, E> composeRC(f: ResultContinuation<A, B, E>,
                           g: ResultContinuation<B, C, E>): ResultContinuation<A, C, E> =
        { a, onSuccess, onError ->
            f(a, { b -> g(b, onSuccess, onError) }, onError)
        }

val divThenSquare = composeRC(div42by, square)

typealias DoubleCont = Continuation<Double>
typealias StrCont = Continuation<String>

val div42ByCurried: (Int) -> (DoubleCont) -> (StrCont) -> Unit =
        { b ->
            { onSuccess ->
                { onError ->
                    div42by(b, onSuccess, onError)
                }
            }
        }

val squareCurried: (Double) -> (DoubleCont) -> (StrCont) -> Unit =
        { value ->
            { onSuccess ->
                { onError ->
                    square(value, onSuccess, onError)
                }
            }
        }

typealias ResultCurried<A, B, E> = (A) -> (Continuation<B>) -> (Continuation<E>) -> Unit

fun <A, B, C, E> composeRCCurried(f: ResultCurried<A, B, E>,
                                  g: ResultCurried<B, C, E>): ResultCurried<A, C, E> =
        { a ->
            { onSuccess ->
                { onError ->
                    f(a)() { b ->
                        g(b)() { c ->
                            onSuccess(c)
                        }(onError)
                    }(onError)
                }
            }
        }

val divThenSquareCurried = composeRCCurried(div42ByCurried, squareCurried)

//data class Result<A, E>(val onSuccess: (Continuation<A>) -> Unit,
//                        val onError: (Continuation<E>) -> Unit) {
//    companion object
//}

typealias OnSuccess<A> = Continuation<A>
typealias OnError<E> = Continuation<E>

data class Result<A, E>(val subscribe: (OnSuccess<A>) -> (OnError<E>) -> Unit) {
    companion object
}

typealias ResultFunction<A, B, E> = (A) -> Result<B, E>

fun <A, B, C, E> composeResult(f: ResultFunction<A, B, E>,
                               g: ResultFunction<B, C, E>): ResultFunction<A, C, E> =
        { a ->
            Result { onSuccess ->
                { onError ->
                    f(a).subscribe { b ->
                        g(b).subscribe { c ->
                            onSuccess(c)
                        }(onError)
                    }(onError)
                }
            }
        }

fun <A, B, E> bindResult(ra: Result<A, E>,
                         f: ResultFunction<A, B, E>): Result<B, E> =
        Result { onSuccess ->
            { onError ->
                ra.subscribe { a ->
                    f(a).subscribe(onSuccess)(onError)
                }(onError)
            }
        }

fun <A, B, E> Result<A, E>.bind(f: (A) -> Result<B, E>): Result<B, E> =
        bindResult(this, f)

fun <A, E> Result.Companion.Success(value: A): Result<A, E> =
        Result { onSuccess -> { onSuccess(value) } }

fun <A, E> Result.Companion.Error(error: E): Result<A, E> =
        Result { { it(error) } }

val div42ByResult: (Int) -> Result<Double, String> = { value ->
    when (value) {
        0 -> Result.Error("Division by zero!")
        else -> Result.Success(42.toDouble() / value)
    }
}

val squareResult: (Double) -> Result<Double, String> = { value ->
    if (value < 0)
        Result.Error("Square root of a negative number!")
    else
        Result.Success(Math.sqrt(value))
}

val divThenSquareResult = composeResult(div42ByResult, squareResult)

data class Result2<A, E>(val subscribe: (error: OnError<E>, success: OnSuccess<A>) -> Unit) {
    companion object
}

typealias Result2Function<A, B, E> = (A) -> Result2<B, E>

fun <A, B, C, E> composeResult2(f: Result2Function<A, B, E>,
                                g: Result2Function<B, C, E>): Result2Function<A, C, E> =
        { a ->
            //            Result2 { onError, onSuccess ->
//                f(a).subscribe(onError) { b ->
//                    g(b).subscribe(onError, onSuccess)
//                }
//            }
            bindResult2(f(a), g)
        }

fun <A, B, E> bindResult2(ra: Result2<A, E>,
                          f: (A) -> Result2<B, E>): Result2<B, E> =
        Result2 { onError, onSuccess ->
            ra.subscribe(onError) { a ->
                f(a).subscribe(onError, onSuccess)
            }
        }

fun <A, B, E> Result2<A, E>.bind(f: (A) -> Result2<B, E>): Result2<B, E> =
        bindResult2(this, f)

fun <A, E> Result2.Companion.Success(value: A): Result2<A, E> =
        Result2 { _, onSuccess -> onSuccess(value) }

fun <A, E> Result2.Companion.Error(error: E): Result2<A, E> =
        Result2 { onError, _ -> onError(error) }

val div42ByResult2: (Int) -> Result2<Double, String> =
        {
            when (it) {
                0 -> Result2.Error("Division by zero!")
                else -> Result2.Success(42.toDouble() / it)
            }
        }

val squareResult2: (Double) -> Result2<Double, String> =
        {
            if (it < 0)
                Result2.Error("Square root of a negative number!")
            else
                Result2.Success(Math.sqrt(it))
        }

val divThenSquareResult2 = composeResult2(div42ByResult2, squareResult2)

fun main(args: Array<String>) {
/*
    divThenSquare(0, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })
    divThenSquare(-1, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })
    divThenSquare(42, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })

    val a = divThenSquareCurried(0)
    val b = a {
    }
    b {
    }

    divThenSquareCurried(0)() {
        println("divThenSquareCurried success: $it")
    }() {
        println("divThenSquareCurried error: $it")
    }

    divThenSquareCurried(-1)() {
        println("divThenSquareCurried success: $it")
    }() {
        println("divThenSquareCurried error: $it")
    }

    divThenSquareCurried(42)() {
        println("divThenSquareCurried success: $it")
    }() {
        println("divThenSquareCurried error: $it")
    }
    */

/*
    divThenSquareResult(42).subscribe {
        println("Result success: $it")
    }() {
        println("Result error: $it")
    }

    Result.Success<Int, String>(0)
            .bind(div42ByResult)
            .bind(squareResult)
            .subscribe {
                println("Result success: $it")
            }() {
        println("Result error: $it")
    }
    */

    Result2.Success<Int, String>(42)
            .bind(div42ByResult2)
            .bind(squareResult2)
            .subscribe(
                    { println("Result2 error: $it") },
                    { println("Result2 success: $it") }
            )
}



