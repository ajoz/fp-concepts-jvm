package io.github.ajoz.noifs;

import static io.github.ajoz.util.Functions.curry2;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 */
public class NoIfs7FunctionalExample {
    static final BiFunction<String, String, Boolean> contains = String::contains;
    static final BiFunction<String, String, Boolean> equals = String::equals;

    static final Function<String, Function<String, Boolean>> containsC = curry2(contains);
    static final Function<String, Function<String, Boolean>> equalsC = curry2(equals);


}
