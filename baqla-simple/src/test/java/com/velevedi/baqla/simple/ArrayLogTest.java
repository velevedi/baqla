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
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 */
public class ArrayLogTest {

    @Test
    public void testCreation() throws Exception {
        Log log = new ArrayLog(10);

        assertThat(log.id(), is(notNullValue()));
        assertThat(log.size(), is(0));
    }

    @Test
    public void testIdGeneration() throws Exception {
        Log log = new ArrayLog(10);

        assertThat(log.size(), is(0));

        long nextEntryId = log.nextEntryId();

        assertThat(nextEntryId, is(0L));
        assertThat(log.size(), is(0));

        nextEntryId = log.nextEntryId();

        assertThat(nextEntryId, is(1L));
        assertThat(log.size(), is(0));

        nextEntryId = log.nextEntryId();

        assertThat(nextEntryId, is(2L));
        assertThat(log.size(), is(0));

        nextEntryId = log.nextEntryId();

        assertThat(nextEntryId, is(3L));
        assertThat(log.size(), is(0));
    }

    @Test
    public void testAddingEntries() throws Exception {
        Log log = new ArrayLog(5);

        long entryId = log.nextEntryId();

        assertThat(entryId, is(0L));
        assertThat(log.size(), is(0));

        LogEntry entry1 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry1);
        assertThat(log.size(), is(1));

        entryId = log.nextEntryId();
        LogEntry entry2 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry2);
        assertThat(entryId, is(1L));
        assertThat(log.size(), is(2));

        entryId = log.nextEntryId();
        LogEntry entry3 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry3);
        assertThat(entryId, is(2L));
        assertThat(log.size(), is(3));

        entryId = log.nextEntryId();
        LogEntry entry4 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry4);
        assertThat(entryId, is(3L));
        assertThat(log.size(), is(4));

        entryId = log.nextEntryId();
        LogEntry entry5 = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry5);
        assertThat(entryId, is(4L));
        assertThat(log.size(), is(5));
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testMaxLimitReached() throws Exception {
        int size = 5;
        Log log = new ArrayLog(size);

        for (int i = 0; i < size; i++) {
            long entryId = log.nextEntryId();
            LogEntry entry = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
            log.add(entry);
        }

        long entryId = log.nextEntryId();
        LogEntry entry = LogEntry.newBuilder().id(entryId).taskId("my task").payload(entryId).build();
        log.add(entry); // exception
    }

    @Test
    public void testEqualsHashCode() throws Exception {
        LogEntry entry = LogEntry.newBuilder().id(1L).taskId("my task").payload(1L).build();

        Log log1 = new ArrayLog(5);
        log1.add(entry);

        Log log2 = new ArrayLog(5);
        log2.add(entry);

        assertFalse(log1.equals(log2));
        assertFalse(log1.id().equals(log2.id()));
        assertFalse(log1.hashCode() == log2.hashCode());

        assertTrue(log1.equals(log1));
    }

    @Test
    public void testFiltering() throws Exception {
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

        assertThat(entries.size(), is(3));
    }

    @Test
    public void testCopyTo() throws Exception {
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
        assertThat(newLog.size(), is(0));

        log.copyTo(newLog, new TaskIdFilter(tasks));

        assertThat(newLog.size(), is(1));
    }

    @Test
    public void testForkAllTasksDifferent() throws Exception {
        Log source = new ArrayLog(100);

        for (int i = 0; i < 10; i++) {
            long entryId = source.nextEntryId();
            source.add(
                    LogEntry.newBuilder().id(entryId).taskId("task" + entryId).payload(entryId).build()
            );
        }

        assertThat(source.size(), is(10));

        Log fork = source.fork();

        assertThat(source.id(), is(fork.parent()));

        assertThat(source.size(), is(fork.size()));
    }

    @Test
    public void testForkOneTask() throws Exception {
        Log source = new ArrayLog(100);

        for (int i = 0; i < 10; i++) {
            source.add(
                    LogEntry.newBuilder().id(source.nextEntryId()).taskId("task").payload(i).build()
            );
        }

        assertThat(source.size(), is(10));

        Log fork = source.fork();

        assertThat(source.id(), is(fork.parent()));

        assertThat(fork.size(), is(1));

        List<Entry> entries = fork.find(new LatestValuesForTasksFilter("task"));
        assertThat(entries.iterator().next().payload(), is(9));
    }

}