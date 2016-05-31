package com.dannernaytion.util.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Either<L, R> {

    private final Optional<L> left;
    private final Optional<R> right;

    private Either(Optional<L> l, Optional<R> r) {
        if(!l.isPresent() && !r.isPresent())
            throw new IllegalArgumentException("both items cannot be null");

        this.left = l;
        this.right = r;
    }

    public <T> Either<T, R> mapLeft(Function<? super L, T> mapper) {
        return new Either<>(left.map(mapper), right);
    }

    public <T> Either<L, T> mapRight(Function<? super R, T> mapper) {
        return new Either<>(left, right.map(mapper));
    }

    public void apply(Consumer<L> leftConsumer, Consumer<? super R> rightConsumer) {
        left.ifPresent(leftConsumer);
        right.ifPresent(rightConsumer);
    }

    public <U,T> Either flatMapLeft(Function<? super L, Either<U,T>> mapper) {
        if(left.isPresent())
         return mapLeft(mapper).foldl(l->l);
        return new Either(left,right);
    }


    public <U,T> Either flatMapRight(Function<? super R, Either<U,T>> mapper) {
        if(right.isPresent())
            return mapRight(mapper).foldr(r->r);
        return new Either(left,right);
    }

    public  <T> T fold(Function<L, T> leftFunction, Function<R, T> rightFunction){
        if(left.isPresent())
            return foldl(leftFunction);
        return foldr(rightFunction);

    }

    private <T> T foldl(Function<L, T> leftFunction) {
        return leftFunction.apply(left.get());
    }

    private <T> T foldr(Function<R, T> rightFunction) {
        return rightFunction.apply(right.get());
    }

    @Override
    public String toString() {
        return "Either{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !Either.class.isInstance(o))
            return false;

        Either<?, ?> either = (Either<?, ?>) o;

        if (left != null ? !left.equals(either.left) : either.left != null) return false;
        return right != null ? right.equals(either.right) : either.right == null;

    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    public static class Left<L,R> extends Either<L,R>{
        public Left(L value) {
            super(Optional.of(value), Optional.empty());
        }
    }

    public static class Right<L,R> extends Either<L,R>{
        public Right(R value) {
            super(Optional.empty(), Optional.of(value));
        }
    }

}
