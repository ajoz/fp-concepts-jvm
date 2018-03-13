package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

// an example of a Iterator<T> without the need for specialized
// hasNext :: () -> boolean method
// the name Iter, so it's not confused with Iterator
// also didn't choose Seq, Sequence or Stream for similar reason
public interface Iter<T> {
    Try<T> next();
}
