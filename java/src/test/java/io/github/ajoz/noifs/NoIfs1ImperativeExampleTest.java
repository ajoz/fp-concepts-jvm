package io.github.ajoz.noifs;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.ajoz.noifs.NoIfs1ImperativeExample.match;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class NoIfs1ImperativeExampleTest {

    @Test
    @Parameters({
            "Short string, false, true, Short string",
            "Short string, true, true, Short string",
            "ShOrT StRiNg, true, true, Short string",
            "Short string, false, false, Short",
            "Short string, true, false, Short",
            "ShOrT StRiNg, true, false, ShOrT",
            "OneWord, false, true, OneWord",
            "OneWord, true, true, OneWord",
            "OnEwOrD, true, true, OneWord",
            "OneWord, false, false, One",
            "OneWord, true, false, One",
            "OnEwOrD, true, false, One",
            "A long long string, false, true, A long long string",
            "A long long string, true, true, A long long string",
            "A lOnG loNg sTrInG, true, true, A long long string",
            "A long long string, false, false, string",
            "A long long string, true, false, string",
            "A lOnG loNg sTrInG, true, false, string",
            " , false, true, ",
            " , false, false, ",
            " , true, true, ",
            " , true, false, ",
            "     , false, false, ",
            "     , true, false, ", /* only white chars string contains one white char - case insensitive*/
            ", true, true,", /* empty string equals empty - case insensitive*/
            ", false, true,", /* empty string equals empty - case sensitive*/
            ", true, false,", /* empty string contains empty - case insensitive*/
            ", false, false," /* empty string contains empty - case sensitive*/
    })
    @TestCaseName("should give a positive match({0}, {1}, {2}, {3})")
    public void shouldMatchPositive(String where, boolean ignoreCase, boolean globalMatch, String what) {
        assertTrue(match(where, ignoreCase, globalMatch, what));
    }

    @Test
    @Parameters({
            "Short string, false, true, SHORT STRING",
            "Short string, false, false, SHORT",
            "Short string, false, true, short",
            "Short string, false, true,",
            "Short string, false, true, ",
            "Short string, true, true,    ",
            "Short string, true, true, STRING",
            "Short string, true, false, SHSTR",
            "Short string, true, true, itsnothere",
            "Short string, true, true,",
            "Short string, true, true, ",
            "Short string, true, true,    ",
            ", false, true, notempty",
            ", false, false, notempty",
            ", true, false, notempty",
            ", true, true, notempty",
            "notempty, false, true,",
            "notempty, true, true,"
    })
    @TestCaseName("should give a negative match({0}, {1}, {2}, {3})")
    public void shouldMatchNegative(String where, boolean ignoreCase, boolean globalMatch, String what) {
        assertFalse(match(where, ignoreCase, globalMatch, what));
    }
}