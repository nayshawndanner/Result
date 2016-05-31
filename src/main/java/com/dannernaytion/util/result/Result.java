package com.dannernaytion.util.result;

import com.dannernaytion.util.either.Either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<S, F> {

    private Either<S, F> either;

    Result(Either<S, F> either) {
        this.either = either;
    }

    public <T> Result<T, F> mapSuccess(Function<S, T> mapper) {
        return new Result<>(either.mapLeft(mapper));
    }

    public <T> Result<S, T> mapFailure(Function<F, T> mapper) {
        return new Result<>(either.mapRight(mapper));
    }

    public void apply(Consumer<S> successConsumer, Consumer<F> failureConsumer) {
        either.apply(successConsumer,failureConsumer);
    }

    public <T, R> Result flatMapSuccess(Function<? super S, Result<T, R>> mapper) {
        return new Result(this.either.flatMapLeft(s -> mapper.apply(s).either));
    }

    public <R, T> Result flatMapFailure(Function<? super F, Result<R, T>> mapper) {
        return new Result(this.either.flatMapRight(f -> mapper.apply(f).either));
    }

    public  <T> T fold(Function<S, T> successFunction, Function<F, T> failureFunction){
        return this.either.fold(successFunction,failureFunction);
    }

    @Override
    public String toString() {
        return either.toString()
                .replace("Either","Result")
                .replace("left","success")
                .replace("right","failure");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !Result.class.isInstance(o)) return false;

        Result<?, ?> result = (Result<?, ?>) o;

        return either != null ? either.equals(result.either) : result.either == null;

    }

    @Override
    public int hashCode() {
        return either != null ? either.hashCode() : 0;
    }
}
