package io.github.ajoz.comprehension;

import io.github.ajoz.util.TriFunction;
import io.github.ajoz.util.Try;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ForComprehension {

    public static void main(String[] args) {
        final Try<String> result2 =
                // we cannot leave this as just ForComprehension2<>
                // because the compiler has issues with resolving
                // the third type just from the result of the lambda
                // passed to yield. It needs to resolve it in the
                // moment of creation of ForComprehension2, if not
                // passed, compiler will resolve everything as
                // ForComprehension2<String, Integer, Object>
                // this makes the mechanism super verbose
                new ForComprehension2<String, Integer, String>(
                        () -> getSomeInfo(),
                        () -> getOtherInfo()
                ).yield(result2processing);

        final Try<String> result3 =
                new ForComprehension3<String, Integer, Boolean, String>(
                        () -> getSomeInfo(),
                        () -> getOtherInfo(),
                        () -> getDifferentInfo()
                ).yield((someInfo, otherInfo, differentInfo) -> {
                    return someInfo + ":" + otherInfo + ":" + differentInfo;
                });

        // when the yield expression is a part of ForComprehension constructor
        final Try<String> result2a =
                new ForComprehension2a<>(
                        () -> getSomeInfo(),
                        () -> getOtherInfo(),
                        result2processing
                ).yield();

        // more complex but resembling Scala's for-comprehension
        // or Haskell's do-notation:
        final Try<String> resultEnhanced3 =
                new ForEnhancedComprehension3<String, Integer, Boolean, String>(
                        () -> Try.success("foo"),
                        // we need to do this to have access to first result
                        text -> Try.success(text.length()),
                        // we need to do this to have access to first and second
                        (text, size) -> Try.success(text.length() == size)
                        // for any "comprehension" with more then 3 elements
                        // this will get problematic, we do not have access to
                        // Function4, Function5 etc
                        /*
                        Besides it does not look like:
                        for {
                            text <- someFunction()
                            size <- otherFunction()
                            flag <- differentFunction()
                        } yield text + ":" + size + ":" + flag
                        :-(
                        Java is not ment for this
                         */
                ).yield(result3processing);
    }

    static TriFunction<String, Integer, Boolean, String> result3processing =
            (someInfo, otherInfo, differentInfo) -> someInfo + ":" + otherInfo + ":" + differentInfo;

    static BiFunction<String, Integer, String> result2processing =
            (someInfo, otherInfo) -> someInfo + ":" + otherInfo;

    static Try<String> getSomeInfo() {
        return Try.success("foo");
    }

    static Try<Integer> getOtherInfo() {
        return Try.success(42);
    }

    static Try<Boolean> getDifferentInfo() {
        return Try.success(false);
    }
}




