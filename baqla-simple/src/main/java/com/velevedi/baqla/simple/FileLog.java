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

import com.google.gson.Gson;
import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Filter;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.simple.filter.AllLatestValuesFilter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of the Log interface based on a file.
 * This is basic approach to demonstrate possibility to use Log interface on top of any storage.
 * For this particular example we use a file.
 */
public class FileLog implements Log {

    private final UUID id;
    private UUID parent = null;

    private final String folder;
    private final String fileNamePrefix;
    private final String store;

    private final AtomicLong idGenerator = new AtomicLong();

    private long writePointer = 0;
    private List<Long> pointers = new ArrayList<>();

    private final RandomAccessFile file;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);

    public FileLog(String folder, String fileNamePrefix) {
        this.id = UUID.randomUUID();

        this.folder = folder;
        this.fileNamePrefix = fileNamePrefix;
        this.store = fileNamePrefix + "_" + id;

        Path filePath = Paths.get(folder, store);
        try {
            this.file = new RandomAccessFile(filePath.toFile(), "rwd");
            this.file.seek(0);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to create file [" + filePath + "]", e);
        }
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
        rwl.writeLock().lock();
        try {
            byte[] contents = (toJson(entry) + '\n').getBytes();

            long currentPointer = writePointer + contents.length;
            pointers.add(writePointer);

            writeAt(writePointer, contents);

            writePointer = currentPointer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            rwl.writeLock().unlock();
        }
        return true;
    }

    private void writeAt(long position, byte[] contents) throws IOException {
        file.seek(position);
        file.write(contents);
    }

    private String readLineAt(long position) throws IOException {
        file.seek(position);
        return file.readLine();
    }

    @Override
    public List<Entry> find(Filter filter) {
        rwl.readLock().lock();
        try {
            List<Entry> result = new ArrayList<>();
            for (int i = pointers.size() - 1; i >= 0; i--) {
                String line = readLineAt(pointers.get(i));
                Entry entry = fromJson(line);
                if (filter.test(entry)) {
                    result.add(entry);
                }
                if (filter.complete()) {
                    break;
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public void copyTo(Log toLog, Filter filter) {
        rwl.readLock().lock();
        try {
            for (Entry entry : find(filter)) {
                if (filter.test(entry)) {
                    toLog.add(entry);
                }
            }
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public Log fork() {
        FileLog forked = new FileLog(this.folder, this.fileNamePrefix);

        forked.idGenerator.set(this.idGenerator.longValue());
        forked.parent = this.id;

        List<Entry> latestValues = find(new AllLatestValuesFilter());
        latestValues.forEach(forked::add);

        return forked;
    }

    @Override
    public int size() {
        return pointers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileLog that = (FileLog) o;

        return id.equals(that.id)
                && store.equals(that.store);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + store.hashCode();
        return result;
    }


    private static String toJson(Entry entry) {
        return new Gson().toJson(entry);
    }

    private static Entry fromJson(String string) {
        return new Gson().fromJson(string, LogEntry.class);
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
