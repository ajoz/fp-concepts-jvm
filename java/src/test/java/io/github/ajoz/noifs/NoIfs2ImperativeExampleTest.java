package io.github.ajoz.noifs;

import io.github.ajoz.noifs.NoIfs2ImperativeExample.MatchCase;
import io.github.ajoz.noifs.NoIfs2ImperativeExample.MatchPredicate;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.ajoz.noifs.NoIfs2ImperativeExample.match;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class NoIfs2ImperativeExampleTest {

    @Test
    @Parameters({
            "Short string, CASE_SENSITIVE, EQUALS, Short string",
            "Short string, CASE_INSENSITIVE, EQUALS, Short string",
            "ShOrT StRiNg, CASE_INSENSITIVE, EQUALS, Short string",
            "Short string, CASE_SENSITIVE, CONTAINS, Short",
            "Short string, CASE_INSENSITIVE, CONTAINS, Short",
            "ShOrT StRiNg, CASE_INSENSITIVE, CONTAINS, ShOrT",
            "OneWord, CASE_SENSITIVE, EQUALS, OneWord",
            "OneWord, CASE_INSENSITIVE, EQUALS, OneWord",
            "OnEwOrD, CASE_INSENSITIVE, EQUALS, OneWord",
            "OneWord, CASE_SENSITIVE, CONTAINS, One",
            "OneWord, CASE_INSENSITIVE, CONTAINS, One",
            "OnEwOrD, CASE_INSENSITIVE, CONTAINS, One",
            "A long long string, CASE_SENSITIVE, EQUALS, A long long string",
            "A long long string, CASE_INSENSITIVE, EQUALS, A long long string",
            "A lOnG loNg sTrInG, CASE_INSENSITIVE, EQUALS, A long long string",
            "A long long string, CASE_SENSITIVE, CONTAINS, string",
            "A long long string, CASE_INSENSITIVE, CONTAINS, string",
            "A lOnG loNg sTrInG, CASE_INSENSITIVE, CONTAINS, string",
            " , CASE_SENSITIVE, EQUALS, ",
            " , CASE_SENSITIVE, CONTAINS, ",
            " , CASE_INSENSITIVE, EQUALS, ",
            " , CASE_INSENSITIVE, CONTAINS, ",
            "     , CASE_SENSITIVE, CONTAINS, ",
            "     , CASE_INSENSITIVE, CONTAINS, ", /* only white chars string contains one white char - case insensitive*/
            ", CASE_INSENSITIVE, EQUALS,", /* empty string equals empty - case insensitive*/
            ", CASE_SENSITIVE, EQUALS,", /* empty string equals empty - case sensitive*/
            ", CASE_INSENSITIVE, CONTAINS,", /* empty string contains empty - case insensitive*/
            ", CASE_SENSITIVE, CONTAINS," /* empty string contains empty - case sensitive*/
    })
    @TestCaseName("should give a positive match({0}, {1}, {2}, {3})")
    public void shouldMatchPositive(String where, MatchCase aCase, MatchPredicate predicate, String what) {
        assertTrue(match(where, aCase, predicate, what));
    }

    @Test
    @Parameters({
            "Short string, CASE_INSENSITIVE, CONTAINS,SHOT STRING",
            "Short string, CASE_INSENSITIVE, EQUALS,SHORT",
            "Short string, CASE_INSENSITIVE, EQUALS, short",
            "Short string, CASE_INSENSITIVE, EQUALS,",
            "Short string, CASE_INSENSITIVE, EQUALS, ",
            "Short string, CASE_SENSITIVE, EQUALS,    ",
            "Short string, CASE_SENSITIVE, EQUALS, STRING",
            "Short string, CASE_SENSITIVE, CONTAINS, SHSTR",
            "Short string, CASE_SENSITIVE, EQUALS, itsnothere",
            "Short string, CASE_SENSITIVE, EQUALS,",
            "Short string, CASE_SENSITIVE, EQUALS, ",
            "Short string, CASE_SENSITIVE, EQUALS,    ",
            ", CASE_INSENSITIVE, EQUALS, notempty",
            ", CASE_INSENSITIVE, CONTAINS, notempty",
            ", CASE_SENSITIVE, CONTAINS, notempty",
            ", CASE_SENSITIVE, EQUALS, notempty",
            "notempty, CASE_INSENSITIVE, EQUALS,",
            "notempty, CASE_SENSITIVE, EQUALS,"
    })
    @TestCaseName("should give a negative match({0}, {1}, {2}, {3})")
    public void shouldMatchNegative(String where, MatchCase aCase, MatchPredicate predicate, String what) {
        assertFalse(match(where, aCase, predicate, what));
    }
}