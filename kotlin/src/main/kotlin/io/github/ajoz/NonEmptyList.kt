package io.github.ajoz

import java.util.LinkedList
import java.util.Objects

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as NonEmptyList<*>?
        return list == that!!.list
    }

    override fun hashCode(): Int {
        return Objects.hash(list)
    }
}