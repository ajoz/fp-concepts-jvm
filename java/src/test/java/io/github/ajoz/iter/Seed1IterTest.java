package io.github.ajoz.iter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.function.Function;

public class Seed1IterTest {
    @Test
    public void shouldReturnTheSeedAsFirstValue() {
        final Integer expected = 42;

        Iter.from(expected, Function.identity())
                .next()
                .ifSuccess(value -> assertEquals(expected, value))
                .ifFailure(exc -> fail("Should return a value!"));
    }

    @Test
    public void shouldReturnASecondValueFromSeed() {
        final Integer seed = 42;
        final Function<Integer, Integer> generator =
                value -> value + 1;

        final Iter<Integer> iter = Iter.from(seed, generator);
        // first value
        iter.next();
        // second value
        iter.next()
                .ifSuccess(value -> assertEquals(generator.apply(seed), value))
                .ifFailure(exc -> fail("Should return a value!"));


    }
}