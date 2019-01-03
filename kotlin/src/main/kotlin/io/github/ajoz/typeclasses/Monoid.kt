package io.github.ajoz.typeclasses

/*
  Let's define a

 */
interface Monoid<A> {
    infix fun A.mappend(other: A): A
    fun mempty(): A
    fun mconcat(items: List<A>): A =
            foldl({ a: A, b: A -> a mappend b }, mempty(), items)
}

object IntAddMonoidInstance : Monoid<Int> {
    override fun Int.mappend(other: Int) = this + other
    override fun mempty() = 0
}

object IntMultiplicationMonoidInstance : Monoid<Int> {
    override fun Int.mappend(other: Int) = this * other
    override fun mempty() = 1
}

object ListMonoidInstance : Monoid<List<*>> {
    override fun List<*>.mappend(other: List<*>) = this + other
    override fun mempty() = emptyList<Any>()
}

tailrec fun <A, B> foldl(f: (B, A) -> B, id: B, list: List<A>): B =
        when (list.isEmpty()) {
            true -> id
            false -> foldl(f, f(id, list.head), list.tail)
        }

val <A> List<A>.head
    get() = this[0]

val <A> List<A>.tail
    get() = this.subList(1, this.size)

val List<Int>.sum
    get() = IntAddMonoidInstance.mconcat(this)

val List<Int>.product
    get() = IntMultiplicationMonoidInstance.mconcat(this)

@Suppress("UNCHECKED_CAST")
fun <A> List<List<A>>.flatten(): List<A> =
        ListMonoidInstance.mconcat(this) as List<A>

fun main(args: Array<String>) {
    val ints = listOf(1, 2, 3, 4, 5, 6)

    println("Sum of $ints = ${ints.sum}")
    println("Product of $ints = ${ints.product}")

    val complex = listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6)
    )

    println("Flatten of $complex = ${complex.flatten()}")
}