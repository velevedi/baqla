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

import com.velevedi.baqla.Task;

import java.util.Set;

/**
 * Graph represents a collection of interdependent tasks that need be run together to fulfil the business requirements.
 * Each task calculates result and can provide data for upstream tasks to perform actions.
 *
 * @param <V> data type of the tasks that will perform calculations
 * @see com.velevedi.baqla.graph.AdjacencyMatrix
 */
public interface Graph<V> {

    /**
     * Type of the graph
     */
    enum Type {
        DIRECTED,
        DIRECTED_ACYCLIC,
        DIRECTED_WEIGHTED
    }

    /**
     * Each graph has a name associated with it. This name is used to identify the graph.
     *
     * @return name of the graph
     */
    String name();

    /**
     * Graph has a lot of interconnected tasks. Each task can send calculation results to upstream tasks.
     * This method returns a set of tasks that will be processed next.
     *
     * @param taskIds a set of task ids to select the next tasks for
     * @return a set of next tasks to run
     */
    Set<Task<V>> nextTasks(Set<String> taskIds);

    /**
     * Graph has a lot of interconnected tasks. This method returns a group of tasks that have been run before.
     * Method searches only one level down.
     *
     * @param taskIds a set of task ids to select previous tasks for
     * @return a set of previous tasks run before the set of provided
     */
    Set<Task<V>> previousTasks(Set<String> taskIds);

    /**
     * Graph has entry points where the calculation starts. This method returns tasks that initiate calculation.
     *
     * @return a set of initial tasks
     */
    Set<Task<V>> initialTasks();

    /**
     * Graph has a set of tasks that finish calculation. This method returns tasks where the calculation stops.
     *
     * @return a set of final tasks
     */
    Set<Task<V>> finalTasks();

    /**
     * Returns the number of tasks associated with the graph
     *
     * @return number of tasks
     */
    int numberOfTasks();
}
