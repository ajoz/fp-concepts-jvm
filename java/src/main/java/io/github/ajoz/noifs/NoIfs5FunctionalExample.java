package io.github.ajoz.noifs;

import io.github.ajoz.util.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * So far we have changed boolean values to enums and then enums to functions. We haven't done this yet for our predicate.
 * Everything boils down to a difference:
 *
 * String.contains(String)
 *
 * and
 *
 * String.contentEquals(String)
 *
 * This screams to be changed to a function in the form of (String, String) -> Boolean.
 *
 * We have such a function called BiFunction. It takes two arguments and returns a result. We can now change our predicate
 * to two separate BiFunctions. We get:
 *
 * CONTAINS: (where, what) -> where.contains(what);
 * EQUALS: (where, what) -> where.contentEquals(what);
 *
 * We can now change our TriFunction to take a BiFunction as an argument. We need to change our matching function. So a
 * simple match will be:
 *
 * Match: (String, (String, String) -> Boolean, String) -> Boolean
 *
 * Or in other words:
 *
 * Match: (where, predicate, what) -> predicate.apply(where, what);
 *
 * Last step is to fix our CaseInsensitive function, its signature is almost the same except we are not passing an enum
 * but a function instead.
 *
 * A lot of extra work, but was it worth it?
 *
 * caseInsensitive.apply(match).apply("Test", contains, "Te"); // true
 * caseInsensitive.apply(match).apply("Test", equals, "main"); // false
 * match.apply("TEST", contains, "ES"); // true
 * match.apply("TEST", contains, "es"); // false
 *
 * There is a problem with it still! Match itself is not useful at all it just uses a given predicate. We can simplify
 * everything even more.
 */
public class NoIfs5FunctionalExample {

    public interface Match extends TriFunction<String, MatchPredicate, String, Boolean> {}

    public interface MatchPredicate extends BiFunction<String, String, Boolean> {}

    public static MatchPredicate contains = String::contains;

    public static MatchPredicate equals = String::contentEquals;

    public static Function<Match, Match> caseInsensitive =
            match -> (where, predicate, what) -> match.apply(where.toLowerCase(), predicate, what.toLowerCase());

    public static Match match = (where, predicate, what) -> predicate.apply(where, what);

    public static void main(final String[] args) {
        caseInsensitive.apply(match).apply("Test", contains, "Te"); // true
        caseInsensitive.apply(match).apply("Test", equals, "main"); // false
        match.apply("TEST", contains, "ES"); // true
        match.apply("TEST", contains, "es"); // false
    }
}
