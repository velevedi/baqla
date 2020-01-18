package com.velevedi.baqla;

import java.util.Objects;

/**
 * Default implementation of the Entry class
 *
 * @param <I> type of the index of an entry
 * @param <V> type of a value of an entry
 */
class ComparableIdEntry<I extends Comparable<? super I>, V> implements Log.Entry<I, V> {
    private final String source;
    private final I index;
    private final V value;

    public ComparableIdEntry(String source, I index, V value) {
        Objects.requireNonNull(source, "null values are not allowed");
        Objects.requireNonNull(index, "null values are not allowed");
        Objects.requireNonNull(value, "null values are not allowed");
        this.source = source;
        this.index = index;
        this.value = value;
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public I index() {
        return index;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableIdEntry<?, ?> that = (ComparableIdEntry<?, ?>) o;
        return source.equals(that.source) &&
                index.equals(that.index) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, index, value);
    }
}
