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

import java.util.*;
import java.util.stream.Collectors;

public class DirectedGraph<V> implements Graph<V> {

    private final String name;
    private final Map<String, Integer> idToIndex = new HashMap<>();
    private final Map<Integer, String> indexToId = new HashMap<>();

    private final Map<String, Task<V>> nameToTask = new HashMap<>();

    private final AdjacencyMatrix adjacencyMatrix;

    DirectedGraph(String name, Set<Task<V>> tasks, Set<Link> links) {
        this.name = name;
        int counter = 0;
        for (Task<V> task : tasks) {
            int index = counter++;
            String taskName = task.id();
            nameToTask.put(taskName, task);
            idToIndex.put(taskName, index);
            indexToId.put(index, taskName);
        }
        adjacencyMatrix = new AdjacencyMatrix(tasks.size());
        for (Link link : links) {
            adjacencyMatrix.link(idToIndex.get(link.getFrom()), idToIndex.get(link.getTo()));
        }
    }

    @Override
    public String name() {
        return name;
    }

    private Set<Task<V>> nodeIndexToTask(int... nodeIndices) {
        return Arrays.stream(nodeIndices)
                .mapToObj(i -> nameToTask.get(indexToId.get(i)))
                .collect(Collectors.toSet());
    }

    private int[] taskIdToNodeIndex(Set<String> taskIds) {
        return taskIds.stream()
                .filter(i -> idToIndex.get(i) != null)
                .mapToInt(idToIndex::get)
                .toArray();
    }

    @Override
    public Set<Task<V>> nextTasks(Set<String> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return Collections.emptySet();
        }
        return nodeIndexToTask(
                adjacencyMatrix.getNodesToProcess(taskIdToNodeIndex(taskIds))
        );
    }

    @Override
    public Set<Task<V>> previousTasks(Set<String> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Task<V>> result = new HashSet<>();
        for (String taskId : taskIds) {

            Integer index = idToIndex.get(taskId);
            if (index == null) continue;

            int[] nodes = adjacencyMatrix.getPreviousNodes(index);
            for (int id : nodes) {
                result.add(nameToTask.get(indexToId.get(id)));
            }
        }
        return result;
    }

    @Override
    public Set<Task<V>> initialTasks() {
        return Arrays.stream(adjacencyMatrix.getInitialNodes())
                .mapToObj(
                        id -> nameToTask.get(indexToId.get(id))
                )
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Task<V>> finalTasks() {
        return Arrays.stream(adjacencyMatrix.getFinalNodes())
                .mapToObj(
                        id -> nameToTask.get(indexToId.get(id))
                )
                .collect(Collectors.toSet());
    }

    @Override
    public int numberOfTasks() {
        return adjacencyMatrix.length();
    }

}
