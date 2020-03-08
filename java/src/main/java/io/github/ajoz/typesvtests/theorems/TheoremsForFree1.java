package io.github.ajoz.typesvtests.theorems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/*
  C([A, B]) = C(A) * C(B)
  the cardinality of the product is the product of cardinalities
 */
class Product<A, B> {
    final A first;
    final B second;

    Product(final A first,
            final B second) {
        this.first = first;
        this.second = second;
    }
}
/*
  C(A | B) = C(A) + C(B)
  the cardinality of the sum is the sum of cardinalities

  For Optional we can calculate the cardinality as:
  Optional<A> = Some(A) | None
  The None has only one inhabitant
  C(Optional<A>) = C(A) + 1

  So for example cardinality of a Boolean is 2 because there are two instances of it True and False
  C(Optional<Boolean>) = 2 + 1 = 3
  2 from Boolean
  1 from None
  So 3 possible inhabitants
 */
abstract class Sum<A, B> {
    abstract <C> C match(final Function<A, C> onLeft,
                         final Function<B, C> onRight);

    public static class Left<A, B> extends Sum<A, B> {
        final A left;

        Left(final A left) {
            this.left = left;
        }

        @Override
        <C> C match(final Function<A, C> onLeft,
                    final Function<B, C> onRight) {
            return onLeft.apply(left);
        }
    }

    public static class Right<A, B> extends Sum<A, B> {
        final B right;

        Right(final B right) {
            this.right = right;
        }

        @Override
        <C> C match(final Function<A, C> onLeft,
                    final Function<B, C> onRight) {
            return onRight.apply(right);
        }
    }
}

public class TheoremsForFree1 {
    // A * (B | C) -> (A * B) | (B * C)
    static <A, B, C> Sum<Product<A, B>, Product<A, C>> theorem1(final Product<A, Sum<B, C>> product) {
        return product.second.match(
                /*
                  if the Sum<B, C> (B | C) is left aligned (stores B) then
                  we need to return Product<A, B> (A * B), in the result
                  we want the (A * B) to be left aligned then we need to
                  return Sum.Left
                */
                (B left) -> new Sum.Left<>(new Product<>(product.first, left)),
                /*
                  if the Sum<B, C> (B | C) is right aligned (stores C) then
                  we need to return Product<A, C> (A * C), in the result
                  we want the (A * B) to be right aligned then we need to
                  return Sum.Right
                 */
                (C right) -> new Sum.Right<>(new Product<>(product.first, right))
        );
    }

    static <A> Optional<List<A>> theorem2(final List<Optional<A>> optionals) {
        final List<A> result = new ArrayList<>();
        for (final Optional<A> optional : optionals) {
            optional.ifPresent(result::add);
        }
        return Optional.of(result);
    }
}
