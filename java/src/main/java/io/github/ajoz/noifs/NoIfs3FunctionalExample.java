package io.github.ajoz.noifs;

import java.util.function.Function;

/**
 * Instead of using enums like in the previous example we will now try to substitute them with something more flexible.
 * As we said before boolean flags are a smell and we replaced them with enums to increase the readability. Unfortunately
 * we have substituted one `code smell` with another `code smell` as to work with enums we now need `switch` statements.
 * <p>
 * So how do we remove a `switch` statement? We could use replace it with polymorphism (the java equivalent at least).
 * We could create an interface:
 * <p>
 * public interface Case {
 * String apply(String s);
 * }
 * <p>
 * And specific implementations for case sensitive and case insensitive. There is even a better solution we can create a
 * Function. Please look at {@link Function} - its a simple Java 6 / 7 compatible version, but with the introduction of
 * Java 8 there is a version with very useful default methods like `andThen`, `compose` and `identity`.
 * <p>
 * What is the advantage? Its pretty simple:
 * - we can pass any function that changes String -> String
 * - we can have the functions as separate reusable building blocks
 * - we can test created functions outside of the scope of the match method
 * - we achieve readability as the original switch is now gone
 * <p>
 * There are some drawbacks:
 * - we need to have two functions one of case sensitive and case insensitive. It's confusing that we need to apply
 * the identity function for situation when we don't want to ignore the string case.
 * - those functions no longer describe a string "case" sensitivity but rather represent some string processing done
 * before strings are matched. How those functions are called might be confusing.
 * <p>
 * Let's try to figure out how to extract our String -> String function out of match method?
 */
public class NoIfs3FunctionalExample {

    public enum MatchPredicate {
        CONTAINS,
        EQUALS
    }

    public static Function<String, String> caseSensitive = s -> s;

    public static Function<String, String> caseSensitive2 = Function.identity();

    public static Function<String, String> caseInsensitive = String::toLowerCase;

    public static boolean match(final String where,
                                final Function<String, String> cse,
                                final MatchPredicate predicate,
                                final String what) {
        final String pattern = cse.apply(what);
        final String target = cse.apply(where);

        switch (predicate) {
            case CONTAINS:
                return target.contains(pattern);
            case EQUALS:
                return target.contentEquals(pattern);
        }

        return false;
    }
}
