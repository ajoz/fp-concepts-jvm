package io.github.ajoz.sequence;

import java.util.Arrays;

public class SeqTest {
    private static Seq<Integer> fib(final int a, final int b) {
        return new Seq.Cons<>(a, () -> fib(b, a + b));
    }

    public static void main(String[] args) {
//        Seq<Integer> seq =
////                fib(0, 1)
////                        .take(5)
////                        .map(i -> i * 42);
////        for (Integer integer : seq) {
////            System.out.println(integer);
////        }
        Seq<String> seq =
                Seq.of(Arrays.asList("This", "is", "a", "test"))
                    .flatMap(str -> Seq.of(Arrays.asList(str, str)));
        for (String s : seq) {
            System.out.println(s);
        }
    }
}
