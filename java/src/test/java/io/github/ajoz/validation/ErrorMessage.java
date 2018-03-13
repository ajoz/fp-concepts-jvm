package io.github.ajoz.validation;

import io.github.ajoz.category.Semigroup;

import java.util.Objects;

class ErrorMessage implements Semigroup<ErrorMessage> {
    private final String message;

    // Needs a nonempty string!!
    ErrorMessage(final String message) {
        this.message = message;
    }

    public ErrorMessage append(final ErrorMessage other) {
        return new ErrorMessage(this.message + "\n" + other.message);
    }

    @Override
    public String toString() {
        return "ErrorMessage{ '" + message + '\'' + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
