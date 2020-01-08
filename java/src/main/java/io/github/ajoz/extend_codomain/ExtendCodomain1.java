package io.github.ajoz.extend_codomain;

import java.util.Optional;

public class ExtendCodomain1 {
    /*
      How can we handle incorrect inputs?
     */
    public Integer divide(final Integer a, final Integer b) {
        return a / b; // Let's skip the discussions if it should be a float or not
    }
    /*
      We can extend the codomain of the function:
     */
    public Optional<Integer> divide2(final Integer a, final Integer b) {
        if (b == 0)
            return Optional.empty();
        return Optional.of(a / b);
    }
}
