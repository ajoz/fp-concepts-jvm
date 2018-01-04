package io.github.ajoz.noifs

infix fun String.containsA(what: String): Boolean = this.contains(what)

fun ignoreCase(what: String): String = what.toLowerCase()

fun main(args: Array<String>) {
    println("a text" containsA "a")
    println("a longer text" containsA ignoreCase("LONG"))
}