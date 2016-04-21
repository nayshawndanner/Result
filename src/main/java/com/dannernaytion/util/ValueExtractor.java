package com.dannernaytion.util;

public class ValueExtractor<T> {
    private T value;

    public void extract(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
