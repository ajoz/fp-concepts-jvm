package io.github.ajoz.capability.functional

class UserAcquisitionManagerBetter
(
        private val query: (DbQuery) -> (() -> DbResult)? /*, here other fields of course */
) {
    // super important business logic that is using the database
}

/*
 Is this monstrosity even better?
 query: (DbQuery) -> (() -> DbResult)?

 What do we have here?
 - a function that takes a query as an argument
 - and returns a function or a null

 Just by looking at this single line of code it is hard to wrap your head
 around it.

 Why like this?
 - we do not want any SELECT query, if a code can query for USERS then why can't
   it query for passwords, finances, secrets?
 - we can check each query if it is acceptable before running it

 What's not to like?
 - Our code will need to handle failure cases
 - It makes the code much more complicated

 Would be nice if the UserAcquisitionManager would be created with only the
 data it needs:
 */
class UserAcquisitionManagerEvenBetter
(
        private val query: () -> DbResult /*, here other fields of course */
) {
    // super important business logic that is using the database
}

/*
 In the version above it receives the function that knows from the start what
 data is needed.
 */