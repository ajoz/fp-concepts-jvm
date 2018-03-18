package io.github.ajoz.validation;

import static io.github.ajoz.util.Functions.constant;
import io.github.ajoz.category.Functor;
import io.github.ajoz.category.Semigroup;
import static java.util.function.Function.identity;

import java.util.function.Function;

public abstract class Validation<E extends Semigroup<E>, A> implements Functor<A> {

    private static final class Success<E extends Semigroup<E>, A> extends Validation<E, A> {
        final A value;

        private Success(final A value) {
            this.value = value;
        }

        // map :: (a -> b) -> Validation err a -> Validation err b
        @Override
        public <B> Validation<E, B> map(final Function<A, B> function) {
            return new Success<>(function.apply(value));
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public A getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Validation.Success{ value=" + value + " }";
        }
    }

    private static final class Failure<E extends Semigroup<E>, A> extends Validation<E, A> {
        final E error;

        private Failure(final E error) {
            this.error = error;
        }

        @Override
        public <B> Validation<E, B> map(final Function<A, B> function) {
            return new Failure<>(error);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public E getError() {
            return error;
        }

        @Override
        public String toString() {
            return "Validation.Failure{ error=" + error + " }";
        }
    }

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    public A getValue() {
        throw new RuntimeException("No value element available!");
    }

    public E getError() {
        throw new RuntimeException("No error element available!");
    }

    // to workaround Java type system and make the Functor.map method return a Validation
    // we need to override it with a return type that is derived from Functor
    @Override
    public abstract <B> Validation<E, B> map(Function<A, B> function);

    /*
     I call this apLeft, in Haskell it is defined as:
     (<*) :: Validation err a -> Validation err b -> Validation err a

     This seems strange at first but it's supposed to run both Validations and
     return a Validation that contains type `a`.

     The implementation looks complicated all because of how Java is processing
     "generics" and that it's not possible to have real standalone generic functions
     unless we do an "object-like" implementation. Here we have a constant() method
     that returns a function of the form:

     constant :: a -> b -> a

     In case of Java currying is a bit cumbersome so constant() returns

     Function<A, Function<B, A>> constant() { //...

     We will go through the operations, one by one:

     1. map(constant())
     We have a Validation<Err, A> and we map over it with a Function<A, Function<B, A>>
     So as a result we get a Validation<Err, Function<B, A>>

     2. ap(map(constant(), other)
     We are calling ap function with Validation<Err, Function<B, A>> and Validation<Err, B>
     as the arguments. As we know the stored Function<B, A> will always return the value of
     type `A` thus we will get a result of type Validation<Err, A>
     */
    public <B> Validation<E, A> apLeft(final Validation<E, B> other) {
        return ap(map(constant()), other);
    }

    /*
     I call this apRight, in Haskell it is defined as:
     (*>) :: Validation err a -> Validation err b -> Validation err b

     Such function is generalized in Control.Applicative package
     (*>) :: f a -> f b -> f b
     a1 *> a2 = (id <$ a1) <*> a2

     Java implementation won't be as concise. As we already know the <*> is the "angry mom" operator
     in our Validation implementation called ap. We used it already for apLeft. This
     <$ function is doing a map . const over the given value.

     (<$) :: a -> f b -> f a
     (<$) =  fmap . const

     Angry mom operator needs a function wrapped in an Applicative Functor, in this
     case we need a Validation<Err, Function<A, B>>

     fmap takes function as first arguments and a functor as a second
     const takes a value `a` as a first argument and returns a function b -> a

     fmap . const is a simple composition

     The beauty haskell syntax starts to shine here as

     (id <$ Validation<Err, A>) will get us Validation<Err, Function<T, T>> I deliberately
     used T for the type to indicate its the identity function and it can take any type.

     So Validation<Err, Function<T, T>> <*> Validation<Err, B> will give us Validation<Err, B>
     as identity for value of type B will return this value of type B
     */
    public <B> Validation<E, B> apRight(final Validation<E, B> other) {
        final Function<Validation<E, A>, Validation<E, Function<B, B>>> vid = mapConst(identity());
        final Validation<E, Function<B, B>> fmapped = vid.apply(this);
        return ap(fmapped, other);
    }

    public static <E extends Semigroup<E>, A> Validation<E, A> success(final A value) {
        return new Success<>(value);
    }

    public static <E extends Semigroup<E>, A> Validation<E, A> failure(final E error) {
        return new Failure<>(error);
    }

    public static <A, B, E extends Semigroup<E>> Function<Validation<E, B>, Validation<E, A>> mapConst(final A a) {
        return validation -> {
            final Function<A, Function<B, A>> constant = constant();
            final Function<B, A> constA = constant.apply(a);
            return validation.map(constA);
        };
    }

    @SuppressWarnings("ConstantConditions")
    public static <E extends Semigroup<E>, A, B> Validation<E, B> ap(final Validation<E, Function<A, B>> first,
                                                                     final Validation<E, A> second) {
        // Failure e1 `ap` Failure e2 = Failure (e1 <> e2)
        if (first instanceof Failure && second instanceof Failure) {
            final Failure<E, Function<A, B>> failure1 = (Failure<E, Function<A, B>>) first;
            final Failure<E, A> failure2 = (Failure<E, A>) second;
            final E e1 = failure1.error;
            final E e2 = failure2.error;
            return new Failure<>(e1.append(e2));
        }
        // Failure e1 `ap` Success _ = Failure e1
        else if (first instanceof Failure && second instanceof Success) {
            final Failure<E, Function<A, B>> failure = (Failure<E, Function<A, B>>) first;
            final E e1 = failure.error;
            return new Failure<>(e1);
        }
        // Success _ `ap` Failure e2 = Failure e2
        else if (first instanceof Success && second instanceof Failure) {
            final Failure<E, A> failure2 = (Failure<E, A>) second;
            final E e2 = failure2.error;
            return new Failure<>(e2);
        }
        // Success f `ap` Success a = Success (f a)
        else {
            final Success<E, Function<A, B>> success1 = (Success<E, Function<A, B>>) first;
            final Success<E, A> success2 = (Success<E, A>) second;
            final Function<A, B> f = success1.value;
            final A a = success2.value;
            return new Success<>(f.apply(a));
        }
    }
}
