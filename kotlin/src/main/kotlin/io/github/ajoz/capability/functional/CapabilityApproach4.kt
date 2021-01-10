package io.github.ajoz.capability.functional

import java.util.concurrent.atomic.AtomicBoolean

/*
 How to revoke the rights? We could simply deny any query and end it with an
 Exception (I know it is crude) when the rights were revoked. We need to have
 an easy handle so, it is just a simple call on our end.

 We could go with a simple interface approach (there was too much fp lately ;-)
 */
interface Revoker {
    fun revoke() // for the sake of the example this is good enough
}

/*
 Now let's have a function that will add revoking capability to any function
 passed to it as an argument:
 */
fun <A, B> revocable(f: (A) -> B): Pair<(A) -> B, Revoker> {
    val available = AtomicBoolean(true)
    val revocableFunction: (A) -> B = {
        if (available.get()) {
            f(it)
        }
        throw IllegalStateException("Privileges were revoked!")
    }
    val revoker = object : Revoker {
        override fun revoke() {
            available.set(false)
        }
    }
    return Pair(revocableFunction, revoker)
}

@Suppress("UNUSED_VARIABLE")
fun main() {
    val db = Database()

    val revocable: Pair<(query: DbQuery) -> DbResult, Revoker> =
            revocable(db::run)

    val revocableFunction: (query: DbQuery) -> DbResult =
            revocable.first

    val revoker: Revoker =
            revocable.second

    val uam = UserAcquisitionManager(revocableFunction)

    // now to revoke at any moment
    revoker.revoke()

    // we could wrap everything with logging:
    val loggedRevocableFunction: (query: DbQuery) -> DbResult =
            addLogging("UserAcquisitionManager", revocableFunction)

    // or
    val revocableLogged: Pair<(query: DbQuery) -> DbResult, Revoker> =
            revocable(addLogging("UserAcquisitionManager", db::run))

    val revocableLoggedFunction: (query: DbQuery) -> DbResult =
            revocable.first

    val revokerLogged: Revoker =
            revocable.second

}