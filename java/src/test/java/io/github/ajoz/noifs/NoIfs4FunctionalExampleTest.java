package io.github.ajoz.noifs;

import io.github.ajoz.noifs.NoIfs4FunctionalExample.MatchPredicate;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.ajoz.noifs.NoIfs4FunctionalExample.caseInsensitive;
import static io.github.ajoz.noifs.NoIfs4FunctionalExample.match;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class NoIfs4FunctionalExampleTest {

    @Test
    @Parameters({
            "Short string, EQUALS, Short string",
            "Short string, CONTAINS, Short",
            "OneWord, EQUALS, OneWord",
            "OneWord, CONTAINS, One",
            "A long long string, EQUALS, A long long string",
            "A long long string, CONTAINS, string",
            " , EQUALS, ",
            " , CONTAINS, ",
            "     , CONTAINS, ",
            ", EQUALS,",
            ", CONTAINS,"
    })
    @TestCaseName("should match({0}, {1}, {2}) for case sensitive")
    public void shouldMatchCaseSensitive(final String where,
                                         final MatchPredicate predicate,
                                         final String what) {
        assertTrue(match.apply(where, predicate, what));
    }

    @Test
    @Parameters({
            "Short string, CONTAINS,SHOT STRING",
            "Short string, EQUALS,SHORT",
            "Short string, EQUALS, short",
            "Short string, EQUALS,",
            "Short string, EQUALS, ",
            ", EQUALS, notempty",
            ", CONTAINS, notempty",
            "notempty, EQUALS,"
    })
    @TestCaseName("should match({0}, {1}, {2}) for case insensitive")
    public void shouldMatchCaseInsensitive(final String where,
                                           final MatchPredicate predicate,
                                           final String what) {
        assertFalse(caseInsensitive.apply(match).apply(where, predicate, what));
    }
}