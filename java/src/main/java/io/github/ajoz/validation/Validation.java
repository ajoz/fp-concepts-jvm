package io.github.ajoz.validation;

import io.github.ajoz.util.Functor;
import io.github.ajoz.util.Semigroup;

import java.util.function.Function;

public abstract class Validation<E extends Semigroup<E>, A> implements Functor<A> {

    private static final class Success<E extends Semigroup<E>, A> extends Validation<E, A> {
        final A value;

        private Success(final A value) {
            this.value = value;
        }

        @Override
        public <B> Validation<E, B> map(final Function<A, B> function) {
            return new Success<>(function.apply(value));
        }

        public boolean isSuccess() {
            return true;
        }

        public A getValue() {
            return value;
        }

        public E getError() {
            throw new RuntimeException("No ErrorString in success validation!");
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

        public <B> Validation<E, B> map(final Function<A, B> function) {
            return new Failure<>(error);
        }

        public boolean isSuccess() {
            return false;
        }

        public A getValue() {
            throw new RuntimeException("No ErrorString in success validation!");
        }

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

    public abstract A getValue();

    public abstract E getError();

    public abstract <B> Validation<E, B> map(Function<A, B> function);

    public <B> Validation<E, A> apLeft(final Validation<E, B> other) {
        final Validation<E, Function<B, A>> mapped = this.map(Validation.<A, B>constant());
        return ap(mapped, other);
    }

    public <B> Validation<E, B> apRight(final Validation<E, B> other) {
        final Function<Validation<E, A>, Validation<E, Function<B, B>>> vid = mapConst(Function.<B>identity());
        final Validation<E, Function<B, B>> fmapped = vid.apply(this);
        return ap(fmapped, other);
    }

    private static <A, B, E extends Semigroup<E>> Function<Validation<E, B>, Validation<E, A>> mapConst(final A a) {
        return v -> {
            final Function<A, Function<B, A>> constant = constant();
            final Function<B, A> constA = constant.apply(a);
            return v.map(constA);
        };
    }

    private static <A, B> Function<A, Function<B, A>> constant() {
        return a -> (Function<B, A>) ignored -> a;
    }

    public static <E extends Semigroup<E>, A> Validation<E, A> success(final A value) {
        return new Success<>(value);
    }

    public static <E extends Semigroup<E>, A> Validation<E, A> failure(final E error) {
        return new Failure<>(error);
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
