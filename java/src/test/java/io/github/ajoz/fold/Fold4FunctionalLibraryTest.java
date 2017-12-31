package io.github.ajoz.fold;

import static io.github.ajoz.fold.Fold4FunctionalLibrary.foldLeft;
import static io.github.ajoz.fold.Fold4FunctionalLibrary.product;
import static io.github.ajoz.fold.Fold4FunctionalLibrary.sum;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.List;
import java.util.function.BiFunction;


public class Fold4FunctionalLibraryTest {
    @Test
    public void shouldReturnZeroForEmptyListSum() {
        //GIVEN:
        final List<Integer> emptyList = emptyList();
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sum(emptyList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnNegativeValueSum() {
        //GIVEN:
        final List<Integer> negativeList = asList(-1, -1, -1, -1);
        final Integer expected = -4;
        //WHEN:
        final Integer actual = sum(negativeList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveValueSum() {
        //GIVEN:
        final List<Integer> positiveList = asList(1, 1, 1, 1);
        final Integer expected = 4;
        //WHEN:
        final Integer actual = sum(positiveList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnZeroForMixedValuesSum() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, -2, 1, 2);
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sum(mixedList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveValueForIncrementalValuesSum() {
        //GIVEN:
        final List<Integer> incrementalList = asList(1, 2, 3);
        final Integer expected = 6;
        //WHEN:
        final Integer actual = sum(incrementalList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnOneForEmptyListProduct() throws Exception {
        //GIVEN:
        final List<Integer> emptyList = emptyList();
        final Integer expected = 1;
        //WHEN:
        final Integer actual = product(emptyList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveForPositiveListProduct() {
        //GIVEN:
        final List<Integer> positiveList = asList(1, 2, 3);
        final Integer expected = 6;
        //WHEN:
        final Integer actual = product(positiveList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveForEvenNumberOfNegativeValuesProduct() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, -2, 1, 2);
        final Integer expected = 4;
        //WHEN:
        final Integer actual = product(mixedList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnNegativeForOddNumberOfNegativeValuesProduct() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, 2, 1, 2);
        final Integer expected = -4;
        //WHEN:
        final Integer actual = product(mixedList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldCountListElements() {
        //GIVEN:
        final List<String> stringList = asList("There", "are", "7", "elements", "on", "this", "list");
        final Integer expected = 7;
        //WHEN:
        final Integer actual = foldLeft(stringList, 0, (x, y) -> x + 1);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldJoinAndUpperCaseListElements() {
        /**
         * This test is a good example how powerful our simple implementation of foldLeft
         * has become. We can now use the foldLeft to "reduce" data to desired form. Many
         * functions like sumBad or product are just specific usages of foldLeft.
         */
        //GIVEN:
        final List<String> stringList = asList("elements", "should", "be", "joined");
        final String expected = "ELEMENTSSHOULDBEJOINED";
        //WHEN:
        final String actual = foldLeft(stringList, "", (x, y) -> (x + y).toUpperCase());
        //THEN:
        assertThat(actual, is(expected));
    }
}