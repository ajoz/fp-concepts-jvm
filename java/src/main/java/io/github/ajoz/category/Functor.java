package io.github.ajoz.category;

import java.util.Optional;
import java.util.function.Function;

/*
  What is a Functor? Functor is a map between categories. Let A and B be
  categories and F be functor from A to B. F is a mapping that:
  1) for each object X in category A associates an object F(X) in category B
  2) for each morphism f :: X -> Y in category A associates a morphism F(f) :: F(X) -> F(Y)
    in category B such that following conditions hold:
    -- F(idX) == idF(X) for every X in category A
       this law is to preserve sanity when thinking and expecting what a functor
       does. With idX (identity for X) defined as idX :: X -> X this means that
       passing identity function to a Functor will have the same result as passing
       Functor to identity
    -- F(f . g) == F(f) . F(g) for all morphism f :: X -> Y and g :: Y -> Z in category A
       this rule is to preserve composition of morphisms in the context of a
       Functor. The composition f . g :: X -> Z means that F(f . g) :: F(X) -> F(Z)
       we now need F(X) -> F(Z) be equal to composition of F(X) -> F(Y) with F(Y) -> F(Z)

  Functor shows very well the beauty of Haskell and how FP in Java is very
  cumbersome due to Java's design decisions.

  First some Haskell, we can write a Functor as:

  class Functor f where
      fmap :: (a -> b) -> f a -> f b

  Where is the beauty? It's right before your eyes. We declare a type class Functor
  and 


 */
@FunctionalInterface
public interface Functor<A> {
    <B> Functor<B> map(final Function<A, B> function);
}

/*
  If we would like to define a Functor in Java we have some options:

  First one is a s simple generic type Functor<A> like above. This is probably
  the first thing that comes to mind when trying to transpose the Functor
  concept to Java grounds.

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
      BazFunctor<B> but still we are unable to achieve the change we want

      There is also another problem, class can pass anything implementing
      Functor instead itself. With such Functor definition there is no
      way to guarantee the correct return type.
     */
}

/*
  We can find another way of defining a functor if we look at how Haskell
  defines it:

  class Functor f where
      fmap :: (a -> b) -> f a -> f b

  Let's focus on the `f a` part. In the previous attempts we created something
  (using Java-ish notation) that looks like:

  fmap :: (a -> b) -> Functor<A> -> Functor<B>

  This is not exactly correct as the Haskell version distinguishes between
  some type `f` (a shape) that can contain a type `a` or `b` and by the
  Functor class defines how this shape `f` should behave to "be a functor".

  In Java this would mean we would need something like:

  interface Functor<F<A>> {
      <B> F<B> fmap(Function<A, B> mapper);
  }

  The problem is that both `F` and `A` or `B` are generic. This means that we
  need a "type" that would allow us to specify that there is some specific type
  and it has a certain shape, this shape should be able to hold a type be
  "container-like".

  interface Shape1<T, A> {

  }

  Let's get back to the Functor definition:

  class Functor f where
      fmap :: (a -> b) -> f a -> f b


  Using our Java "shape" type it could look like:

  interface Functor<F> {
      <A, B> Shape1<F, B> fmap(Function<A, B> fun, Shape1<F, A> f)
  }

  This looks more like the Haskell version, let's remove the noise and leave
  the necessary thins:

  Functor<F> {
     <F, B> fmap(Function<A, B>, <F, A>)
  }

  Ok this is nice and all but how to use it? It seems super detached from the
  class hierarchy comparing to the first definition. This is correct! it is
  super detached because it is detached. Having the Functor defined as

  interface Functor<F> {
      <A, B> Shape1<F, B> fmap(Function<A, B> fun, Shape1<F, A> f)
  }

  Allows us to create "functors" for types that already are defined in different
  libraries and we cannot modify their sources.

  Let's get back to the topic of the "shape" a bit 

 */
@SuppressWarnings("unused")
interface Kind<F, A> {
    F fix();
}

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class OptionalKind<A> implements Kind<Optional, A> {
    Optional<A> wrapped;

    private OptionalKind(final Optional<A> wrapped) {
        this.wrapped = wrapped;
    }

    public static <A> Kind<Optional, A> wrap(final Optional<A> optional) {
        return new OptionalKind<>(optional);
    }

    public static <A> Optional<A> unwrap(final Kind<Optional, A> optionalKind) {
        //noinspection unchecked
        return (Optional<A>) optionalKind.fix();
    }

    @Override
    public Optional fix() {
        return wrapped;
    }
}

interface FunctorTypeClass<F> {
    <A, B> Kind<F, B> fmap(Function<A, B> function, Kind<F, A> f);
}

@SuppressWarnings("unchecked")
class OptionalFunctorInstance implements FunctorTypeClass<Optional> {
    @Override
    public <A, B> Kind<Optional, B> fmap(Function<A, B> function, Kind<Optional, A> f) {
        return OptionalKind.wrap(((Optional<A>) f.fix()).map(function));
    }
}

class Test {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // some type that has a "shape" and contains a another type * -> *
        Optional<String> os = Optional.of("Test");
        // we want to model Higher Kinds
        Kind<Optional, String> osk = OptionalKind.wrap(os);

        // we would like to get instance of the type class automatically :-(
        // but we cannot
        FunctorTypeClass<Optional> ofi = new OptionalFunctorInstance();

        // we can now perform the fmap
        Kind<Optional, Integer> oik = ofi.fmap(String::length, osk);

        // we want to get the type back
        Optional<Integer> oi = OptionalKind.unwrap(oik);

        System.out.println("value in the optional = " + oi.get());
    }
}