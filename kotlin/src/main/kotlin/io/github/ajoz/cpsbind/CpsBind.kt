package io.github.ajoz.cpsbind

fun <A, B, C> compose(f: (A) -> B, g: (B) -> C): (A) -> C =
        { a: A -> g(f(a)) }

val size: (String) -> Int = String::length
val is42: (Int) -> Boolean = { it == 42 }

val isStr42 = compose(size, is42)

typealias SizeCallback = (Int) -> Unit
typealias Is42Callback = (Boolean) -> Unit

val sizeCPS: (String, SizeCallback) -> Unit = { str, sc ->
    sc(str.length)
}

val is42CPS: (Int, Is42Callback) -> Unit = { int, ic ->
    ic(int == 42)
}

fun <A, B, C>