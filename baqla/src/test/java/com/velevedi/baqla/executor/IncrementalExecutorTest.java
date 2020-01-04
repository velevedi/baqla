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

package com.velevedi.baqla.executor;

import com.velevedi.baqla.*;
import com.velevedi.baqla.graph.GraphBuilder;
import com.velevedi.baqla.predicate.OneValuePerSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

class IncrementalExecutorTest {

    Graph<Integer> graph;
    Executor executor;

    @BeforeEach
    void setUp() {
        Sum sum = new Sum("sum");
        Multiply multiply = new Multiply("multiply");
        graph = GraphBuilder.<Integer>directedGraph("test")
                .connect(new Constant("5", 5), sum)
                .connect(new Constant("3", 3), sum)
                .connect(sum, multiply)
                .connect(new Constant("2", 2), multiply)
                .build();
        executor = new IncrementalExecutor();
    }

    @Test
    void performRun() {

        Log<Integer, Integer> log = new ListLog<>(new ArrayList<>());

        executor.submit(graph, log);
        executor.submit(graph, log);
        executor.submit(graph, log);

        List<Log.Entry<Integer, Integer>> result = extractCalculationResults(log);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).source(), is("multiply"));
        assertThat(result.get(0).value(), is(16));
    }


    @Test
    void additionalInvocationsDoNotRun() {

        Log<Integer, Integer> log = new ListLog<>(new ArrayList<>());

        executor.submit(graph, log);
        executor.submit(graph, log);
        executor.submit(graph, log);
        // please note that 3 runs is enough to finish calculation defined by the graph
        // if we submit more it will not run as the calculation has been finished
        executor.submit(graph, log);
        executor.submit(graph, log);
        executor.submit(graph, log);
        executor.submit(graph, log);
        executor.submit(graph, log);
        // does not matter how many times

        List<Log.Entry<Integer, Integer>> result = extractCalculationResults(log);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).source(), is("multiply"));
        assertThat(result.get(0).value(), is(16));
    }

    @Test
    void performRunOnMultipleLogs() {
        Log<Integer, Integer> log = new ListLog<>(new ArrayList<>());

        executor.submit(graph, log);

        // forked here to keep the intermediate state
        Log<Integer, Integer> forked = log.fork();

        // then the order of invocations is not important
        executor.submit(graph, log);
        executor.submit(graph, forked);
        executor.submit(graph, forked);
        executor.submit(graph, log);


        assertThat(forked.parent(), is(log.id()));
        assertThat(forked.id(), is(not(log.id())));

        List<Log.Entry<Integer, Integer>> result1 = extractCalculationResults(log);

        assertThat(result1.size(), is(1));
        assertThat(result1.get(0).source(), is("multiply"));
        assertThat(result1.get(0).value(), is(16));

        List<Log.Entry<Integer, Integer>> result2 = extractCalculationResults(log);

        assertThat(result2.size(), is(1));
        assertThat(result2.get(0).source(), is("multiply"));
        assertThat(result2.get(0).value(), is(16));
    }

    @Test
    void incorrectArgumentsSupplied() {
        Log<Integer, Integer> log = new ListLog<>(new ArrayList<>());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> executor.submit(null, log));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> executor.submit(graph, null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> executor.submit(null, null));
    }

    private List<Log.Entry<Integer, Integer>> extractCalculationResults(Log<Integer, Integer> log) {
        Set<String> finalTasksNames = graph.finalTasks().stream()
                .map(Task::id)
                .collect(Collectors.toSet());

        return log.stream()
                .sorted(reverseOrder())
                .filter(new OneValuePerSource<>())
                .filter(e -> finalTasksNames.contains(e.source()))
                .collect(Collectors.toList());
    }

    static class Multiply extends AbstractTask<Integer> {

        public Multiply(String name) {
            super(name);
        }

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            int result = 1;
            for (Log.Entry<I, Integer> entry : entries) {
                result *= entry.value();
            }
            return result;
        }
    }

    static class Sum extends AbstractTask<Integer> {

        public Sum(String name) {
            super(name);
        }

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            int result = 0;
            for (Log.Entry<I, Integer> entry : entries) {
                result += entry.value();
            }
            return result;
        }
    }

    static class Constant extends AbstractTask<Integer> {

        private final Integer value;

        public Constant(String name, Integer value) {
            super(name);
            this.value = value;
        }

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            return value;
        }
    }


}