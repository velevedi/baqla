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

import org.junit.Test;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LogEntryTest {

    @Test
    public void testCreation1() throws Exception {
        LogEntry logEntry1 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();
        
        LogEntry logEntry2 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();

        assertTrue(logEntry1.equals(logEntry2));
        assertTrue(logEntry1.hashCode() == logEntry2.hashCode());
    }

    @Test
    public void testCreation2() throws Exception {
        LogEntry logEntry1 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();

        LogEntry logEntry2 = LogEntry.newBuilder().id(2).taskId("sum").payload(123L).
                build();

        assertFalse(logEntry1.equals(logEntry2));
        assertFalse(logEntry1.hashCode() == logEntry2.hashCode());
    }

    @Test
    public void testCreation3() throws Exception {
        LogEntry logEntry = LogEntry.newBuilder().
                id(1).
                meta(singletonMap("key", "value")).
                taskId("sum").
                payload(123L).
                inputIds(singleton("source")).
                build();

        assertThat(logEntry.id(), is(1L));
        assertThat(logEntry.meta().size(), is(1));
        assertThat(logEntry.meta().get("key"), is("value"));
        assertThat(logEntry.taskId(), is("sum"));
        assertThat(logEntry.payload(), is(123L));
        assertThat(logEntry.inputIds().size(), is(1));
        assertThat(logEntry.inputIds().iterator().next(), is("source"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectCreation1() throws Exception {
        LogEntry.newBuilder().id(1).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectCreation2() throws Exception {
        LogEntry.newBuilder().id(1).taskId("my task").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectCreation3() throws Exception {
        LogEntry.newBuilder().id(1).payload("my data").build();
    }

}