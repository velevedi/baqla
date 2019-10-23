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

import org.junit.jupiter.api.Test;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {

    @Test
    void testCreation1() {
        LogEntry logEntry1 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();

        LogEntry logEntry2 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();

        assertEquals(logEntry1, logEntry2);
        assertEquals(logEntry1.hashCode(), logEntry2.hashCode());
    }

    @Test
    void testCreation2() {
        LogEntry logEntry1 = LogEntry.newBuilder().id(1).taskId("sum").payload(123L).
                build();

        LogEntry logEntry2 = LogEntry.newBuilder().id(2).taskId("sum").payload(123L).
                build();

        assertNotEquals(logEntry1, logEntry2);
        assertNotEquals(logEntry1.hashCode(), logEntry2.hashCode());
    }

    @Test
    void testCreation3() {
        LogEntry logEntry = LogEntry.newBuilder().
                id(1).
                meta(singletonMap("key", "value")).
                taskId("sum").
                payload(123L).
                inputIds(singleton("source")).
                build();

        assertEquals(logEntry.id(), 1L);
        assertEquals(logEntry.meta().size(), 1);
        assertEquals(logEntry.meta().get("key"), "value");
        assertEquals(logEntry.taskId(), "sum");
        assertEquals(logEntry.payload(), 123L);
        assertEquals(logEntry.inputIds().size(), 1);
        assertEquals(logEntry.inputIds().iterator().next(), "source");
    }

    @Test
    void testIncorrectCreation1() {
        assertThrows(IllegalArgumentException.class,
                () -> LogEntry.newBuilder().id(1).build()
        );
    }

    @Test
    void testIncorrectCreation2() {
        assertThrows(IllegalArgumentException.class,
                () -> LogEntry.newBuilder().id(1).taskId("my task").build()
        );
    }

    @Test
    void testIncorrectCreation3() {
        assertThrows(IllegalArgumentException.class,
                () -> LogEntry.newBuilder().id(1).payload("my data").build()
        );
    }

}