package io.github.ajoz.util;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The Try type represents a computation that may either result in an exception, or return a successfully computed value.
 * It closely resembles scala's implementation. Instances of {@link Try} are either {@link Success} or {@link Failure}
 * instance.
 * <pre>
 * {@code
 *
 * }
 * </pre>
 *
 * @param <T>
 */
public abstract class Try<T> {
    /**
     * Although {@link Try} is an abstract class we only want it to have to subclasses in the form of {@link Success}
     * and {@link Failure}. Constructor is marked as private so it's only available for private inner classes.
     */
    private Try() {
    }

    /**
     * @param recovery
     * @return
     */
    public abstract Try<T> recover(final Function<Throwable, T> recovery);

    /**
     * @param recoveryWith
     * @return
     */
    public abstract Try<T> recoverWith(final Function<Throwable, Try<T>> recoveryWith);

    /**
     * @param predicate
     * @return
     */
    public abstract Try<T> filter(final Function<T, Boolean> predicate);

    /**
     * @param predicate
     * @return
     */
    public abstract Try<T> filter(final Predicate<T> predicate);

    /**
     * @param function
     * @param <U>
     * @return
     */
    public abstract <U> Try<U> flatMap(final Function<T, Try<U>> function);

    /**
     * @param function
     * @param <U>
     * @return
     */
    public abstract <U> Try<U> map(final Function<T, U> function);

    /**
     * @param defaultValue
     * @return
     */
    public abstract T getOrElse(T defaultValue);

    /**
     * @param value
     * @param <U>
     * @return
     */
    public static <U> Try<U> fromNullable(final U value) {
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
    }
}


