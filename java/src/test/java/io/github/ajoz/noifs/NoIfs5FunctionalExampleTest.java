package io.github.ajoz.noifs;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.ajoz.noifs.NoIfs5FunctionalExample.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class NoIfs5FunctionalExampleTest {

    @Test
    @Parameters({
            "Short string, Short string",
            "OneWord, OneWord",
            "A long long string, A long long string",
            " , ",
            ",",
    })
    @TestCaseName("should match equals({0}, {1}) for case sensitive")
    public void shouldMatchEqualsForCaseSensitive(final String where,
                                                  final String what) {
        assertTrue(match.apply(where, equals, what));
    }

    @Test
    @Parameters({
            "Short string, Short",
            "OneWord, One",
            "A long long string, string",
            " , ",
            "     , ",
            ","
    })
    public void shouldMatchContainsForCaseSensitive(final String where,
                                                    final String what) {
        assertTrue(match.apply(where, contains, what));
    }

    @Test
    @Parameters({
            "Short string,SHOT STRING",
            ", notempty",
    })
    @TestCaseName("should match({0}, {1}) for case insensitive")
    public void shouldMatchContainsForCaseInsensitive(final String where,
                                                      final String what) {
        assertFalse(caseInsensitive.apply(match).apply(where, contains, what));
    }

    @Test
    @Parameters({
            "Short string, SHORT",
            "Short string, short",
            "Short string,",
            "Short string, ",
            ", notempty",
            "notempty,"
    })
    @TestCaseName("should match({0}, {1}) for case insensitive")
    public void shouldMatchEqualsForCaseInsensitive(final String where,
                                                    final String what) {
        assertFalse(caseInsensitive.apply(match).apply(where, equals, what));
    }
}