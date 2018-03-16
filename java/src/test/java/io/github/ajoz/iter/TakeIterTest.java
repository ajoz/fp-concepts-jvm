package io.github.ajoz.iter;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class TakeIterTest {
    @Test
    public void shouldReturnFailureIfNoItemsUpstream() {
        Iter.empty()
                .take(10)
                .next()
                .ifSuccess(ignored -> fail("Should not return a value!"));
    }

    @Test
    public void shouldReturnFailureIfReturnedAmountOfItems() {
        final Iter<Integer> iter = Iter.from(1, 2, 3, 4).take(2);

        // takes 1
        iter.next();
        // takes 2
        iter.next();
        // no more items to take
        iter.next().ifSuccess(ignored -> fail("Should not return a value"));
    }

    @Test
    public void shouldReturnSuccess() {
        final Integer expected = 42;
        Iter.from(expected)
                .take(1)
                .next()
                .ifFailure(exc -> fail("Should return a value!"))
                .ifSuccess(value -> Assert.assertEquals(expected, value));
    }
}