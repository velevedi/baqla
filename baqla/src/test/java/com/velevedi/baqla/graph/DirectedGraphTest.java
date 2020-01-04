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

package com.velevedi.baqla.graph;

import com.velevedi.baqla.Graph;
import com.velevedi.baqla.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

class DirectedGraphTest {

    TestTask a = new TestTask("a");
    TestTask b = new TestTask("b");
    TestTask c = new TestTask("c");
    TestTask d = new TestTask("d");
    TestTask e = new TestTask("e");
    TestTask f = new TestTask("f");
    Graph<Integer> graph;

    @BeforeEach
    void setUp() {
        graph = GraphBuilder.<Integer>directedGraph("test")
                .connect(a, d)
                .connect(b, d)
                .connect(c, e)
                .connect(d, e)
                .connect(d, f)
                .build();
    }

    @Test
    void checkGraph() {
        assertThat(graph.name(), is("test"));
        assertThat(graph.numberOfTasks(), is(6));
    }

    @Test
    void initialTasks() {
        Set<Task<Integer>> initialTasks = graph.initialTasks();
        assertThat(initialTasks.size(), is(3));
        assertThat(initialTasks, contains(a, b, c));
    }

    @Test
    void finalTasks() {
        Set<Task<Integer>> finalTasks = graph.finalTasks();
        assertThat(finalTasks.size(), is(2));
        assertThat(finalTasks, contains(e, f));
    }

    @Test
    void nextTasks() {
        Set<Task<Integer>> nextTasks = graph.nextTasks(Set.of(a.id(), b.id()));
        assertThat(nextTasks.size(), is(1));
        assertThat(nextTasks, contains(d));

        nextTasks = graph.nextTasks(Set.of(d.id(), c.id()));
        assertThat(nextTasks.size(), is(2));
        assertThat(nextTasks, contains(e, f));

        // only tasks that could be processed further will be returned
        nextTasks = graph.nextTasks(Set.of(d.id()));
        assertThat(nextTasks.size(), is(1));
        assertThat(nextTasks, contains(f));

        // "e" depends on "d".  So, if "e" was ever processed then there was a run cycle that took value from "d" node
        // and run a cycle on next nodes ("e" and "f")
        // this means that f should also be calculated
        nextTasks = graph.nextTasks(Set.of(d.id(), e.id()));
        assertThat(nextTasks.size(), is(0));

        nextTasks = graph.nextTasks(Set.of(f.id(), e.id()));
        assertThat(nextTasks.size(), is(0));

        nextTasks = graph.nextTasks(Set.of("unknown"));
        assertThat(nextTasks.size(), is(0));

        nextTasks = graph.nextTasks(null);
        assertThat(nextTasks.size(), is(0));

        nextTasks = graph.nextTasks(Collections.emptySet());
        assertThat(nextTasks.size(), is(0));
    }

    @Test
    void previousTasks() {
        Set<Task<Integer>> previousTasks = graph.previousTasks(Set.of(a.id(), b.id()));
        assertThat(previousTasks.size(), is(0));

        previousTasks = graph.previousTasks(Set.of(d.id()));
        assertThat(previousTasks.size(), is(2));
        assertThat(previousTasks, contains(a, b));

        previousTasks = graph.previousTasks(Set.of(e.id()));
        assertThat(previousTasks.size(), is(2));
        assertThat(previousTasks, contains(c, d));

        previousTasks = graph.previousTasks(Set.of("unknown"));
        assertThat(previousTasks.size(), is(0));

        previousTasks = graph.previousTasks(null);
        assertThat(previousTasks.size(), is(0));

        previousTasks = graph.previousTasks(Collections.emptySet());
        assertThat(previousTasks.size(), is(0));
    }
}