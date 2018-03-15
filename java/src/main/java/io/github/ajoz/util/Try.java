package io.github.ajoz.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Try<T> {
    private Try() {
    }

    public abstract Try<T> recover(final Function<Throwable, T> recovery);

    public abstract Try<T> recoverWith(final Function<Throwable, Try<T>> recoveryWith);

    public abstract Try<T> filter(final Function<T, Boolean> predicate);

    public abstract Try<T> filter(final Predicate<T> predicate);

    public abstract <U> Try<U> flatMap(final Function<T, Try<U>> function);

    public abstract <U> Try<U> map(final Function<T, U> function);

    public abstract T getOrElse(T defaultValue);

    public abstract T get();

    public abstract boolean isSuccess();

    public Try<T> ifSuccess(final Consumer<T> action) {
        return this;
    }

    public Try<T> ifFailure(final Consumer<Throwable> action) {
        return this;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public static <U> Try<U> ofSupplier(final CheckedSupplier<U> supplier) {
        try {
            // a checked supplier is better as we can wrap API that throws an
            // exception explicitely
            return Try.success(supplier.get());
        } catch (final Exception exc) {
            return Try.failure(exc);
        }
    }

    public static <U> Try<U> ofNullable(final U value) {
        if (null != value) {
            return Try.success(value);
        }

        return Try.failure(new NullPointerException("Tries to create a try from null value "));
    }

    public static <U> Try<U> success(final U value) {
        return new Success(value);
    }

    public static <U> Try<U> failure(final Throwable throwable) {
        return new Failure(throwable);
    }

    private static class Success<T> extends Try<T> {
        private T value;

        Success(final T value) {
            this.value = value;
        }

        @Override
        public Try<T> recover(final Function<Throwable, T> recovery) {
            return Try.success(value);
        }

        @Override
        public Try<T> recoverWith(final Function<Throwable, Try<T>> recoveryWith) {
            return Try.success(value);
        }

        @Override
        public Try<T> filter(final Function<T, Boolean> predicate) {
            if (predicate.apply(value)) {
                return Try.success(value);
            } else {
                return Try.failure(
                        new IllegalArgumentException(
                                String.format("Value %s haven't satisfied the predicate", value, predicate)
                        )
                );
            }
        }

        @Override
        public Try<T> filter(final Predicate<T> predicate) {
            if (predicate.test(value)) {
                return Try.success(value);
            } else {
                return Try.failure(
                        new IllegalArgumentException(
                                String.format("Value %s haven't satisfied the predicate: %s", value, predicate)
                        )
                );
            }
        }

        @Override
        public <U> Try<U> flatMap(final Function<T, Try<U>> function) {
            return function.apply(value);
        }

        @Override
        public <U> Try<U> map(final Function<T, U> function) {
            return Try.success(function.apply(value));
        }

        @Override
        public T getOrElse(final T defaultValue) {
            return value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public Try<T> ifSuccess(final Consumer<T> action) {
            action.accept(value);
            return this;
        }
    }

    private static class Failure<T> extends Try<T> {
        private Throwable throwable;

        Failure(final Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public Try<T> recover(final Function<Throwable, T> recovery) {
            return Try.success(recovery.apply(throwable));
        }

        @Override
        public Try<T> recoverWith(final Function<Throwable, Try<T>> recoveryWith) {
            return recoveryWith.apply(throwable);
        }

        @Override
        public Try<T> filter(final Function<T, Boolean> predicate) {
            return Try.failure(throwable);
        }

        @Override
        public Try<T> filter(final Predicate<T> predicate) {
            return Try.failure(throwable);
        }

        @Override
        public <U> Try<U> flatMap(final Function<T, Try<U>> function) {
            return Try.failure(throwable);
        }

        @Override
        public <U> Try<U> map(final Function<T, U> function) {
            return Try.failure(throwable);
        }

        @Override
        public T getOrElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public T get() {
            throw new RuntimeException("No such element!");
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public Try<T> ifFailure(final Consumer<Throwable> action) {
            action.accept(throwable);
            return this;
        }
    }
}


