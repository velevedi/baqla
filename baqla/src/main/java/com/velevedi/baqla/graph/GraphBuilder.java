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

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to build Graph objects.
 *
 * @param <V> data type of the tasks that will perform calculations
 */
public class GraphBuilder<V> {

    private String name = "";
    private Set<Link> links = new HashSet<>();
    private Set<Task<V>> tasks = new HashSet<>();
    private Graph.Type type;

    /**
     * Creates a builder for directed graph
     *
     * @return graph builder for directed type graphs
     */
    public static <V> GraphBuilder<V> directedGraph(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Graph name can not have blank value");
        }
        GraphBuilder<V> builder = new GraphBuilder<>();
        builder.type = Graph.Type.DIRECTED;
        builder.name = name;
        return builder;
    }

    /**
     * Connects two tasks together to form  directed link
     *
     * @param from the data from "from" task will be sent to "to" task
     * @param to   the tsk that will be run after "from" task
     * @return graph builder to chain methods
     */
    public GraphBuilder<V> connect(Task<V> from, Task<V> to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Tasks can not be null values");
        }
        links.add(new Link()
                .from(from.id())
                .to(to.id())
        );
        tasks.add(from);
        tasks.add(to);
        return this;
    }

    /**
     * Builds graph based on configuration provided
     *
     * @return Graph object to perform calculations on or null if
     * I
     */
    public Graph<V> build() {
        if (duplicateIdsFound(tasks)) {
            throw new IllegalArgumentException("Unable to initialize graph. Duplicate task ids found.");
        }
        if (Graph.Type.DIRECTED.equals(type)) {
            return new DirectedGraph<>(name, tasks, links);
        }
        throw new IllegalArgumentException("Unrecognizable graph type");
    }

    /**
     * Tasks in a graph are identified by ids. So, it's important to check for correct initialization.
     *
     * @param tasks set of tasks to check
     * @return true if a set of tasks contains tasks with duplicate ids.
     */
    private boolean duplicateIdsFound(Set<Task<V>> tasks) {
        Set<String> existingIds = new HashSet<>();
        for (Task<V> task : tasks) {
            if (existingIds.contains(task.id())) {
                return true;
            }
            existingIds.add(task.id());
        }
        return false;
    }

}
