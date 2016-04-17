package com.dannernaytion.util;

import java.util.Optional;

public class Success<S,F> extends Result<S,F>{

    public Success(S success) {
        super(Optional.of(success), Optional.empty());
    }
}
