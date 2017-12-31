package io.github.ajoz.validation;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class SimpleTest {
    private final static Function<Integer, Function<Integer, Integer>> plus =
            first -> (Function<Integer, Integer>) second -> first + second;

    @Test
    public void shouldSumTwoValues() {
        final Validation<ErrorMessage, Function<Integer, Function<Integer, Integer>>> first = Validation.success(plus);
        final Validation<ErrorMessage, Integer> second = Validation.success(1);
        final Validation<ErrorMessage, Integer> third = Validation.success(2);

        final Validation<ErrorMessage, Integer> combined = Validation.ap(Validation.ap(first, second), third);
        final int value = combined.getValue();
        Assert.assertEquals(3, value);
    }
}
