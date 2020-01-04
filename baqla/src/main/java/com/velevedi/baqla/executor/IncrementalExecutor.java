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

import com.velevedi.baqla.Executor;
import com.velevedi.baqla.Graph;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;
import com.velevedi.baqla.predicate.OneValuePerSource;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

/**
 * Executor which runs tasks one level at a time. This executor does not progress through entire Graph to run all tasks
 * available. Instead it just runs level after level giving control to the caller on how to progress.
 * The results of the calculations could be taken from the Log.
 */
public class IncrementalExecutor implements Executor {

    @Override
    public <I extends Comparable<? super I>, V> void submit(Graph<V> graph, Log<I, V> log) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph argument could not be null");
        }
        if (log == null) {
            throw new IllegalArgumentException("Log arguments could not be null");
        }

        Set<Task<V>> tasksToRun;
        if (log.isEmpty()) {
            tasksToRun = graph.initialTasks();
        } else {
            Set<String> latestNodes = log.stream()
                    .sorted(reverseOrder())
                    .filter(new OneValuePerSource<>())
                    .map(Log.Entry::source)
                    .collect(Collectors.toSet());

            tasksToRun = graph.nextTasks(latestNodes);
        }

        for (Task<V> task : tasksToRun) {
            Set<String> previousTasksNames = graph.previousTasks(Set.of(task.id()))
                    .stream()
                    .map(Task::id)
                    .collect(Collectors.toSet());
            Set<Log.Entry<I, V>> arguments = log.stream()
                    .filter(new OneValuePerSource<>())
                    .filter(e -> previousTasksNames.contains(e.source()))
                    .collect(Collectors.toSet());
            V result = task.callOn(arguments);
            log.add(task.id(), result);
        }
    }
}
