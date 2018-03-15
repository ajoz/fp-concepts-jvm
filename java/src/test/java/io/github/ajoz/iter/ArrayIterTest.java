package io.github.ajoz.iter;

import io.github.ajoz.util.Try;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ArrayIterTest {
    @Test
    public void shouldNotReturnNextItemForEmptyArray() {
        final Iter<String> iter = new ArrayIter<>();

        final Try<String> next = iter.next();

        assertTrue(next.isFailure());
    }

    @Test
    public void shouldWorkForOneElementArray() {
        final String expected = "42";
        final Iter<String> iter = new ArrayIter<>(expected);

        // first and only one element
        iter.next()
                .ifFailure(exc -> fail("Should retrieve at least one element"))
                .ifSuccess(v -> Assert.assertEquals(expected, v));

        // no more elements available
        iter.next()
                .ifSuccess(ignored -> fail("Should not retrieve more then one element"));
    }

    @Test
    public void shouldWorkForMultipleElements() {
        final Integer[] expected = {1, 2, 3};

        final Iter<Integer> iter = new ArrayIter<>(expected);

        // first
        iter.next()
                .ifFailure(exc -> fail("Should retrieve at first element"))
                .ifSuccess(v -> Assert.assertEquals(expected[0], v));

        // second
        iter.next()
                .ifFailure(exc -> fail("Should retrieve at second element"))
                .ifSuccess(v -> Assert.assertEquals(expected[1], v));

        // second
        iter.next()
                .ifFailure(exc -> fail("Should retrieve at third element"))
                .ifSuccess(v -> Assert.assertEquals(expected[2], v));

        // no more elements available
        iter.next()
                .ifSuccess(ignored -> fail("Should not retrieve more then three elements"));
    }
}