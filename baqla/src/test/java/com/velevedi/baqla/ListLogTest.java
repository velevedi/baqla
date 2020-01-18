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

import com.velevedi.baqla.predicate.PassAll;
import com.velevedi.baqla.predicate.RefuseAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ListLogTest {

    @Test
    void creation() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        assertNotNull(log);

        assertThat(log.isEmpty(), is(true));
        assertThat(log.size(), is(0));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ListLog<>(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ListLog<>(null, new ArrayList<>()));
    }

    @Test
    void addingEntries() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source", "value1");
        log.add(new ComparableIdEntry<>("source", log.size() + 1, "value2"));
        log.addAll(Collections.singleton(new ComparableIdEntry<>("source", log.size() + 1, "value3")));

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(3));
        assertThat(log.iterator().hasNext(), is(true));
        // entries are ordered by the insertion order because of the List backing store
        assertThat(log.iterator().next().value(), is("value1"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> log.add(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> log.addAll(null));
    }

    @Test
    void tryingToRemove() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source", "value1");
        log.add("source", "value2");
        log.add("source", "value3");

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(3));

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> log.iterator().remove());
    }

    @Test
    void forking() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source", "value1");
        log.add("source", "value2");
        log.add("source", "value3");

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(3));

        Log<Integer, String> forked = log.fork();

        assertThat(forked.parent(), is(log.id()));
        assertThat(forked.id(), is(not(log.id())));
        assertThat(forked.isEmpty(), is(false));
        assertThat(forked.size(), is(3));
    }

    @Test
    void sameValueCanBeInsertedMultipleTimes() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source", "value1");
        log.add("source", "value1");
        log.add("source", "value1");

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(3));
    }

    @Test
    void filteringEntries() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source1", "value11");
        log.add("source1", "value12");
        log.add("source2", "value21");
        log.add("source2", "value22");
        log.add("source3", "value31");
        log.add("source3", "value32");

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(6));

        Set<Log.Entry<Integer, String>> all = log.stream()
                .filter(new PassAll<>())
                .collect(Collectors.toSet());

        assertThat(all.isEmpty(), is(false));
        assertThat(all.size(), is(6));


        Set<Log.Entry<Integer, String>> nothing = log.stream()
                .filter(new RefuseAll<>())
                .collect(Collectors.toSet());

        assertThat(nothing.isEmpty(), is(false));
    }

    @Test
    void sortingEntries() {
        Log<Integer, String> log = new ListLog<>(new ArrayList<>());

        log.add("source1", "value11");
        log.add("source1", "value12");
        log.add("source2", "value21");
        log.add("source2", "value22");
        log.add("source3", "value31");
        log.add("source3", "value32");

        assertThat(log.isEmpty(), is(false));
        assertThat(log.size(), is(6));


        List<Log.Entry<Integer, String>> sorted = log.parallelStream()
                .sorted()
                .filter(new PassAll<>())
                .collect(Collectors.toList());

        Log.Entry<Integer, String> firstValue = sorted.iterator().next();
        assertThat(firstValue.index(), is(0));

        List<Log.Entry<Integer, String>> reversed = log.parallelStream()
                .sorted(reverseOrder())
                .filter(new PassAll<>())
                .collect(Collectors.toList());

        firstValue = reversed.iterator().next();
        assertThat(firstValue.index(), is(5));
    }

    @Test
    void hashCodeEquals() {
        Log<Integer, String> log1 = new ListLog<>(new ArrayList<>());
        Log<Integer, String> log2 = new ListLog<>(new ArrayList<>());

        assertThat(log1, is(not(log2)));

        UUID id = UUID.randomUUID();
        log1 = new ListLog<>(id, new ArrayList<>());
        log2 = new ListLog<>(id, new ArrayList<>());

        assertThat(log1, is(log2));
        assertThat(log1.hashCode(), is(log2.hashCode()));

        id = UUID.randomUUID();
        log1 = new ListLog<>(id, new ArrayList<>());
        log1.add("source", "value1");
        log1.add("source", "value2");
        log1.add("source", "value3");

        log2 = new ListLog<>(id, new ArrayList<>());
        log2.add("source", "value1");
        log2.add("source", "value2");
        log2.add("source", "value3");

        assertThat(log1, is(log2));
        assertThat(log1.hashCode(), is(log2.hashCode()));

        Log<Integer, String>  log = new ListLog<>(new ArrayList<>());
        log.add("source", "value1");
        log.add("source", "value2");
        log.add("source", "value3");

        Log<Integer, String> forked = log.fork();

        assertThat(log, is(not(forked)));
    }
}