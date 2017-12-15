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

package com.velevedi.baqla.simple.filter;

import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.simple.ArrayLog;
import com.velevedi.baqla.simple.LogEntry;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 */
public class AllLatestValuesFilterTest {

    @Test
    public void testValuesSelected() throws Exception {
        Log log = new ArrayLog(10);

        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("A").payload(1L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("A").payload(2L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("A").payload(3L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("B").payload(10L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("C").payload(51L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("B").payload(1L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("B").payload(2L).build()
        );
        log.add(
                LogEntry.newBuilder().id(log.nextEntryId()).taskId("B").payload(3L).build()
        );

        assertThat(log.size(), is(8));

        List<Entry> entries = log.find(new AllLatestValuesFilter());

        assertThat(entries.size(), is(3));

        Map<String, Entry> entriesAsMap = entries.stream().
                collect(toMap(Entry::taskId, entry -> entry));

        assertThat(entriesAsMap.get("A").payload(), is(3L));
        assertThat(entriesAsMap.get("B").payload(), is(3L));
        assertThat(entriesAsMap.get("C").payload(), is(51L));
    }
}