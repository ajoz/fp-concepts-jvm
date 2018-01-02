package io.github.ajoz

import java.util.*

class NonEmptyList<A> : Semigroup<NonEmptyList<A>>, Functor<A> {
    private val list = LinkedList<A>()

    constructor(item: A) {
        list.add(item)
    }

    private constructor(seed: List<A>) {
        list.addAll(seed)
    }

    fun append(item: A): NonEmptyList<A> {
        val appened = LinkedList<A>()
        appened.addAll(list)
        appened.add(item)
        return NonEmptyList(appened)
    }

    override fun append(other: NonEmptyList<A>): NonEmptyList<A> {
        val appened = LinkedList<A>()
        appened.addAll(list)
        appened.addAll(other.list)
        return NonEmptyList(appened)
    }

    override fun <B> map(func: (A) -> B) =
            NonEmptyList(list.map(func))

    override fun toString(): String {
        return "NonEmptyList{$list}"
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as NonEmptyList<*>?
        return list == that!!.list
    }

    override fun hashCode(): Int {
        return Objects.hash(list)
    }
}