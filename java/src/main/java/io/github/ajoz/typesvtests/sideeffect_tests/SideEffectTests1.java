package io.github.ajoz.typesvtests.sideeffect_tests;

/*
  Many times we test if the code we've written correctly performs some
  side effects. Mutates some value in the memory, changes the database,
  writes to a file, sends something over the network. There are many
  examples of useful side effects that are subjects of testing.

  Let's imagine we have a navigation app, our routing API exposes a
  Route class that has a departure and destination information. Let's
  say we allow the destination to be changed.


 */
class Departure {}
class Destination {}

class Route {
    Departure departure;
    Destination destination;
}

/*
  There is also a RouteAPI class that allows us to modify the current
  route:
 */

class RouteAPI {
    void modify(final Route route) {
        // ...
    }
}

/*
  Like you probably noticed already Route is mutable, this creates
  different issues, even threading can be problematic if the reference
  to the Route is passed to several places.

  The solution to this is making the modification function pure and Route
  immutable to avoid any issues related to threading etc.
 */

class RouteAPI2 {
    Route modify(final Route route) {
        throw new UnsupportedOperationException(""); // not important
    }
}

/*
  When the function is pure, there is no need to test side effects.
 */

public class SideEffectTests1 {

}
