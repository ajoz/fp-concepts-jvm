package io.github.ajoz.fold;

import org.junit.Test;

import java.util.List;

import static io.github.ajoz.fold.Fold6FunctionalLibrary.sumLeftRecursive;
import static io.github.ajoz.fold.Fold6FunctionalLibrary.sumRightRecursive;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class Fold6FunctionalLibraryTest {
    @Test
    public void shouldSumWithFoldLeftRecursively() {
        //GIVEN:
        final List<Integer> list = asList(1, 3, 5, 7);
        final Integer expected = 16;
        //WHEN:
        final Integer actual = sumLeftRecursive(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldSumWithFoldLeftRecursivelyAnEmptyList() {
        //GIVEN:
        final List<Integer> list = emptyList();
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sumLeftRecursive(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldSumWithFoldRightRecursively() {
        //GIVEN:
        final List<Integer> list = asList(1, 3, 5, 7);
        final Integer expected = 16;
        //WHEN:
        final Integer actual = sumRightRecursive(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldSumWithFoldRightRecursivelyAnEmptyList() {
        //GIVEN:
        final List<Integer> list = emptyList();
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sumRightRecursive(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldSumFromLeftBeEqualToSumFromRightForEmptyList() {
        //GIVEN:
        final List<Integer> list = emptyList();
        //WHEN:
        final Integer left = sumLeftRecursive(list);
        final Integer right = sumRightRecursive(list);
        //THEN:
        assertThat(left, is(right));
    }

    @Test
    public void shouldSumFromLeftBeEqualToSumFromRightForTheSameList() {
        //GIVEN:
        final List<Integer> list = asList(1, 2, 3, 4, 5);
        //WHEN:
        final Integer left = sumLeftRecursive(list);
        final Integer right = sumRightRecursive(list);
        //THEN:
        assertThat(left, is(right));
    }

    @Test
    public void shouldSumLeftBeEqualToSumRightForDifferentLists() {
        //GIVEN:
        final List<Integer> list1 = asList(1, 2, 3, 4);
        final List<Integer> list2 = asList(1, 2, 3);
        //WHEN:
        final Integer left = sumLeftRecursive(list1);
        final Integer right = sumRightRecursive(list2);
        //THEN:
        assertThat(left, not(right));
    }
}