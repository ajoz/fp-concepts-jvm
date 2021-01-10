package io.github.ajoz.capability.functional

// some database
class Database {
    fun run(query: DbQuery): DbResult =
            DbResult() // some dummy result
}

// represents a possible query in the database
class DbQuery

// represents a possible results when doing a query on the database
class DbResult
