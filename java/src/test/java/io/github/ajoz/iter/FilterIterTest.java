package io.github.ajoz.iter;

import static org.junit.Assert.fail;
import org.junit.Test;

public class FilterIterTest {
    @Test
    public void shouldReturnFailureForEmptyUpstream() {
        final Iter<Object> iter = Iter.empty();

        iter.filter(o -> true)
                .next()
                .ifSuccess(ignored -> fail("Should not return a value"));
    }

}