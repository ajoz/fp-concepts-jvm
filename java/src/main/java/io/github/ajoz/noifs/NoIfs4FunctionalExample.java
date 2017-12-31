package io.github.ajoz.noifs;

import java.util.function.Function;

/**
 * In this example we will try to extract "case" function from the match and change match itself to a building block.
 * By extracting I mean that if we call match it will result with a case sensitive operation but if we chain the calls
 * with "caseInsensitive" function we will get an opposite operation. An example of such call:
 * <p>
 * caseInsensitive match "Test string" CONTAINS "test"
 * <p>
 * It reads like a sentence and simplifies internals of match method. Can we achieve such a thing?
 * <p>
 * Let's think about functions in Java. We have:
 * - one argument functions (Function): String -> String
 * - two argument functions (BiFunction): String, String -> String
 * <p>
 * Those two function types you can find in this repository and also in Java8 standard library. There is one problem
 * for us, we have this unfortunate predicate. So maybe a TriFunction: String, Predicate, String -> Boolean?
 * <p>
 * Let's implements it:
 * <p>
 * public interface TriFunction<A1, A2, A3, R> {
 * R apply(A1 arg1, A2 arg2, A3 arg3);
 * }
 * <p>
 * For our particular code it would look like:
 * <p>
 * TriFunction<String, MatchPredicate, String, Boolean>
 * <p>
 * Now we can use this "TriFunction" instead of the match method. We can pass it around. As Java 6 and Java 7 (in lesser
 * extent) is very verbose we can create a helper interface called "Match" that will extend this TriFunction so its
 * easier to read and pass around.
 * <p>
 * public interface Match extends TriFunction<String, MatchPredicate, String, Boolean> {}
 * <p>
 * First let's implement our original match method as a TriFunction. It's fairly simple but what about case sensitivity?
 * We want it to be a function so we need to pass our "Match" into it but what do we return? A boolean? So how will we
 * call matching function? It always returns a boolean? Our case sensitivity function needs to take a "Match" and return
 * a "Match". The one it returns will guarantee that strings are matched without case:
 * <p>
 * Function<Match, Match> caseInsensitive = (match) -> {
 * return (where, predicate, what) -> {
 * return match.apply(where.toLowerCase(), predicate, what.toLowerCase());
 * }
 * }
 * <p>
 * So what is happening here?
 * We take an "Match" object and we return a new "Match" (a TriFunction in disguise). This new "Match" takes old one
 * and becomes a decorator for it. It changes the arguments that are passed to it to lower case and then uses the original
 * "Match" object for calculating the result.
 * <p>
 * The notation used above with lambdas makes this much more readable. So now we can just call the code like this:
 * <p>
 * caseInsensitive.apply(match).apply("Test string", CONTAINS, "test");
 * <p>
 * almost like the promised:
 * <p>
 * caseInsensitive match "Test string" CONTAINS "test"
 * <p>
 * Java unfortunately is very expressive this is why we need to suffer those .apply() explicit calls.
 * <p>
 * But can we make it better?
 */
public class NoIfs4FunctionalExample {

    public enum MatchPredicate {
        CONTAINS,
        EQUALS
    }

    public interface TriFunction<A1, A2, A3, R> {
        R apply(A1 arg1, A2 arg2, A3 arg3);
    }

    //Easier to read like this
    public interface Match extends TriFunction<String, MatchPredicate, String, Boolean> {
    }

    public static Match match =
            (where, predicate, what) -> {
                switch (predicate) {
                    case CONTAINS:
                        return where.contains(what);
                    case EQUALS:
                        return where.contentEquals(what);
                }

                return false;
            };

    public static Function<Match, Match> caseInsensitive =
            match -> (Match) (where, predicate, what) -> match.apply(where.toLowerCase(), predicate, what.toLowerCase());
}
