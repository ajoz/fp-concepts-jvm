package io.github.ajoz.narrow_domain;

import java.util.Optional;

public class NarrowDomain1 {
    /*
      We often test the functions with a large domain.
      What does it mean? Basically the types we are using
      contain values which are not matching the constraints
      we envisioned for our function.

      Most often a function first checks if the values
      passed are correct i.e. in range.

      Let's look at a simple example of a division function
      that takes an Integer:
     */
    public Integer divide(final Integer a, final Integer b) {
        return a / b; // Let's skip the discussions if it should be a float or not
    }
}


/*
  Obvious issue is the attempt to divide by zero. By invoking the code
  divide(1, 0) we will get a simple java.lang.ArithmeticException: / by zero
  as a result.
  Is this the intended result? Instead of creating unit tests that check
  if the input passed is correct or adding preconditions to the code we
  can fix it by narrowing the domain.

  Integer as a type is a bit too large, we would like to exclude the 0
  We need a smaller type. A type that holds everything a normal Integer
  would but without a 0.
 */
final class NonZeroInteger extends Number {
    private final Integer value;

    private NonZeroInteger(final Integer value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public static Optional<NonZeroInteger> of(final Integer value) {
        if (value == 0)
            return Optional.empty();
        return Optional.of(new NonZeroInteger(value));
    }
}

/*
  With the new specialized type we can now simply change the signature of the divide
  function:
 */
class NarrowedDomain {
    public Integer divide(final Integer a, final NonZeroInteger b) {
        return a / b.intValue();
    }
}
/*
  We have pushed the responsibility for the correctness backward. We are telling the caller
  of the function what value we expect. We are more strict now.
 */
