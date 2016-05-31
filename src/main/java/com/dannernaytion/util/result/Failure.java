package com.dannernaytion.util.result;

import com.dannernaytion.util.either.Either;

public class Failure<S, F> extends Result<S, F>{

    public Failure(F failure) {
        super(new Either.Right<>(failure));
    }
}
