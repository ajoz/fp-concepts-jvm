package io.github.ajoz.noifs;

import io.github.ajoz.noifs.NoIfs3FunctionalExample.MatchPredicate;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.ajoz.noifs.NoIfs3FunctionalExample.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class NoIfs3FunctionalExampleTest {

    @Test
    @Parameters({
            "",
            "\n",
            "\n\n\n",
            "a",
            "aabb",
            "aAbB",
            "aaaa BBBB cccc DDDD",
            "a\nb\nc"
    })
    @TestCaseName("for \"{0}\" should return \"{0}\"")
    public void shouldReturnTheSameStringForCaseSensitive(String tested) {
        assertThat(caseSensitive.apply(tested), is(tested));
    }

    @Test
    @Parameters({
            "",
            "\n",
            "\n\n\n",
            "a",
            "aabb",
            "aAbB",
            "aaaa BBBB cccc DDDD",
            "a\nb\nc"
    })
    @TestCaseName("for \"{0}\" should return \"{0}\"")
    public void shouldReturnTheSameStringForCaseSensitiveIdx(String tested) {
        assertThat(caseSensitive2.apply(tested), is(tested));
    }

    @Test
    @Parameters({
            ",",
            "\n,\n",
            "\n\n\n,\n\n\n",
            "A,a",
            "aabb,aabb",
            "aAbB,aabb",
            "aaaa BBBB cccc DDDD,aaaa bbbb cccc dddd",
            "A\nB\nC,a\nb\nc"
    })
    @TestCaseName("for \"{0}\" should return \"{1}\"")
    public void shouldReturnLowercaseString(String before, String after) {
        assertThat(caseInsensitive.apply(before), is(after));
    }

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
    public void shouldMatchCaseSensitive(String where, MatchPredicate predicate, String what) {
        assertTrue(match(where, caseSensitive, predicate, what));
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
    public void shouldMatchCaseInsensitive(String where, MatchPredicate predicate, String what) {
        assertFalse(match(where, caseInsensitive, predicate, what));
    }
}