package com.dannernaytion.util.result;

import com.dannernaytion.util.either.Either;

public class Success<S,F> extends Result<S,F>{

    public Success(S success) {
        super(new Either.Left<>(success));
    }
}
