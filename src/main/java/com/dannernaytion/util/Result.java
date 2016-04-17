package com.dannernaytion.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<L,R>{

    private final Optional<L> failure;
    private final Optional<R> success;

    protected Result(Optional<L> f, Optional<R> s) {
        this.failure = f;
        this.success = s;
    }

    public <T> Result<L,T> mapSuccess(Function<? super R, T> mapper){

        return new Result<>(failure, success.map(mapper));
    }

    public <T> Result<T,R> mapFailure(Function<? super L, T> mapper){

        return new Result<>(failure.map(mapper), success);
    }

    public void apply(Consumer<? super L> sConsumer, Consumer<? super R> fConsumer){
        failure.ifPresent(sConsumer);
        success.ifPresent(fConsumer);
    }
}
