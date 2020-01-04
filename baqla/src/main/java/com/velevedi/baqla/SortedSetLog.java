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

import com.velevedi.baqla.util.IndexFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Log implementation which uses SortedSet as a data store to save entries.
 *
 * @param <V> type of a value of an entry
 */
public class SortedSetLog<I extends Comparable<? super I>, V> extends AbstractLog<I, V> {

    private final IndexFactory<I> indexFactory;
    private final SortedSet<Entry<I, V>> store;

    public SortedSetLog(IndexFactory<I> indexFactory, SortedSet<Entry<I, V>> store) {
        this(UUID.randomUUID(), indexFactory, store);
    }

    public SortedSetLog(UUID id, IndexFactory<I> indexFactory, SortedSet<Entry<I, V>> store) {
        super(id);
        if (indexFactory == null) {
            throw new IllegalArgumentException("Index factory can not be null");
        }
        if (store == null) {
            throw new IllegalArgumentException("Backing store should not be null");
        }
        this.indexFactory = indexFactory;
        this.store = store;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Log<I, V> fork() {
        try {
            SortedSet<Entry<I, V>> newStore = store.getClass().getDeclaredConstructor().newInstance();
            newStore.addAll(store);
            SortedSetLog<I, V> result = new SortedSetLog<>(indexFactory, newStore);
            result.parent = this.id;
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
    public Iterator<Entry<I, V>> iterator() {
        return new UnmodifiableIterator<>(store);
    }

    @Override
    public void add(String source, V value) {
        I nextIndex = indexFactory.nextIndex();
        store.add(new ComparableIdEntry<>(source, nextIndex, value));
    }

    @Override
    public Spliterator<Entry<I, V>> spliterator() {
        return store.spliterator();
    }

    @Override
    public void close() throws IOException {
        if (store instanceof Closeable) {
            ((Closeable) store).close();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortedSetLog<?, ?> that = (SortedSetLog<?, ?>) o;
        return id.equals(that.id) && store.equals(that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store);
    }
}
