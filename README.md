# functional-concepts-jvm

This repository is a playground for different functional programming concepts on JVM: Java 8, Kotlin, Scala, Clojure, Eta (Haskell).

* Java - A series of examples of implementing **fold** function. From an example of *sum* and *product* to a full higher-order function. There are few examples implemented with fold like:
  1. count - normally we wouldn't use a fold function to count the number of elements on the list but for practice sake we will.
  2. last - finds the last item on the list
  3. reverse - returns a list with items with reversed order
  4. average - returns average value for the given list of Integers
  5. contains - return a boolean value indicating whether a search item is on the given list.
  6. join - returns a String created from a list of values joined with a given separator
  7. mkString - returns a String created from a list of values joined with a colon
  8. penultimate - returns the element next to the last element of the list. Hint: use Tuple
  9. unique - returns a list with unique elements from the given list
* Java - a series of examples how to change a method with several boolean arguments into composition of functions
* Java - an example how to implement a lazy "stream-like" data structure
* Java - a Scala [Try](http://www.scala-lang.org/api/2.9.3/scala/util/Try.html) implementation
* Java - a Haskell [Validation](https://github.com/tonymorris/validation) implementation
* Kotlin - the same Validation applicative functor implementation (code shorter and much readable then in Java)
