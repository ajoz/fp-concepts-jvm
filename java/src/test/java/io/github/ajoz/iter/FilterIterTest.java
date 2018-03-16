package io.github.ajoz.iter;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

public class FilterIterTest {
    @Test
    public void shouldReturnFailureForEmptyUpstream() {
        Iter.empty()
                .filter(o -> true)
                .next()
                .ifSuccess(ignored -> fail("Should not return a value"));
    }

    @Test
    public void shouldReturnFirstItemThatSatisfiesThePredicate() {
        final String expected = "42";
        Iter.from(expected)
                .filter(o -> true)
                .next()
                .ifFailure(exc -> fail("Should return a value"))
                .ifSuccess(value -> Assert.assertEquals(expected, value));
    }

    @Test
    public void shouldFilterSeveralItems() {
        Iter.from(1, 2, 3, 4, 5)
                .filter(o -> o > 3)
                .next()
                .ifFailure(exc -> fail("Should return a value"))
                .ifSuccess(value -> Assert.assertTrue(value > 3));
    }
}