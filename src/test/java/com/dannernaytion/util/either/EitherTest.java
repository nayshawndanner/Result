package com.dannernaytion.util.either;

import com.dannernaytion.util.either.Either.Right;
import com.dannernaytion.util.either.Either.Left;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EitherTest {

    @Test(expected = NullPointerException.class)
    public void LeftConstruction_throwsWhenGivenNull(){
        new Left<>(null);
    }

    @Test(expected = NullPointerException.class)
    public void RightConstruction_throwsWhenGivenNull(){
        new Right<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsWhenMapLeftFunctionReturnsNull(){
        new Left<>("left-value").mapLeft(l -> null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsWhenMapRightFunctionReturnsNull(){
        new Right<>("left-value").mapRight(l -> null);
    }

    @Test
    public void mapRightWhenRight_shouldReturnTheRightValue() throws Exception {
        final String stringValue = "25";
        final Integer expectedIntValue = 25;
        final Right<Object, Integer> expectedEither = new Right<>(expectedIntValue);

        Either<Object, Integer> mappedEither = new Right<>(stringValue)
                .mapRight(Integer::new);


        assertThat(expectedEither, equalTo(mappedEither));
    }

    @Test
    public void mapLeftWhenLeft_shouldReturnTheLeftValue() throws Exception {
        final String stringValue = "25";
        final Integer expectedIntValue = 25;
        final Left<Integer, Object> expectedEither = new Left<>(expectedIntValue);

        Either<Integer, Object> mappedEither = new Left<>(stringValue)
                .mapLeft(Integer::new);


        assertThat(expectedEither, equalTo(mappedEither));
    }

    @Test
    public void mapRightWhenLeft_shouldReturnTheInitialEither() {
        Either<String, Object> initalEither = new Left<>("Hello Sir");

        final Either<String, Object> mappedEither =
                initalEither.mapRight(v -> {fail("Should Not be Called");
                                            return v;
                                           });

        assertThat(initalEither, equalTo(mappedEither));
    }

    @Test
    public void mapLeftWhenRight_shouldReturnTheInitialEither() {
        Either<String, Object> initalEither = new Right<>("Hello Sir");

        final Either<String, Object> mappedEither = initalEither.mapLeft(v -> {
            fail("Should Not be Called");
            return v;
        });
        assertThat(initalEither, equalTo(mappedEither));
    }

    @Test
    public void applyWhenLeft_shouldCallTheLeftFuctions() {
        final String expectedValue = "Hello sir";
        final String[] valueHolder = new String[1];

        new Left<>(expectedValue)
                .apply(
                        l -> valueHolder[0]=l,
                        r -> fail("Unexpected Right Consumer call")
                );

        assertThat(expectedValue, equalTo(valueHolder[0]));
    }

    @Test
    public void applyWhenRight_shouldCallTheRightFunctions() {
        final String expectedValue = "Hello sir";
        final String[] valueHolder = new String[1];
        new Right<>(expectedValue)
                .apply(
                        l -> fail("Unexpected Left Consumer call"),
                        r -> valueHolder[0]=r
                );
        assertThat(expectedValue, equalTo(valueHolder[0]));
    }

    @Test
    public void flatMapLeftWhenLeft_shouldReturnTheEitherDeclaredInTheFunction() {
        Either<Integer, String> expectedFlatMappedEither = new Right<>("success-value");

        Either<Integer, String> initialEither = new Left<>(0);

        Either<Integer, String> flatMappedEither =
                initialEither.flatMapLeft(intValue -> new Right<>("success-value"));

        assertThat(flatMappedEither, equalTo(expectedFlatMappedEither));
    }

    @Test
    public void flatMapRightWhenRight_shouldReturnTheEitherDeclaredInTheFunction() {
        Either<Integer, Integer> initialEither = new Right<>(0);
        Either<String, Integer> expectedFlatMappedEither = new Left<>("success-value");

        Either<String, Integer> flatMappedEither =
                initialEither.flatMapRight(intValue -> new Left<>("success-value"));

        assertThat(flatMappedEither, equalTo(expectedFlatMappedEither));
    }

    @Test
    public void flapMapLeftWhenRight_shouldReturnTheInitialEither(){
        final String initialRightValue = "failure-value";
        Either<Integer, String> initialEither = new Right<>(initialRightValue);

        Either flatMappedEither = initialEither
                .flatMapLeft(voidClass -> {
                    fail("FlatMapLeft function should not be called");
                    return null;
                });


        assertThat(flatMappedEither, equalTo(initialEither));

    }

    @Test
    public void flapMapRightWhenLeft_shouldReturnTheInitialEither(){

        final String initialEitherValue = "success-value";
        Either<String, Integer> initialEither = new Left<>(initialEitherValue);

        Either flatMappedEither = initialEither
                .flatMapRight(intValue -> {
                    fail("Not expected to map failure on success");
                    return null;
                });

        flatMappedEither.apply(
                strValue -> assertThat(initialEitherValue, equalTo(strValue)),
                intValue -> fail("Not exptecting Right value"));

        assertThat(flatMappedEither, equalTo(initialEither));
    }

    @Test
    public void foldWhenRight_shouldReturnTheRightValue() {
        final String expectedValue = "right-value";
        final Either<Object, String> rightEither = new Right<>(expectedValue);

        final String foldValue = rightEither.fold(
                l -> {
                    fail("left-function call not expected");
                    return null;
                },
                r -> r);

        assertThat(expectedValue, equalTo(foldValue));
    }

    @Test
    public void foldWhenLeft_shouldReturnTheLeftValue() {
        final String expectedValue = "left-value";
        final Either<String, Object> leftEither = new Left<>(expectedValue);

        final String foldValue = leftEither.fold(
                l -> l,
                r -> {
                    fail("left-function call not expected");
                    return null;
                });

        assertThat(expectedValue, equalTo(foldValue));
    }

}