package io.github.ajoz.typeclasses

interface Monoid<A> {
    infix fun A.mappend(other: A): A
    fun mempty(): A
}

object IntAddMonoid : Monoid<Int> {
    override fun Int.mappend(other: Int) = this + other
    override fun mempty() = 0
}

object IntMultiplicationMonoid : Monoid<Int> {
    override fun Int.mappend(other: Int) = this * other
    override fun mempty() = 1
}

object ListMonoid : Monoid<List<*>> {
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

