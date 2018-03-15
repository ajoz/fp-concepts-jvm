package io.github.ajoz.iter;

import static org.junit.Assert.fail;
import org.junit.Test;

public class EmptyIterTest {
    @Test
    public void shouldAlwaysNotReturnAValue() {
        final Iter<String> iter = new EmptyIter<>();

        iter.next().ifSuccess(ignored -> fail("Should not return a value"));
        iter.next().ifSuccess(ignored -> fail("Should not return a value"));
        iter.next().ifSuccess(ignored -> fail("Should not return a value"));
        iter.next().ifSuccess(ignored -> fail("Should not return a value"));
    }
}