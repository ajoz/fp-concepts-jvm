package io.github.ajoz.capability.functional

/*
 So if passing the whole database object is a big No No, then what? We can start
 with using a simple function instead. For our intents and purposes a Database
 is just a function that takes a query on one end and spits a result on the other.

 What will this gives us?
 - no need to fight with mocks in our unit/integration tests
 - we do not need an in memory database with correct data just to test anything
 - if we have enough abstraction then we won't be relaying on a single database
 - we cannot now call different non query related methods on the database object
   and we are not tempted to do it.
 */
class UserAcquisitionManager2
(
        private val query: (DbQuery) -> DbResult /*, here other fields of course */
) {
    // super important business logic that is using the database
}

@Suppress("UNUSED_VARIABLE")
fun main() {
    // if we need the database for something we can create it
    val db = Database()

    // we can wrap the database in a function
    val simpleQueryFunction1: (DbQuery) -> DbResult = { query ->
        db.run(query)
    }
    // in a few different ways
    val simpleQueryFunction2: (DbQuery) -> DbResult = {
        db.run(it)
    }
    // Kotlin allows to do a few approaches
    val simpleQueryFunction3: (DbQuery) -> DbResult =
            db::run

    // we could pass specialized versions of this function to our
    // acquisition manager now
    val uam = UserAcquisitionManager2(simpleQueryFunction3)
}

/*
 Are there any problems left? Unfortunately yes! It really depends on our
 query abstraction. If the DbQuery is capable of doing INSERT, UPDATE, DELETE
 rather then SELECT only then we can run any query. Also a SELECT on any table
 is not a thing we would like the UserAcquisitionManager to be able to do,
 it should only extract user information, maybe not even all of them.

 Allowing it to do any select is a potential security risk.

 Before we will think how to solve this issue, let's explore the current
 implementation.
 */