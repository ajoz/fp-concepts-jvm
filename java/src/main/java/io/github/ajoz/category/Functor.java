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

  Let's get back to the topic of the "shape" a bit.

  class Functor f where
      fmap :: (a -> b) -> f a -> f b

  The most peculiar thing in the code above is what we called "shape" part. We
  need something that can hold a thing and we will return another something that
  can hold a thing.

  Shape? Let's start with asking what is a type of a type?

  What does it even mean to have a type of a type? To answer this question we
  need to think what is the type of the type constructor. So what is a type
  constructor?

  A type constructor is a special "construct" (pun intended) that creates a
  type. I think about them as "functions" that return types.

  data Bool = True | False

  This Bool here is a type constructor, a very special one that does not take
  an argument, it's called a "nullary" type constructor. This definition above
  introduces two data constructors: True and False. They also do not take
  arguments, meaning they are "nullary" data constructors.

  Haskell is super nice here because it allows looking at the definition in terms
  of sides:

  - left side is type
  - right side is value

  What about Java?

  enum Bool {
      True,
      False
  }

  We can create a similar thing although the syntax is not as clear as in the
  Haskell example.

  Let's define an Optional (Maybe, Option, etc):

  data Optional a = Absent | Present a

  Using the thing that we learned Optional is a type constructor that takes a
  type (some type marked as `a`) and returns a type Optional a

  Present :: a -> Optional a

  Data constructor Present takes a >>value<< of type a and returns a >>value<< of
  type Optional a

  We have the right side covered but what about left side, the type side?

  What is a type of Optional? In type theory it is called a Kind denoted by *.

  - * is the kind of all nullary type constructors
  - * -> * is the kind of unary type constructors
  - * -> * -> * is the kind of binary type constructors

  Our type Bool has the kind *, but our type Optional has the type * -> *

  Why * -> *?

  Let's say we have an Optional String

  String has the kind *
  Optional String has the kind *

  So Optional takes something of kind * and returns something of kind * thus
  Optional has kind * -> *

  Similar thing is happening for a List

  Int :: *
  List Int :: *
  List :: * -> *

  You need concrete types to work with, its not possible to create a real
  List<Optional>

  You may say, but its possible in Java! That is not true because in Java it
  would mean you have a List<Optional<Object>>. It's just a Java way of notation.

  Ok we now know what is a type of type. Let's look at the original Haskell
  definition of the Functor typeclass

  class Functor f where
    fmap :: (a -> b) -> f a -> f b

  What is the type of the f here? It has kind * -> * because it is possible to
  take a and pass to that type constructor f to get concrete type f a.

  So with the Functor class we are saying that we are expecting a type f that
  is a unary type constructor with kind * -> *

  Completely impossible in Java :(

  We can hack our way to get something that looks like this, but we will have to
  squeeze our eyes a bit.

  Let's define our "shape" our "kind" in Java. For this example we will focus on
  unary type constructors.

  Let's call it Kind:
 */
@SuppressWarnings("unused")
interface Kind<F, A> {
    F unwrap();
}

/*
  It's a generic type that takes two generic arguments:
  - F - that is supposed to be our type constructor
  - A - that is supposed to be the type of the argument of the type constructor F

  We do not have much options here, we will have to create instances of this
  Kind<F, A> for any type we want to think as Higher Order Types.

  We have the ability to express the higher kinded type so how would our Java
  Functor look like:
 */

interface FunctorTypeClass<F> {
    <A, B> Kind<F, B> fmap(Function<A, B> function, Kind<F, A> f);
}

/*
  It takes a generic argument of type F, which is our "shape" the type we are
  interested in. The newly created interface declares a single method called
  fmap that returns a Kind<F, B> and takea a function from A to be and a
  Kind<F, A>.

  Cool. Let's use it.

  Let's create an "instance" of this "type class" for a type Optional.

  class OptionalFunctorInstance implements FunctorTypeClass<Optional> {
    @Override
    public <A, B> Kind<Optional, B> fmap(Function<A, B> function, Kind<Optional, A> f) {
        ????????
    }
  }

  But how to implement it? We need to create Kind<Optional, A> first. This is
  tricky now as we need to create this kind and store the value of the Optional<A>
  and then be able to retrieve it back.

  Our "Kind" will be more or less a wrapper to make things easy. We will wrap
  the given type and then return it back. For returning it back we have this
  handy fix() method. It will return us a bare type that would have to be cast
  but this is the price we pay in Java.

  Let's implement OptionalKind:
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class OptionalKind<A> implements Kind<Optional, A> {
    private Optional<A> wrapped;

    // a way to wrap
    public OptionalKind(final Optional<A> wrapped) {
        this.wrapped = wrapped;
    }

    // a way to unwrap
    @Override
    public Optional<A> unwrap() {
        return wrapped;
    }
}

/*
  Let's implement the instance of our Optional Functor:
 */
@SuppressWarnings("unchecked")
class OptionalFunctorInstance implements FunctorTypeClass<Optional> {
    @Override
    public <A, B> Kind<Optional, B> fmap(Function<A, B> function, Kind<Optional, A> f) {
        // the formatting although not super orthodox helps reading this:
        return new OptionalKind<>(
                // as f.unwrap() returns us just a bare Optional, we need to
                // cast it to a Optional<A>, we can do it safely because we
                // know its the only thing that Kind<Optional, A> can hold.
                // Java will cry about the unchecked casts though.
                ((Optional<A>) f.unwrap()).map(function)
        );
    }
}

/*

  Now the fun begins, how to use this monstrosity? It will get ugly!
 */
class Test {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // some type that has a "shape" and contains a another type * -> *
        Optional<String> os = Optional.of("Test");
        // we want to model Higher Kinds
        Kind<Optional, String> osk = new OptionalKind<>(os);

        // we would like to get instance of the type class automatically :-(
        // but we cannot
        FunctorTypeClass<Optional> ofi = new OptionalFunctorInstance();

        // we can now perform the fmap
        Kind<Optional, Integer> oik = ofi.fmap(String::length, osk);

        // we want to get the type back
        Optional<Integer> oi = oik.unwrap();

        System.out.println("value in the optional = " + oi.get());

        /*

        Why all the trouble?

        In Java it's just an exercise but could be a really useful thing if
        properly supported on the language level.

        Would it be nice to add "behaviours" to type defined in some other
        libraries that we do not have access to?

        Let's say you are working with a library Foo. This library has a super
        important type called Bar<A>. You noticed that it could be treated
        as a Functor.

        It would be super cool if you could do:

        final Bar<String> bar = ...

        final Bar<Integer> mappedBar = bar.map(String::length);

        although Bar<A> does not have a map(Function<A, B> func) method defined.

        This would be super useful.

         */
    }
}