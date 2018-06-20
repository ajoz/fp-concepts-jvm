package io.github.ajoz.category;

import java.util.function.Function;

@FunctionalInterface
public interface Functor<A> {
    <B> Functor<B> map(final Function<A, B> function);
}

/*
  Functor shows very well the beauty of Haskell and how FP in Java is very
  cumbersome due to Java's design decisions. If we would like to define a
  Functor in Java we have some options:

  1) Simple generic type Functor<A> like above. This is probably the first thing
  that comes to mind when trying to transpose the Functor concept to Java grounds.

  As Functor is a simple "structure" a certain "shape" that a map function can
  work on then the basic definition above should be sufficient? Or maybe not?
 */

class FooFunctor<A> implements Functor<A> {
    /*
      There is a problem here as we get a map that returns a Functor instance
      and we would like to return a FooFunctor instance instead.

      @Override
      public <B> Functor<B> map(final Function<A, B> function) {
        // some implementation
      }

      This can be solved by just changing Functor<B> to FooFunctor<B> everything
      compiles fine:
     */
    @Override
    public <B> FooFunctor<B> map(final Function<A, B> function) {
        return new FooFunctor<>();
    }

    /*
      This is super wonky, as it needs to be manually changed. We could pass
      information about the expected type but not without a loss of information
      due to how the generics are inferred.

      First let's try this:

      interface Functor<A, F extends Functor<A, F>> {
          <B> F map(final Function<A, B> function);
      }

      Definition for generic Functor like this allows us to cause the implementation
      to automatically return the type we need but there is a problem with it:

      class BarFunctor<A> implements Functor<A, BarFunctor<A>> {
          @Override
          public <B> BarFunctor<A> map(Function<A, B> function) {
              // implementation
          }
      }

      We need the "shape" to be "of B" but we get one "of A". As we cannot define
      "B" at this point we can use wildcards:

      interface Functor<A, F extends Functor2<?, ?>> {
          <B> F map(final Function<A, B> function);
      }

      There is a small problem now as

      class BazFunctor<A> implements Functor2<A, BazFunctor<?>> {
         @Override
         public <B> BazFunctor<?> map(final Function<A, B> function) {
             // implementation
         }
      }

      We get the "shape" that is "of ?" we can manually change it to
      BazFunctor<B> but this does not make this automatically.

      There is also another problem, class can pass anything implementing
      Functor instead itself. With such Functor definition there is no
      way to guarantee the correct return type.
     */
}
