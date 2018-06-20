package io.github.ajoz.category;

import java.util.function.BiFunction;

/*
  Foldable describes a certain structure (shape). I prefer to not use the word
  "container" as it in most cases makes one think about a basic List or an Array.
  Not only a List, Stream, Sequence or any other "Array"-like data structure can
  be a Foldable.

  Anything that can, based on the initial value and a two argument function
  produce a value and fold it's internal structure can be thought as Foldable.

  It's super easy to think about a List as Foldable, we can take a each item
  and use the folding function to get a single value at the end.

  For a list:
  [1, 2, 3, 4]

  And function:
  sum x y = x + y

  We can sum all elements of the list, we only need the initial value. Identity
  (neutral) value for addition is 0 (zero). We can use 0 as the initial value
  because the result won't be affected by it.

  Integer sum(final Foldable<Integer> foldable) {
      return foldable.fold(0, (x, y) -> x + y);
  }

  Any Foldable has a representation as a list. It's simple if we define
  an append function for a list, that takes a List and a single element.
  A neutral value for a List is empty List

  <A> List<A> toList(final Foldable<A> foldable) {
      // emptyList has to be mutable so we cannot use Collections.emptyList()
      return foldable.fold(emptyList, (list, item) -> {
            // java lists are mutable and the API is wonky
            list.add(item); //
            return list;
      });
  }

  There is a certain beauty in fold (which I mentioned in fold examples) as
  Functors map can be expressed in terms of fold.
 */
@FunctionalInterface
public interface Foldable<A> {
    <B> B fold(final B initial, final BiFunction<A, B, B> function);
}
