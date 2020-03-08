package io.github.ajoz.typesvtests.illegal.money;

import java.math.BigInteger;

/*
  How do we usually model the money in our code?

  In most code bases and APIs I've seen it was a combination of
  a double and a string, first was expressing amount and second
  was the currency code.

  Like you can imagine this is very dangerous as
  - double has a nice precision but its use for money value is questionable
  - nothing forbids us to put a Double.NaN as a value :(
  - string as a currency code holder is a poor choice because we can put
    anything there

  Also a common way of modeling money is combining value and currency into
  a single type, below you have an example of such modeling attempt.
 */
public class ModelingMoney1 {

    /*
      Usual way of expressing the currency is a simple enum, is it correct though?
     */
    private enum Currency {
        PLN,
        USD,
        CAD
        // other ISO codes etc
    }

    private static class Money {
        // We can argue which type is best for expressing the amount
        // especially if we think about the lowest possible representation
        // of a certain currency e.g. PLN has the lowest indivisible in the
        // form of "Grosz", USD has "Cent" etc
        // using float might be problematic in case of any rounding issues
        // we would not like to make money disappear or appear out of
        // nowhere, the best idea would be to use integers then
        private final BigInteger amount;
        private final Currency currency;

        public Money(final BigInteger amount,
                     final Currency currency) {
            this.amount = amount;
            this.currency = currency;
        }

        /*
          Interesting thing is the set of operations that we allow to perform on our type.
          Let's focus only on the simple operation of adding two instances of our type

          Except the usual issue of null input, there can be a case where one could try
          to add two values of different currencies. We can solve this in two different
          ways:
          - throw an exception when the input is incorrect
          - extend the codomain of the function to return an Optional, Maybe, Try etc

          First one is ugly because if we will throw a checked exception then using the
          API will be very cumbersome. Throwing an unchecked exception then such API
          becomes a surprise if something fails in the runtime.

          Second one is also ugly for anyone not used to FP, as they will have to now
          chain calls to flatMap and a simple addition of two values is not so simple
          as it should be.
         */
        public Money plus(final Money other) {
            if (null == other)
                throw new IllegalArgumentException("Cannot add a null!");

            // why do I use explicit this? simply because it reads better
            // if java had only a isNotEqual method available that would be just
            // a wrapper for !equals I would be in heaven.
            // "if this currency is not equal to other currency" == beauty!!
            if (!this.currency.equals(other.currency))
                throw new IllegalArgumentException("Cannot add money of different currencies");

            return new Money(this.amount.add(other.amount), currency);
        }
    }

    public static void main(final String[] args) {
        final Money dolars1 = new Money(new BigInteger("1"), Currency.USD);
        final Money dolars2 = new Money(new BigInteger("2"), Currency.USD);
        final Money zlotys = new Money(new BigInteger("10"), Currency.PLN);

        // although we can do something that is reasonable:
        final Money dolars3 = dolars1.plus(dolars2);
        // the compiler will not say anything if we will try to do:
        final Money explode = dolars1.plus(zlotys);
        // can we do something about it?
    }
}
