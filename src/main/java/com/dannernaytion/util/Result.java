package com.dannernaytion.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<S, F> {

    private final Optional<S> success;
    private final Optional<F> failure;

    protected Result(Optional<S> s, Optional<F> f) {
        this.success = s;
        this.failure = f;
    }

    public <T> Result<T, F> mapSuccess(Function<? super S, T> mapper) {
        return new Result<>(success.map(mapper), failure);
    }

    public <T> Result<S, T> mapFailure(Function<? super F, T> mapper) {
        return new Result<>(success, failure.map(mapper));
    }

    public void apply(Consumer<? super S> successConsumer, Consumer<? super F> failureConsumer) {
        success.ifPresent(successConsumer);
        failure.ifPresent(failureConsumer);
    }

    public <T, R> Result flatMapSuccess(Function<? super S, Result<T, R>> mapper) {
        final Result wrappedSuccessResult = mapSuccess(mapper);
        if(!success.isPresent())
            return wrappedSuccessResult;

        return unwrapSuccess(wrappedSuccessResult);
    }

    public <R, T> Result flatMapFailure(Function<? super F, Result<R, T>> mapper) {
        final Result<S, Result<R, T>> wrappedFailureResult = mapFailure(mapper);
        if(!failure.isPresent())
            return wrappedFailureResult;
        return unwrapFailure(wrappedFailureResult);
    }

    private <T, R> Result unwrapSuccess(Result<Result<T, R>, F> wrappedSuccessResult) {
        ValueExtractor<Result<T, R>> valueExtractor = new ValueExtractor<>();
        wrappedSuccessResult.apply(s -> valueExtractor.extract(s), f -> {
        });
        return valueExtractor.getValue();

    }

    private <R, T> Result<R, T> unwrapFailure(Result<S, Result<R, T>> wrappedFailureResult) {
        ValueExtractor<Result<R, T>> valueExtractor = new ValueExtractor<>();
        wrappedFailureResult.apply(s -> {
        }, f -> valueExtractor.extract(f));
        return valueExtractor.getValue();
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", failure=" + failure +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?, ?> result = (Result<?, ?>) o;

        if (success != null ? !success.equals(result.success) : result.success != null) return false;
        return failure != null ? failure.equals(result.failure) : result.failure == null;

    }

    @Override
    public int hashCode() {
        int result = success != null ? success.hashCode() : 0;
        result = 31 * result + (failure != null ? failure.hashCode() : 0);
        return result;
    }

}
