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
import com.velevedi.baqla.util.IntegerIndexFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Log implementation which uses List as a data store to save entries.
 *
 * @param <V> type of a value of an entry
 */
public class ListLog<V> extends AbstractLog<Integer, V> {

    private final List<Entry<Integer, V>> store;
    private final IndexFactory<Integer> indexFactory = new IntegerIndexFactory();

    public ListLog(List<Entry<Integer, V>> store) {
        this(UUID.randomUUID(), store);
    }

    public ListLog(UUID id, List<Entry<Integer, V>> store) {
        super(id);
        if (store == null) {
            throw new IllegalArgumentException("Backing store should not be null");
        }
        this.store = store;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Log<Integer, V> fork() {
        try {
            List<Entry<Integer, V>> newStore = store.getClass().getDeclaredConstructor().newInstance();
            newStore.addAll(store);
            ListLog<V> result = new ListLog<>(newStore);
            result.parent = this.id;
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to fork log [" + id + "]", e);
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
    public Iterator<Entry<Integer, V>> iterator() {
        return new UnmodifiableIterator<>(store);
    }

    @Override
    public void add(String source, V value) {
        int nextIndex = indexFactory.nextIndex();
        if (nextIndex == Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Maximum capacity has been reached");
        }
        store.add(new ComparableIdEntry<>(source, nextIndex, value));
    }

    @Override
    public Spliterator<Entry<Integer, V>> spliterator() {
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
        ListLog<?> that = (ListLog<?>) o;
        return id.equals(that.id) && store.equals(that.store) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store);
    }
}
