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

data class Result1<A, E>(val onSuccess: (Continuation<A>) -> Unit,
                         val onError: (Continuation<E>) -> Unit)

fun <A, E> returnSuccessResult1(value: A): Result1<A, E> =
        Result1(
                onSuccess = { it(value) },
                onError = {}
        )

fun <A, E> returnErrorResult1(error: E): Result1<A, E> =
        Result1(
                onSuccess = {},
                onError = { it(error) }
        )

val div42ByRes1: (Int) -> Result1<Double, String> =
        { value: Int ->
            when (value) {
                0 -> returnErrorResult1("Division by zero!")
                else -> returnSuccessResult1(42.toDouble() / value)
            }
        }

val squareRes1: (Double) -> Result1<Double, String> =
        { value: Double ->
            if (value < 0) {
                returnErrorResult1("Square root of a negative number!")
            } else
                returnSuccessResult1(Math.sqrt(value))
        }

typealias Result1Function<A, B, E> = (A) -> Result1<B, E>

fun <A, B, C, E> composeResult1(f: Result1Function<A, B, E>,
                                g: Result1Function<B, C, E>): Result1Function<A, C, E> =
        { a ->
            Result1(
                    onSuccess = { cc ->
                        val rb = f(a)
                        rb.onSuccess { b ->
                            val rc = g(b)
                            rc.onSuccess(cc)
                        }
                    },
                    onError = { ce ->
                        val rb = f(a)
                        rb.onError(ce)
                        rb.onSuccess { b ->
                            val rc = g(b)
                            rc.onError(ce)
                        }
                    }
            )
        }

val divThenSquareRes1 = composeResult1(div42ByRes1, squareRes1)

fun main(args: Array<String>) {
//    divThenSquare(0, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })
//    divThenSquare(-1, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })
//    divThenSquare(42, { println("divThenSquare success: $it") }, { println("divThenSquare error: $it") })
//
//    val a = divThenSquareCurried(0)
//    val b = a {
//    }
//    b {
//    }
//
//    divThenSquareCurried(0)() {
//        println("divThenSquareCurried success: $it")
//    }() {
//        println("divThenSquareCurried error: $it")
//    }
//
//    divThenSquareCurried(-1)() {
//        println("divThenSquareCurried success: $it")
//    }() {
//        println("divThenSquareCurried error: $it")
//    }
//
//    divThenSquareCurried(42)() {
//        println("divThenSquareCurried success: $it")
//    }() {
//        println("divThenSquareCurried error: $it")
//    }

    val res1 = divThenSquareRes1(0)
    res1.onSuccess { println("divThenSquareRes1 success: $it") }
    res1.onError { println("divThenSquareRes1 error: $it") }

    val res2 = divThenSquareRes1(-1)
    res2.onSuccess { println("divThenSquareRes1 success: $it") }
    res2.onError { println("divThenSquareRes1 error: $it") }

    val res3 = divThenSquareRes1(42)
    res3.onSuccess { println("divThenSquareRes1 success: $it") }
    res3.onError { println("divThenSquareRes1 error: $it") }

}



