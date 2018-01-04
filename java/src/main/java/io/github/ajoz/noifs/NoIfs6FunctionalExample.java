package io.github.ajoz.noifs;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * In the last example we have changed the enum predicate into a BiFunction. Although we operate only on function calls
 * we have a dummy TriFunction that takes a BiFunction and applies some arguments to it. The more arguments a function
 * has the harder it is to read and analyse. Let's simplify things and downgrade all the TriFunctions we have to
 * BiFunctions.
 *
 * We currently have:
 *
 * Match: (String, (String, String) -> Boolean, String) -> Boolean
 *
 * But we could easily just use:
 *
 * Match: (String, String) -> Boolean
 *
 * in its place. The only thing that we really need to change is our CaseInsensitive function:
 *
 * Function<Match, Match> caseInsensitive = (match) -> {
 *     return (where, predicate, what) -> {
 *         return match.apply(where.toLowerCase(), predicate, what.toLowerCase());
 *     };
 * };
 *
 * As Match is now a BiFunction, everything can be written as:
 *
 * Function<Match, Match> caseInsensitive = (match) -> {
 *     return (where, what) -> {
 *         return match.apply(where.toLowerCase(), what.toLowerCase());
 *     };
 * };
 *
 * Let's check few examples of how we can use everything together:
 *
 * caseInsensitive.apply(matchContains).apply("Test", "Te");
 * caseInsensitive.apply(matchEquals).apply("Test", "Te");
 * matchContains.apply("Test", "Te");
 * matchEquals.apply("Test", "teeee");
 *
 * So what are the advantages?
 * - matchContains and matchEquals are just two simple examples of how a match can be done. We are operating with a very
 * simple building block: (String, String) -> Boolean. It allows us to do whatever operation we want as long as we return
 * a true or false at the end. In other words there are no restraints on the matching algorithm we will choose.
 * - caseInsensitive is also another example of flexibility we have. It takes a (String, String) -> Boolean function and
 * returns a (String, String) -> Boolean function. Lowering the case of both strings is just a simple example of preprocessing
 * that can be done before a matching. It's not hard to imagine other types of Match -> Match functions that would change
 * the strings in some way: trim, remove white chars, shorten, etc.
 * - although original algorithm was not very long, we still got a shorter, more concise form that's easily reusable and
 * extendable.
 *
 * Any issues?
 * - we lost the original "sentence-like" readability. Every part does not combine into a nice sentence like before but
 * it's much less code to read and maintain. Java expressiveness doesn't help with all those .apply() we need to use.
 * - one can argue that when there is a very generic (String, String) -> Boolean function then anything can be passed
 * there even functions that are not matching strings at all. It's true. But such mistake is much easier to spot then
 * going through a long algorithm with many branching paths.
 */
public class NoIfs6FunctionalExample {

    public interface Match extends BiFunction<String, String, Boolean> {}

    public static Match matchContains = String::contains;

    public static Match matchEquals = String::contentEquals;

    public static Function<Match, Match> caseInsensitive =
            match -> (where, what) -> match.apply(where.toLowerCase(), what.toLowerCase());


    public static void main(String[] args) {
        caseInsensitive.apply(matchContains).apply("Test", "Te");
        caseInsensitive.apply(matchEquals).apply("Test", "Te");
        matchContains.apply("Test", "Te");
        matchEquals.apply("Test", "teeee");
    }
}
