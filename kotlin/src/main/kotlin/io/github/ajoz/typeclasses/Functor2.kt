package io.github.ajoz.typeclasses


interface Kind2<F, A> {
    val wrapped: Any
}

class MaybeKind2<A>(override val wrapped: Any) : Kind2<ForMaybe, A>

@Suppress("UNCHECKED_CAST")
fun <A> Kind2<ForMaybe, A>.fromKind2(): Maybe<A> =
        wrapped as Maybe<A>

fun <A> Maybe<A>.toKind2(): Kind2<ForMaybe, A> =
        MaybeKind2(this)

class ListKind2<A>(override val wrapped: Any) : Kind2<ForList, A>

@Suppress("UNCHECKED_CAST")
fun <A> Kind2<ForList, A>.fromKind2(): List<A> =
        wrapped as List<A>

fun <A> List<A>.toKind2(): Kind2<ForList, A> =
        ListKind2(this)

interface Functor2<F> {
    infix fun <A, B> Kind2<F, A>.map(func: (A) -> B): Kind2<F, B>
}

object MaybeFunctor2Instance : Functor2<ForMaybe> {
    override fun <A, B> Kind2<ForMaybe, A>.map(func: (A) -> B): Kind2<ForMaybe, B> {
        val maybe = fromKind2()
        return (when (maybe) {
            is Maybe.Some -> Maybe.Some(func(maybe.value))
            is Maybe.None -> Maybe.None
        }).toKind2()
    }
}

object ListFunctor2Instance : Functor2<ForList> {
    override fun <A, B> Kind2<ForList, A>.map(func: (A) -> B): Kind2<ForList, B> {
        val list = fromKind2()
        return (when (list.isEmpty()) {
            true -> emptyList()
            else -> list.map(func)
        }).toKind2()
    }
}

fun <F> Functor2<F>.toJson(userKind: Kind2<F, User>): Kind2<F, String> =
        userKind map {
            "{name: ${it.name}, surname: ${it.surname}}"
        }

fun <F> Kind2<F, User>.toJson(f: Functor2<F>): Kind2<F, String> =
        f.toJson(this)

fun Maybe<User>.toJson(): Maybe<String> =
        toKind2().toJson(MaybeFunctor2Instance).fromKind2()

fun List<User>.toJson(): List<String> =
        toKind2().toJson(ListFunctor2Instance).fromKind2()

fun main(args: Array<String>) {
    val user1 = User("Foo", "Bar")
    val user2 = User("Baz", "Qux")

    val users = listOf(user1, user2)
    val maybeUser = Maybe.Some(user1)

    println(users.toJson())
    println(maybeUser.toJson())
}