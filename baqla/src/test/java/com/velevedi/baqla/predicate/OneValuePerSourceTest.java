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

package com.velevedi.baqla.predicate;

import com.velevedi.baqla.TestEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OneValuePerSourceTest {

    @Test
    void evaluateTheValue() {

        OneValuePerSource<Integer, String> predicate = new OneValuePerSource<>();

        TestEntry entry1 = new TestEntry("source1", 1, "value1");
        TestEntry entry2 = new TestEntry("source2", 2, "value2");
        TestEntry entry3 = new TestEntry("source3", 3, "value3");
        TestEntry entry4 = new TestEntry("source4", 4, "value4");

        assertTrue(predicate.test(entry1));
        assertTrue(predicate.test(entry2));
        assertTrue(predicate.test(entry3));
        assertTrue(predicate.test(entry4));
    }

    @Test
    void severalEntriesFromTheSameSource() {

        OneValuePerSource<Integer, String> predicate = new OneValuePerSource<>();

        TestEntry entry1 = new TestEntry("source1", 1, "value1");
        TestEntry entry2 = new TestEntry("source1", 2, "value2");
        TestEntry entry3 = new TestEntry("source1", 3, "value3");

        assertTrue(predicate.test(entry1));
        assertFalse(predicate.test(entry2));
        assertFalse(predicate.test(entry3));
    }

    @Test
    void evaluateNullEntryAsFalse() {
        OneValuePerSource<Integer, String> predicate = new OneValuePerSource<>();

        assertFalse(predicate.test(null));
    }
}