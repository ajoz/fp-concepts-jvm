package io.github.ajoz.sequence;

public class SeqTest {
    private static Seq<Integer> fib(final int a, final int b) {
        return new Seq.Cons(a, () -> fib(b, a + b));
    }

    public static void main(String[] args) {
        Seq<Integer> seq = fib(0, 1).take(10);
        for (Integer integer : seq) {
            System.out.println(integer);
        }
    }
}
