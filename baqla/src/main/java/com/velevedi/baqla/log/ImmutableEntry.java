/*
 *
 *  * Copyright (C) 2017. The Baqla Authors
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.velevedi.baqla.log;

import java.util.Map;
import java.util.Objects;

/**
 * This object represents a result of a function calculation. Once calculated it will never change.
 */
public class ImmutableEntry<K, V> implements Log.Entry<K, V> {

    private final String source;
    private final Map<String, Object> meta;
    private final K key;
    private final V value;

    private ImmutableEntry(String source, Map<String, Object> meta, K key, V value) {
        this.source = source;
        this.meta = meta;
        this.key = key;
        this.value = value;
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public Map<String, Object> meta() {
        return meta;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableEntry<?, ?> that = (ImmutableEntry<?, ?>) o;
        return source.equals(that.source) &&
                meta.equals(that.meta) &&
                key.equals(that.key) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, meta, key, value);
    }



}
