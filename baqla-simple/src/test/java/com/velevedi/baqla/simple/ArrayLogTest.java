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
import com.velevedi.baqla.Log;
import com.velevedi.baqla.simple.filter.LatestValuesForTasksFilter;
import com.velevedi.baqla.simple.filter.TaskIdFilter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 */
class ArrayLogTest {

    @Test
    void testCreation()  {
        Log log = new ArrayLog(10);

        assertNotNull(log.id());
        assertEquals(log.size(), 0);
    }

    @Test
    void testIdGeneration() {
        Log log = new ArrayLog(10);

        assertEquals(log.size(), 0);

        long nextEntryId = log.nextEntryId();

        assertEquals(nextEntryId, 0L);
        assertEquals(log.size(), 0);

        nextEntryId = log.nextEntryId();

        assertEquals(nextEntryId, 1L);
        assertEquals(log.size(), 0);

        nextEntryId = log.nextEntryId();

        assertEquals(nextEntryId, 2L);
        assertEquals(log.size(), 0);

        nextEntryId = log.nextEntryId();

        assertEquals(nextEntryId, 3L);
        assertEquals(log.size(), 0);
    }

    @Test
    void testAddingEntries() {
        Log log = new ArrayLog(5);

        long entryId = log.nextEntryId();

        assertEquals(entryId, 0L);
        assertEquals(log.size(), 0);

        LogEntry entry1 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry1);
        assertEquals(log.size(), 1);

        entryId = log.nextEntryId();
        LogEntry entry2 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry2);
        assertEquals(entryId, 1L);
        assertEquals(log.size(), 2);

        entryId = log.nextEntryId();
        LogEntry entry3 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry3);
        assertEquals(entryId, 2L);
        assertEquals(log.size(), 3);

        entryId = log.nextEntryId();
        LogEntry entry4 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry4);
        assertEquals(entryId, 3L);
        assertEquals(log.size(), 4);

        entryId = log.nextEntryId();
        LogEntry entry5 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry5);
        assertEquals(entryId, 4L);
        assertEquals(log.size(), 5);
    }

    @Test
    void testMaxLimitReached() {
        int size = 5;
        Log log = new ArrayLog(size);

        for (int i = 0; i < size; i++) {
            long entryId = log.nextEntryId();
            LogEntry entry = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
            log.add(entry);
        }

        long entryId = log.nextEntryId();
        LogEntry entry = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> log.add(entry)
        );
    }

    @Test
    void testEqualsHashCode() {
        LogEntry entry = LogEntry.newBuilder().id(1L).taskId("my task").payload(1L).build();

        Log log1 = new ArrayLog(5);
        log1.add(entry);

        Log log2 = new ArrayLog(5);
        log2.add(entry);

        assertNotEquals(log1, log2);
        assertNotEquals(log1.id(), log2.id());
        assertNotEquals(log1.hashCode(), log2.hashCode());

        assertEquals(log1, log1);
    }

    @Test
    void testFiltering() {
        Log log = new ArrayLog(10);

        for (int i = 0; i < 10; i++) {
            long entryId = log.nextEntryId();
            log.add(
                    LogEntry.newBuilder().id(entryId).taskId("task" + entryId).payload(entryId).build()
            );
        }

        Set<String> tasks = new HashSet<>();
        tasks.add("task0");
        tasks.add("task5");
        tasks.add("task9");

        List<Entry> entries = log.find(new TaskIdFilter(tasks));

        assertEquals(entries.size(), 3);
    }

    @Test
    void testCopyTo() {
        Log log = new ArrayLog(10);

        for (int i = 0; i < 10; i++) {
            long entryId = log.nextEntryId();
            log.add(
                    LogEntry.newBuilder().id(entryId).taskId("task" + entryId).payload(entryId).build()
            );
        }

        Set<String> tasks = new HashSet<>();
        tasks.add("task5");

        Log newLog = new ArrayLog(7);
        assertEquals(newLog.size(), 0);

        log.copyTo(newLog, new TaskIdFilter(tasks));

        assertEquals(newLog.size(), 1);
    }

    @Test
    void testForkAllTasksDifferent() {
        Log source = new ArrayLog(100);

        for (int i = 0; i < 10; i++) {
            long entryId = source.nextEntryId();
            source.add(
                    LogEntry.newBuilder().id(entryId).taskId("task" + entryId).payload(entryId).build()
            );
        }

        assertEquals(source.size(), 10);

        Log fork = source.fork();

        assertEquals(source.id(), fork.parent());

        assertEquals(source.size(), fork.size());
    }

    @Test
    void testForkOneTask() {
        Log source = new ArrayLog(100);

        for (int i = 0; i < 10; i++) {
            source.add(
                    LogEntry.newBuilder().id(source.nextEntryId()).taskId("task").payload(i).build()
            );
        }

        assertEquals(source.size(), 10);

        Log fork = source.fork();

        assertEquals(source.id(), fork.parent());

        assertEquals(fork.size(), 1);

        List<Entry> entries = fork.find(new LatestValuesForTasksFilter("task"));
        assertEquals(entries.iterator().next().payload(), 9);
    }

}