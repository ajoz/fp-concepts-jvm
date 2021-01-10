package io.github.ajoz.capability.designissues
/*
 I do not like modern approach to object oriented design.
 I never know what methods a class should have for it to
 be able to correctly reflect real life and reality.

 I often think that maybe all the people that do not have
 such problems are using some secret knowledge that I did
 not have access to.

 This was my first issue when doing some OOP an email class
 should have a send or receive method?
 */
class Mail {
    fun send() {
        // business logic ...
    }
    // or
    fun receive() {
        // business logic ...
    }
}

fun main() {
    val email = Mail()
    email.send()
    // vs
    email.receive()
}
/*
 Should an email even know how to send itself? You might
 probably dismiss this question as silly but shouldn't
 asking such question help us get to a better design?

 Maybe there should be a MailSender and MailReceiver
 classes? But this might complicate the design too much,
 soon we will get to a point in which there will be
 delegates, managers, proxies, fascades etc and we just
 want to send an email (or receive it) ;-)
 */