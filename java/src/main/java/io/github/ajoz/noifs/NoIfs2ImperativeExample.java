package io.github.ajoz.noifs;

/**
 * Boolean flags lower the code readability no matter how much you disagree with this statement. Multiple boolean flags
 * in one method are confusing when reading the code. I can't even imagine a code with 3, 4 or 5 boolean flags in one
 * method. It's a testing nightmare as you are supposed to check each combination of arguments passed to the method.
 *
 * Often boolean flags can be exchanged with enums to increase readability. Let's examine our `match` method. When we
 * replace flags with enums then each mode needs to be written explicitly instead of simple true / false. Compare:
 *
 * match("Text", CASE_INSENSITIVE, CONTAINS, "EXT");
 *
 * with:
 *
 * match("Text", true, false, "EXT");
 *
 * Let me give a rather bold statement that it almost reads like a "sentence". Is our work here done? There is a main
 * problem with this approach. Currently we have two types of case matching: sensitive and insensitive. It's pretty
 * straightforward but what about the predicate? Currently we are only checking if two strings are equal or one contains
 * the other but maybe we would like to have another predicate someday? Then what? We need to extend both our predicate
 * enum and our switch that is handling predicates. I know that in this simple example below adding one or two more
 * predicates won't but much of a problem but imagine several new types of matching predicate. It could be a maintenance
 * and testing hell.
 *
 * So let's examine the examples below and think how we can improve it.
 */
public class NoIfs2ImperativeExample {

    public enum MatchCase {
        CASE_SENSITIVE,
        CASE_INSENSITIVE
    }

    public enum MatchPredicate {
        CONTAINS,
        EQUALS
    }

    public static boolean match(final String where,
                                final MatchCase cse,
                                final MatchPredicate predicate,
                                final String what) {
        final String target;
        final String pattern;
        switch (cse) {
            case CASE_INSENSITIVE:
                target = where.toLowerCase();
                pattern = what.toLowerCase();
                break;
            case CASE_SENSITIVE:
            default:
                target = where;
                pattern = what;
        }

        switch (predicate) {
            case CONTAINS:
                return target.contains(pattern);
            case EQUALS:
                return target.contentEquals(pattern);
        }

        return false;
    }
}
