package com.dannernaytion.util.result;

import com.dannernaytion.util.either.Either;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

public class ResultTest {

    @Test
    public void testMapFailure() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        final Result<Integer, String> initialResult = new Result<>(new Either.Right<>(value));
        Result<Integer, Integer> actualResult = initialResult.mapFailure(Integer::new);

        actualResult.mapFailure(num -> {
            assertThat(num, equalTo(expected));
            return num;
        });
    }

    @Test
    public void testMapSuccess() throws Exception {
        final String value = "25";
        final Integer expected = 25;

        final Result<String, Integer> initialResult = new Result<>(new Either.Left<>(value));

        Result<Integer, Integer> actualResult = initialResult.mapSuccess(Integer::new);

        actualResult.mapSuccess(num -> {
            assertThat(num, equalTo(expected));
            return num;
        });
    }

    @Test
    public void testApply() throws Exception {
        String expectedStr = new String("Hello");
        new Result<>(new Either.Left<>("Hello"))
                .apply(
                        s -> assertThat(s, equalTo(expectedStr)),
                        f -> {
                        }
                );
    }

    @Test
    public void testMapFailureWhenEmpty() {
        new Result<>(new Either.Left<>(""))
                .mapFailure(v -> {
                    fail("Should Not be Called");
                    return v;
                });
    }

    @Test
    public void testMapSuccessWhenEmpty() {
        new Result<>(new Either.Right<>(""))
                .mapSuccess(v -> {
                    fail("Should Not be Called");
                    return v;
                });
    }
    
    @Test
    public void testFlatMapSuccess() {
        Result<Integer, String> expectedFlatMappedResult = new Failure<>("success-value");
        Result<Integer, Integer> initialResult = new Success<>(0);
        Result<Integer, String> flatMappedResult =
                initialResult.flatMapSuccess(intValue -> new Failure<>("success-value"));

        assertThat(flatMappedResult, equalTo(expectedFlatMappedResult));
    }

    @Test
    public void testFlatMapFailure() {
        Result<Integer, Integer> initialResult = new Failure<>(0);
        Result<String, Integer> expectedFlatMappedResult = new Success<>("success-value");
        Result<String, Integer> flatMappedResult =
                initialResult.flatMapFailure(intValue -> new Success<>("success-value"));

        assertThat(flatMappedResult, equalTo(expectedFlatMappedResult));
    }

    @Test
    public void testFlapMapSuccessEmpty() {
        final String initialFailureValue = "failure-value";
        Result<Integer, String> initialResult = new Result<>(new Either.Right<>(initialFailureValue));

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
    public void testFlapMapFailureEmpty() {
        final String expectedValue = "success-value";
        Result<String, Integer> initialResult = new Result<>(new Either.Left<>(expectedValue));

        Result flatMappedResult = initialResult
                .flatMapFailure(voidClass -> {
                    fail("Not expected to map failure on success");
                    return null;
                });

        flatMappedResult.apply(
                successValue -> assertThat(successValue, equalTo(expectedValue)),
                failureValue -> fail("Not expecting failure Compose call"));

        assertThat(flatMappedResult, equalTo(initialResult));

    }

    @Test
    public void test_SuccessResultConstruction() {
        String successfulResultValue = new String("success-value");
        Result<String, Integer> actualResult = new Success<>(successfulResultValue);

        actualResult.apply(
                successValue -> assertThat(successValue, equalTo(successfulResultValue)),
                e -> fail("Unexpected Failure Consumer call")
        );
    }

    @Test
    public void test_FailureResultConstruction() {
        Integer failureResultValue = new Integer(5);
        Result<String, Integer> actualResult = new Failure<>(failureResultValue);

        actualResult.apply(
                s -> fail("Unexpected Success Consumer call"),
                failureValue -> assertThat(failureValue, equalTo(failureResultValue))
        );
    }
}