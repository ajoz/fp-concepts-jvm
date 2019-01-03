package io.github.ajoz.typeclasses

interface Functor<F> {
    fun <A, B> map(f: Kind<F, A>, func: (A) -> B): Kind<F, B>
}

interface Kind<F, A> {
    val wrapped: Any
}

class ForList private constructor() {
    companion object
}

@Suppress("UNCHECKED_CAST")
fun <A> Kind<ForList, A>.fromKind() = wrapped as List<A>

fun <A> List<A>.toKind(): Kind<ForList, A> =
        object : Kind<ForList, A> {
            override val wrapped: Any = this@toKind
        }

object ListFunctorInstance : Functor<ForList> {
    override fun <A, B> map(f: Kind<ForList, A>, func: (A) -> B) =
            f.fromKind().map(func).toKind()
}

sealed class Maybe<out A> {
    data class Some<A>(val value: A) : Maybe<A>()
    object None : Maybe<Nothing>()
}

class ForMaybe private constructor() {
    companion object
}

fun <A> Maybe<A>.toKind() =
        object : Kind<ForMaybe, A> {
            override val wrapped: Any = this@toKind
        }

@Suppress("UNCHECKED_CAST")
fun <A> Kind<ForMaybe, A>.fromKind() =
        wrapped as Maybe<A>

object MaybeFunctorInstance : Functor<ForMaybe> {
    override fun <A, B> map(f: Kind<ForMaybe, A>, func: (A) -> B): Kind<ForMaybe, B> {
        val maybe = f.fromKind()
        return when (maybe) {
            is Maybe.Some -> Maybe.Some(func(maybe.value)).toKind()
            is Maybe.None -> Maybe.None.toKind()
        }
    }
}

data class User(val name: String, val surname: String)

fun <F> Functor<F>.toJson(userData: Kind<F, User>): Kind<F, String> =
        map(userData) { user: User ->
            "{name: ${user.name}, surname: ${user.surname}}"
        }

fun main(args: Array<String>) {
    val user1 = User("Foo", "Bar")
    val user2 = User("Baz", "Qux")

    val users = listOf(user1, user2)
    val maybeUser = Maybe.Some(user1)

    val listJson = ListFunctorInstance.toJson(users.toKind()).fromKind()
    val maybeJson = MaybeFunctorInstance.toJson(maybeUser.toKind()).fromKind()

    println(listJson)
    println(maybeJson)
}