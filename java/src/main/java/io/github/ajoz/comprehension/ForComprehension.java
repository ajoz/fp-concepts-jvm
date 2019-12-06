package io.github.ajoz.comprehension;

import io.github.ajoz.util.Try;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ForComprehension {

    public static void main(String[] args) {
        Try<Object> result2 =
                new ForComprehension2<>(
                        () -> getSomeInfo(),
                        () -> getOtherInfo()
                ).yield((someInfo, otherInfo) -> {
                    return someInfo + ":" + otherInfo;
                });

        Try<Object> result3 =
                new ForComprehension3<>(
                        () -> getSomeInfo(),
                        () -> getOtherInfo(),
                        () -> getDifferentInfo()
                ).yield((someInfo, otherInfo, differentInfo) -> {
                    return someInfo + ":" + otherInfo + ":" + differentInfo;
                });
    }

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




