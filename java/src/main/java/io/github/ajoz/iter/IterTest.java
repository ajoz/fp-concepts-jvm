package io.github.ajoz.iter;

public class IterTest {
    public static void main(String[] args) {
        final Iter<String> strings = new ArrayIter<>("This", "is", "a", "basic", "test");
        final Iter<Integer> mapped = new MapIter<>(strings, String::length);
        final Iter<Integer> filtered = new FilterIter<>(mapped, length -> length > 2);
        Iters.forEach(System.out::println, filtered);
    }
}
