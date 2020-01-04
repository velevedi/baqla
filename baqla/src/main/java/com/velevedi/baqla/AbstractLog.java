/*
 * Copyright (C) 2017. The Baqla Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. The License can be obtained at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.velevedi.baqla;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract Log which has basic methods implemented.
 *
 * @param <I> type of the index of an entry
 * @param <V> type of a value of an entry
 */
public abstract class AbstractLog<I extends Comparable<? super I>, V> implements Log<I, V> {

    protected UUID id;
    protected UUID parent;

    public AbstractLog(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        this.id = id;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public UUID parent() {
        return parent;
    }


    /**
     * Default implementation of the Entry class
     *
     * @param <I> type of the index of an entry
     * @param <V> type of a value of an entry
     */
    static class ComparableIdEntry<I extends Comparable<? super I>, V> implements Entry<I, V> {
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

}
