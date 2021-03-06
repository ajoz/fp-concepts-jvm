package io.github.ajoz.capability.functional

/*
  Let's start with adding logging to our solution because maybe our original
  database class was doing it out of the box.

  Below is the simple implementation of a logging function. Normally you would
  probably use a specialized library but for the sake of the example this does
  not matter.
 */
fun log(tag: String, message: String) =
        println("$tag:${generateTimestamp()}:$message")

/*
 our logs need timestamps and other ways to identify them, let's imagine that
 the function below delivers this
 */
fun generateTimestamp(): Long =
        1L

/*
 We have the same implementation of the UserAcquisitionManager as previously.
 To add logging we just only need the best programming/design pattern which
 is composition.

 Implementation is quite simple, we take a function, we wrap it up in another
 function and just log the input.
 */
fun <A, B> addLogging(tag: String, f: (A) -> B): (A) -> B = {
    log(tag, "$it")
    f(it)
}
/*
  Let's use it now:
 */
fun main() {
    val db = Database()

    val queryWithLogging: (DbQuery) -> DbResult =
            addLogging("UserAcquisitionManager", db::run)

    val uam = UserAcquisitionManager(queryWithLogging)
}

/*
 Logging is too easy, let's try something harder like revoking the rights on
 demand?
 */