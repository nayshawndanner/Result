package com.dannernaytion.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Result<L,R>{

    private final Optional<L> left;
    private final Optional<R> right;

    protected Result(Optional<L> l, Optional<R> r) {
        this.left = l;
        this.right = r;
    }

    public <T> Result<L,T> mapRight(Function<? super R, T> mapper){

        return new Result<>(left, right.map(mapper));
    }

    public <T> Result<T,R> mapLeft(Function<? super L, T> mapper){

        return new Result<>(left.map(mapper), right);
    }

    public void apply(Consumer<? super L> sConsumer, Consumer<? super R> fConsumer){
        left.ifPresent(sConsumer);
        right.ifPresent(fConsumer);
    }
}
