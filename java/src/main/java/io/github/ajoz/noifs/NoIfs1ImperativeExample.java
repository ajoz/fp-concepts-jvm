package io.github.ajoz.noifs;

/**
 * In this exercise we will see how imperative code with conditional statements can be changed to a functional one. It's
 * possible to give up on using boolean flags for methods and use functions instead. Each `if` in the code creates a branch
 * that makes reading the resulting algorithm harder.
 *
 * The simplest approach would be to just create a method that covers each boolean flag. Is it really the simpliest? It
 * would certainly bloat our API.
 *
 * In the example below we will discuss a `match` method. That method is supposed to check if a given string matches
 * (equals or contains) another string.
 *
 * Let's analyze this simple match method. It contains 4 arguments:
 * 1) where - a String which will be used for matching, we treat it as a base in case of globalMatch set to true. By base
 * we mean that we check if it contains the second String `what`
 * 2) ignoreCase - a boolean used to indicate if we want to do a case sensitive matching
 * 3) globalMatch - a boolean used to distinguish if we want to check if two strings are the same (Equals) or if one of
 * those strings contains the other. By "global" we mean whole strings.
 * 4) what - a String which will be matched, in case of globalMatch it will be just `where equals what` otherwise it will
 * be `where contains what`
 *
 * Simple as that! What can be improved here?
 */
public class NoIfs1ImperativeExample {

    /**
     * Checks if the given {@link String} matches another {@link String}. Matching can be global - if whole strings
     * contain the same text, or local - if only part of {@link String} needs to match. It's possible to match strings
     * with case sensitiveness turned on or off.
     *
     * @param where       base text used for matching, against this text all the matching will be performed.
     * @param ignoreCase  true if case sensitiveness needs to turned off, false otherwise.
     * @param globalMatch true if both strings need to contain the same text.
     * @param what        in case of global matching this string needs to be exactly the same as base text, otherwise
     *                    if can be only contained in the base text.
     * @return true if strings match given the matching criteria, false otherwise.
     */
    public static boolean match(final String where, final boolean ignoreCase, final boolean globalMatch, final String what) {
        final String pattern;
        final String target;
        if (ignoreCase) {
            pattern = what.toLowerCase();
            target = where.toLowerCase();
        } else {
            pattern = what;
            target = where;
        }

        if (globalMatch) {
            return target.contentEquals(pattern);
        } else {
            return target.contains(pattern);
        }
    }
}
