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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the Log interface based on HashMap store.
 * This is an example of alternative Log implementation where only latest version of data is present.
 */
public class HashMapLog implements Log {

    private final UUID id;
    private UUID parent = null;
    private final Map<String, Entry> store;
    private final AtomicLong idGenerator = new AtomicLong();

    public HashMapLog() {
        this.id = UUID.randomUUID();
        this.store = new HashMap<>();
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
        store.put(entry.taskId(), entry);
        return true;
    }

    @Override
    public List<Entry> find(Filter filter) {
        List<Entry> result = new ArrayList<>();
        for (Map.Entry<String, Entry> entry : store.entrySet()) {
            if (filter.test(entry.getValue())) {
                result.add(entry.getValue());
            }
            if (filter.complete()) {
                break;
            }
        }
        return result;
    }

    @Override
    public void copyTo(Log toLog, Filter filter) {
        for (Map.Entry<String, Entry> entry : store.entrySet()) {
            if (filter.test(entry.getValue())) {
                toLog.add(entry.getValue());
            }
        }
    }

    @Override
    public Log fork() {
        HashMapLog forked = new HashMapLog();

        forked.idGenerator.set(this.idGenerator.longValue());
        forked.parent = this.id;
        // no need to run filter as we store latest versions only anyways
        for (Map.Entry<String, Entry> entry : store.entrySet()) {
            forked.add(entry.getValue());
        }

        return forked;
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashMapLog that = (HashMapLog) o;

        return id.equals(that.id)
                && store.equals(that.store);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + store.hashCode();
        return result;
    }

    @Override
    public void close() throws IOException {
    }
}
