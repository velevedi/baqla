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

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * Implementation based on ArrayList
 */
public class ArrayListLog<K, V> implements Log<K, V> {

    private final UUID id = UUID.randomUUID();
    private UUID parent;
    private final List<Entry<K, V>> store;

    public ArrayListLog(List<Entry<K, V>> store) {
        this.store = store;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public UUID parent() {
        return parent;
    }

    @Override
    public void copyTo(Log<K, V> toLog, Predicate<Entry<K, V>> filter) {

    }

    @Override
    public Log<K, V> fork() {
        return null;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return store.contains(o);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return store.iterator();
    }

    @Override
    public Object[] toArray() {
        return store.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return store.toArray(a);
    }

    @Override
    public boolean add(Entry<K, V> entry) {
        return store.add(entry);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Entry<K, V>> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }
}
