package com.dannernaytion.util;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ResultTest {

    @Test
    public void testMapRight() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        Result<RuntimeException, Integer> actualResult = new Result<RuntimeException, String>(
                Optional.empty(),
                Optional.of(value))
                .mapRight(Integer::new);

        actualResult.mapLeft(num -> {
            assertThat(num, equalTo(expected));
            return num;
        });
    }

    @Test
    public void testMapLeft() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        Result<Integer, RuntimeException> actualResult = new Result<String, RuntimeException>(
                Optional.of(value),
                Optional.empty()).
                mapLeft(Integer::new);

        actualResult.mapLeft(num -> {
            assertThat(num, equalTo(expected));
            return num;
        });
    }

    @Test
    public void testApply() throws Exception {

        String expectedStr = new String("Hello");
        IllegalArgumentException expectedEx = new IllegalArgumentException();

        new Result<>(Optional.of("Hello"), Optional.of(expectedEx))
                .apply(
                        s -> assertThat(s, equalTo(expectedStr)),
                        f -> assertThat(f, equalTo(expectedEx))
                );

    }

    @Test
    public void testMapLeftWhenEmpty() {
        new Result<>(Optional.empty(), Optional.empty())
                .mapLeft(v -> {
                    fail("Should Not be Called");
                    return v;
                });
    }

    @Test
    public void testMapRightWhenEmpty() {
        new Result<>(Optional.empty(), Optional.empty())
                .mapRight(v -> {
                    fail("Should Not be Called");
                    return v;
                });
    }

    @Test
    public void testApplyWhenEmpty() {
        new Result<>(Optional.empty(), Optional.empty())
                .apply(
                        s -> fail("Unexpected Success Consumer call"),
                        f -> fail("Unexpected Failure Consumer call")
                );
    }

    @Test
    public void test_SuccessResultConstruction() {

        String successfulResultValue = new String("Frodo");
        Result<String, RuntimeException> actualResult = new Success<>(successfulResultValue);

        actualResult.apply(
                str -> assertThat(str, equalTo(successfulResultValue)),
                e   -> fail("Unexpected Failure Consumer call")
        );
    }

    @Test
    public void test_FailureResultConstruction() {
        IllegalArgumentException failureResultValue = new IllegalArgumentException();
        Result<String, RuntimeException> actualResult = new Failure<>(failureResultValue);

        actualResult.apply(
                str -> fail("Unexpected Success Consumer call"),
                e   -> assertThat(e, equalTo(failureResultValue))
        );
    }
}