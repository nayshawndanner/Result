package com.dannernaytion.util;

import java.util.Optional;

public class Failure<S,F> extends Result<S,F>{

    public Failure(F failure) {
        super(Optional.empty(), Optional.of(failure));
    }
}
