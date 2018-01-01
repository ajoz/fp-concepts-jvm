package io.github.ajoz.validation

data class ErrorMessage(val message: String) : Semigroup<ErrorMessage> {
    override fun append(other: ErrorMessage): ErrorMessage {
        return ErrorMessage(this.message + "\n" + other.message)
    }
}
