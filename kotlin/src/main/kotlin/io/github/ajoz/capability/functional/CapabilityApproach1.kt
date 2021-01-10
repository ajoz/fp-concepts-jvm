package io.github.ajoz.capability.functional

/*
 Let's imagine a use case in which we have a specialized class
 for gathering information about users on our site/app/whatever.

 Let's call it UserAcquisitionManager.

 First thing that springs to mind is to just give the acquisition
 manager direct access to the database. This might sound like a
 good idea, it is easy, it is simple, but unfortunately it is
 very short sighted. Such approach may quickly lead to a messy
 code. Why?

 If we have access to the whole database it is much more tempting
 to just make the UserAcquisitionManager do things that it was never
 designed to just because it has all the necessary dependencies.

 Yes I know that nothing really forbids us to just add other
 dependencies, but most of the time it is not as easy, might not
 be approved in a PR, might need additional wiring in the whole
 code etc. If too much capability is there already nothing is
 stopping us before we make a mistake :(
 */
class UserAcquisitionManagerWrongApproach
(
        private val db: Database /*, here other fields of course */
) {
    // super important business logic that is using the database
}
/*
Let's ask us some questions:

1) Does UserAcquisitionManager really need access to the whole database?

It doesn't seem so, UserAcquisitionManager should be only interested in
user data nothing more, do we want to give it a chance to mess other things
in the database?

2) Does it need to be able to add any element?

It doesn't need to add anything, it is a module for analyzing information
about users, it shouldn't be able to mess with anything there.

3) Does it need to be able to read any element?

It doesn't need to read ANY element, it needs to read only user related data.
There is much difference between ANY and USER related.

4) Does it need to be able to delete any element?

If it cannot and should not add new element, then it certainly does not need
to delete any element in the database even USER related.

5) What can go wrong if leave it like this?

At first nothing wrong will happen, but only because that in most cases
original code and the intentions behind it do not need to mess anything up.
Later on when the deadlines will be catching your team, there is an issue
on the production and it is late at night, won't you be tempted to just patch
things up and get on with your life?
 */

