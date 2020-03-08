package io.github.ajoz.typesvtests.illegal.money;

import java.math.BigInteger;
import java.util.function.Function;

/*
  In the previous example we had a simple Money model.
  The Money class was an immutable data class containing
  a BigInteger value and an enum Currency.

  We found an issue that it was possible to add money of
  two different currencies without the compiler notifying
  us about it.

  If the currency is the problematic thing that is used to
  distinguish two different Money instances then why not
  upgrade it to the type level?

  First problem is that the Currency is an enum, but most
  probably we would like to model Money with a generic type
  something like:

  Money<PLN> or Money<CAD> or Money<USD>

  As we cannot somehow link the money type to value of the
  currency type, we will need to upgrade the currency from
  enum to a full blown class
 */
public class ModelingMoney2 {
    /*
      We would like to control the types that extend the
      Currency. We can do this with a sealed type hierarchy.
      In Java this can be done with an abstract class with
      a private constructor. Every inner class of the abstract
      class has access to the outer class private constructor
      this way it is not possible to extend the abstract class.

      The only problem below is the verbose Java syntax.
      A lot of code has to be written to create such sealed
      hierarchy:
     */
    private static abstract class Currency {
        private Currency() {}
        public static final class PLN extends Currency {
        }

        public static final class USD extends Currency {
        }

        public static final class CAD extends Currency {
        }
    }

    // another issue with this is that Java is using
    // generics with type erasure meaning that the information
    // about the currency will disappear after compilation
    private static class Money<T extends Currency> {
        private final BigInteger amount;

        private Money(final BigInteger amount) {
            this.amount = amount;
        }

        public Money<T> plus(final Money<T> other) {
            // let's skip checking for null
            // we do not have to check if currency match
            return new Money<>(this.amount.add(other.amount));
        }

        // it is now possible to do some fancy exchange code
        public <R extends Currency> Money<R> exchange(final Function<Money<T>, Money<R>> exchanger) {
            return exchanger.apply(this);
        }
    }

    public static void main(String[] args) {
        final Money<Currency.PLN> zloty = new Money<>(new BigInteger("1"));
        final Money<Currency.USD> dollar1 = new Money<>(new BigInteger("2"));
        final Money<Currency.USD> dollar2 = new Money<>(new BigInteger("3"));

        // we can do this just fine
        final Money<Currency.USD> dollar3 = dollar1.plus(dollar2);
        // we cannot do this because the compiler won't let us
        // dollar1.plus(zloty);

        // this also has issues with Java generics, just play around
        // with the diamond operator for the return type
        final Money<Currency.USD> exchanged = dollar3.exchange(musd -> {
            final BigInteger exchangeRate = new BigInteger("4");
            return new Money<Currency.USD>(musd.amount.multiply(exchangeRate));
        });
    }
}
