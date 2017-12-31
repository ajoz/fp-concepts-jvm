package io.github.ajoz.fold;

import org.junit.Test;

import java.util.List;

import static io.github.ajoz.fold.Fold1ImperativeLibrary.product;
import static io.github.ajoz.fold.Fold1ImperativeLibrary.sum;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Fold1ImperativeLibraryTest {

    @Test
    public void shouldReturnPositiveValueForSinglePositiveItemListSum() {
        //GIVEN:
        final List<Integer> list = asList(128);
        final Integer expected = 128;
        //WHEN:
        final Integer actual = sum(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnNegativeValueForSingleNegativeItemListSum() {
        //GIVEN:
        final List<Integer> list = asList(-256);
        final Integer expected = -256;
        //WHEN:
        final Integer actual = sum(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnZeroForEmptyListSum() throws Exception {
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
    public void shouldReturnZeroForListWithAllZerosSum() {
        //GIVEN:
        final List<Integer> list = asList(0, 0, 0, 0, 0, 0);
        final Integer expected = 0;
        //WHEN:
        final Integer actual = sum(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnOneForEmptyListProduct() {
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
}