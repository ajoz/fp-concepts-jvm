package io.github.ajoz.iter;

import java.util.LinkedList;
import java.util.List;

public final class Iters {

    public static <T> List<T> toList(final Iter<T> iter) {
        final List<T> list = new LinkedList<>();
        for (final T item : iter) {
            list.add(item);
        }
        return list;
    }

}
