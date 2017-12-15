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

package com.velevedi.baqla.simple;

import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Filter;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.simple.filter.AllLatestValuesFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Array based Log implementation with predefined size.
 */
public class ArrayLog implements Log {

    private final UUID id;
    private UUID parent = null;
    private final Entry[] store;
    private final AtomicLong idGenerator = new AtomicLong();
    private int pointer = 0;

    public ArrayLog(int size) {
        this.id = UUID.randomUUID();
        this.store = new Entry[size];
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
    public long nextEntryId() {
        return idGenerator.getAndIncrement();
    }

    @Override
    public boolean add(Entry entry) {
        store[pointer++] = entry;
        return true;
    }

    @Override
    public List<Entry> find(Filter filter) {
        List<Entry> result = new ArrayList<>();
        for (int i = store.length-1; i >= 0; i--) {
            Entry entry = store[i];
            if (filter.test(entry)) {
                result.add(entry);
            }
            if (filter.complete()) {
                break;
            }
        }
        return result;
    }

    @Override
    public void copyTo(Log toLog, Filter filter) {
        for (Entry entry : store) {
            if (filter.test(entry)) {
                toLog.add(entry);
            }
        }
    }

    @Override
    public Log fork() {
        ArrayLog forked = new ArrayLog(this.store.length);

        forked.idGenerator.set(this.idGenerator.longValue());
        forked.parent = this.id;

        List<Entry> latestValues = this.find(new AllLatestValuesFilter());
        latestValues.forEach(forked::add);

        return forked;
    }

    @Override
    public int size() {
        return pointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayLog that = (ArrayLog) o;

        return pointer == that.pointer
                && id.equals(that.id)
                && Arrays.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + Arrays.hashCode(store);
        result = 31 * result + pointer;
        return result;
    }

    @Override
    public void close() throws IOException {
    }
}
