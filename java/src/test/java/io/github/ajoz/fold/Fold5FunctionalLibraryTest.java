package io.github.ajoz.fold;

import org.junit.Test;

import java.util.List;
import java.util.function.BiFunction;

import static io.github.ajoz.fold.Fold5FunctionalLibrary.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Fold5FunctionalLibraryTest {
    @Test
    public void shouldFlattenAList() {
        //GIVEN:
        final List<List<Integer>> listOfLists = asList(asList(1, 2, 3), asList(4, 5, 6));
        final List<Integer> expected = asList(1, 2, 3, 4, 5, 6);
        //WHEN:
        final List<Integer> actual = flatten(listOfLists);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldFlattenAnEmptyList() {
        //GIVEN:
        final List<List<Integer>> listOfLists = emptyList();
        final List<Integer> expected = emptyList();
        //WHEN:
        final List<Integer> actual = flatten(listOfLists);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldNotContainAValueOnAnEmptyList() {
        //GIVEN:
        final List<Integer> emptyList = emptyList();
        final Boolean expected = false;
        //WHEN:
        final Boolean actual = contains(emptyList, 1);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldNotContainAValue() {
        //GIVEN:
        final List<Integer> list = asList(2, 3, 4);
        final Boolean expected = false;
        //WHEN:
        final Boolean actual = contains(list, 1);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldContainAValue() {
        //GIVEN:
        final List<String> stringList = asList("test", "of", "a", "function");
        final Boolean expected = true;
        //WHEN:
        final Boolean actual = contains(stringList, "test");
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldCalculateAverage() {
        //GIVEN:
        final List<Integer> list = asList(1, 2, 3);
        final Integer expected = 2;
        //WHEN:
        final Integer actual = average(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldFindLastElement() {
        //GIVEN:
        final List<Integer> list = asList(0, 1, 2, 3, 4, 5, 6);
        final Integer expected = 6;
        //WHEN:
        final Integer actual = last(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldJoinAList() {
        //GIVEN:
        final List<Boolean> list = asList(true, false, false, true);
        final String expected = "true:false:false:true";
        //WHEN:
        final String actual = join(list, ":");
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldJoinAnEmptyList() {
        final List<Boolean> list = emptyList();
        final String expected = "";
        //WHEN:
        final String actual = join(list, "::");
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldJoinOneElementList() {
        //GIVEN:
        final List<Integer> list = singletonList(1);
        final String expected = "1";
        //WHEN:
        final String actual = join(list, "_");
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldChangeAListToString() {
        //GIVEN:
        final List<Integer> list = asList(1, 2, 3);
        final String expected = "List(1, 2, 3)";
        //WHEN:
        final String actual = mkString(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldChangeAnEmptyListToString() {
        //GIVEN:
        final List<String> emptyList = emptyList();
        final String expected = "List()";
        //WHEN:
        final String actual = mkString(emptyList);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldCountListElements() {
        //GIVEN:
        final List<Integer> list = asList(1, 2, 5, 6);
        final Integer expected = 4;
        //WHEN:
        final Integer actual = count(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldCountEmptyList() {
        //GIVEN:
        final List<String> list = emptyList();
        final Integer expected = 0;
        //WHEN:
        final Integer actual = count(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReverseAnEmptyList() {
        //GIVEN:
        final List<String> list = emptyList();
        //WHEN:
        final List<String> actual = reverse(list);
        //THEN:
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldReverseAOneElementList() {
        //GIVEN:
        final List<String> list = singletonList("Test");
        final List<String> expected = singletonList("Test");
        //WHEN:
        final List<String> actual = reverse(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReverseAList() {
        //GIVEN:
        final List<Integer> list = asList(1, 2, 3, 4);
        final List<Integer> expected = asList(4, 3, 2, 1);
        //WHEN:
        final List<Integer> actual = reverse(list);
        //THEN:
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldFoldStringsFromRight() {
        //GIVEN:
        final List<String> list = asList("this", "is", "a", "test");
        final String initial = "INITIAL";
        final String expected = "this_is_a_test_INITIAL";
        //WHEN:
        final String actual = foldRight(list, initial, (a, b) -> a + "_" + b);
        //THEN
        assertThat(actual, is(expected));
    }
}