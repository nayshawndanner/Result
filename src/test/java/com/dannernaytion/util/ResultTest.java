package com.dannernaytion.util;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

public class ResultTest {

    @Test
    public void testMapFailure() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        Result<RuntimeException, Integer> actualResult = new Result<RuntimeException, String>(
                Optional.empty(),
                Optional.of(value))
                .mapFailure(Integer::new);

        actualResult.mapFailure(num -> {
            assertThat(num, equalTo(expected));
            return num;
        });
    }

    @Test
    public void testMapSuccess() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        Result<Integer, RuntimeException> actualResult = new Result<String, RuntimeException>(
                Optional.of(value), Optional.empty()).mapSuccess(Integer::new);

        actualResult.mapSuccess(num -> {
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
    public void testMapFailureWhenEmpty() {
        new Result<>(Optional.empty(), Optional.empty())
                .mapFailure(v -> {
                    fail("Should Not be Called");
                    return v;
                });
    }

    @Test
    public void testMapSuccessWhenEmpty() {
        new Result<>(Optional.empty(), Optional.empty())
                .mapSuccess(v -> {
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
    public void testFlatMapSuccess() {
        Result<Class<Void>, String> expectedFlatMappedResult = new Failure<>("success-value");
        Result<Integer, Class<Void>> initialResult = new Success<>(0);
        Result<Class<Void>, String> flatMappedResult =
                initialResult.flatMapSuccess(intValue -> new Failure<>("success-value"));

        assertThat(flatMappedResult, equalTo(expectedFlatMappedResult));
    }

    @Test
    public void testFlatMapFailure() {
        Result<Class<Void>, Integer> initialResult = new Failure<>(0);
        Result<String, Class<Void>> expectedFlatMappedResult = new Success<>("success-value");
        Result<String, Class<Void>> flatMappedResult =
                initialResult.flatMapFailure(intValue -> new Success<>("success-value"));

        assertThat(flatMappedResult, equalTo(expectedFlatMappedResult));
    }

    @Test
    public void testFlapMapSuccessEmpty(){
        final String initialFailureValue = "failure-value";
        Result<Class<Void>, String> initialResult = new Result<>(Optional.empty(), Optional.of(initialFailureValue));

        Result flatMappedResult = initialResult
                .flatMapSuccess(voidClass -> {
                    fail("FlatMapSuccess should not be called");
                    return null;
                });

        flatMappedResult.apply(
                successValue -> fail("Not expecting failure Compose call"),
                failureValue -> assertThat(failureValue, equalTo(initialFailureValue))
        );

        assertThat(flatMappedResult, equalTo(initialResult));

    }
    @Test
    public void testFlapMapFailureEmpty(){
        final String expectedValue = "success-value";
        Result<String, Class<Void>> initialResult = new Result<>(Optional.of(expectedValue), Optional.empty());

        Result flatMappedResult = initialResult
                .flatMapFailure(voidClass -> {
                    fail("Not expected to map failure on success");
                    return null;
                });

        flatMappedResult.apply(
                successValue -> assertThat(successValue,equalTo(expectedValue)),
                failureValue -> fail("Not expecting failure Compose call"));

        assertThat(flatMappedResult, equalTo(initialResult));

    }

    @Test
    public void test_SuccessResultConstruction() {
        String successfulResultValue = new String("success-value");
        Result<String, RuntimeException> actualResult = new Success<>(successfulResultValue);

        actualResult.apply(
                successValue -> assertThat(successValue, equalTo(successfulResultValue)),
                e -> fail("Unexpected Failure Consumer call")
        );
    }

    @Test
    public void test_FailureResultConstruction() {
        IllegalArgumentException failureResultValue = new IllegalArgumentException();
        Result<String, RuntimeException> actualResult = new Failure<>(failureResultValue);

        actualResult.apply(
                s -> fail("Unexpected Success Consumer call"),
                failureValue -> assertThat(failureValue, equalTo(failureResultValue))
        );
    }
}