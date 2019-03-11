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
        Seq<String> seq1 = Seq.of(Arrays.asList("This", "is", "a", "test"));
        Seq<Integer> seq2 = Seq.of(Arrays.asList(1, 2, 3, 4, 5, 6));

        Seq<String> seq3 = seq1.zip(seq2, (s, integer) -> s + ":" + integer);

        for (String s : seq3) {
            System.out.println(s);
        }
    }
}
