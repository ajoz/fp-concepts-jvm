package io.github.ajoz.fold;

import org.junit.Test;

import java.util.List;

import static io.github.ajoz.fold.Fold2ImperativeLibrary.product;
import static io.github.ajoz.fold.Fold2ImperativeLibrary.sum;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Fold2ImperativeLibraryTest {
    @Test
    public void shouldReturnInitialValueForEmptyListSum() {
        //GIVEN:
        final List<Integer> emptyList = emptyList();
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sum(emptyList, expected);
        //THEN:
        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnInitialValueForEmptyListProduct() {
        //GIVEN:
        final List<Integer> emptyList = emptyList();
        final Integer expected = 1;
        //WHEN:
        final Integer actual = product(emptyList, expected);
        //THEN:
        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnNegativeValueSum() {
        //GIVEN:
        final List<Integer> negativeList = asList(-1, -1, -1, -1);
        final Integer expected = -4;
        //WHEN:
        final Integer actual = sum(negativeList, 0);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveValueSum() {
        //GIVEN:
        final List<Integer> positiveList = asList(1, 1, 1, 1);
        final Integer expected = 4;
        //WHEN:
        final Integer actual = sum(positiveList, 0);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnZeroForMixedValuesSum() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, -2, 1, 2);
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sum(mixedList, 0);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveValueForIncrementalValuesSum() {
        //GIVEN:
        final List<Integer> incrementalList = asList(1, 2, 3);
        final Integer expected = 6;
        //WHEN:
        final Integer actual = sum(incrementalList, 0);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveForPositiveListProduct() {
        //GIVEN:
        final List<Integer> positiveList = asList(1, 2, 3);
        final Integer expected = 6;
        //WHEN:
        final Integer actual = product(positiveList, 1);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnPositiveForEvenNumberOfNegativeValuesProduct() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, -2, 1, 2);
        final Integer expected = 4;
        //WHEN:
        final Integer actual = product(mixedList, 1);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnNegativeForOddNumberOfNegativeValuesProduct() {
        //GIVEN:
        final List<Integer> mixedList = asList(-1, 2, 1, 2);
        final Integer expected = -4;
        //WHEN:
        final Integer actual = product(mixedList, 1);
        //THEN:
        assertThat(actual, is(expected));
    }
}