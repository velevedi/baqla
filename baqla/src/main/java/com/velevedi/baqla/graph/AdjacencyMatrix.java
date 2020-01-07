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

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.velevedi.baqla.util.ArrayUtils.containsAll;
import static com.velevedi.baqla.util.ArrayUtils.indexOf;

/**
 * Adjacency matrix to represent graph of calculations.
 *
 * <code>
 * matrix size = number of nodes * number of nodes
 * </code>
 * <p>
 * There is no limitations on the type of a graph that could be represented by this data structure.
 * Each node on the matrix can have previous (preceding to this node) and next (following after this node) nodes.
 * This object operates integer nodes indices only.
 * The mapping between the index and a business object is outside of ths matrix implementation.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong> If multiple threads access
 * adjacency matrix, it <i>must</i> be synchronized externally.
 * </p>
 */
public class AdjacencyMatrix {

    /**
     * Array of links [from] [to]
     */
    private final int links[][];

    /**
     * Builds the matrix for a specified number of nodes. Once created the number of nodes can not be changed.
     *
     * @param numberOfNodes the number of nodes to represent.
     */
    public AdjacencyMatrix(int numberOfNodes) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("Number of nodes must be more than zero");
        }
        this.links = new int[numberOfNodes][numberOfNodes];
    }

    /**
     * Returns the length of the array backing matrix
     *
     * @return array's length
     */
    public int length() {
        return links.length;
    }

    /**
     * Returns the number of all elements of the array
     *
     * @return overall size of the matrix
     */
    public long size() {
        return links.length * links.length;
    }

    /**
     * Links 2 nodes together. The link is directed.
     *
     * @param fromNodeIndex source of the link
     * @param toNodeIndex   destination of the link
     * @return matrix object to chain method calls.
     */
    public AdjacencyMatrix link(int fromNodeIndex, int toNodeIndex) {
        links[fromNodeIndex][toNodeIndex] = 1;
        return this;
    }

    /**
     * Returns an array of nodes next to a node provided as argument.
     *
     * @param fromNodeIndex index of a node
     * @return an array of next nodes
     */
    public int[] getNextNodes(int fromNodeIndex) {
        if (fromNodeIndex < 0) {
            throw new IllegalArgumentException("Node index can not be negative");
        }
        return IntStream.range(0, links.length)
                .filter(i -> links[fromNodeIndex][i] == 1)
                .toArray();
    }

    /**
     * Returns an array of next nodes for an array of nodes provided.
     *
     * @param fromNodeIndices array of nodes' indices
     * @return an array of next nodes
     */
    public int[] getNextNodes(int[] fromNodeIndices) {
        if (fromNodeIndices == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        int[] result = new int[0];
        for (int nodeIndex : fromNodeIndices) {
            result = IntStream.concat(Arrays.stream(result), Arrays.stream(getNextNodes(nodeIndex)))
                    .toArray();
        }
        return Arrays.stream(result).distinct().toArray();
    }

    /**
     * Returns an array of nodes previous to a node provided as argument.
     *
     * @param toNodeIndex index of a node
     * @return an array of previous nodes
     */
    public int[] getPreviousNodes(int toNodeIndex) {
        if (toNodeIndex < 0) {
            throw new IllegalArgumentException("Node index can not be negative");
        }
        return IntStream.range(0, links.length)
                .filter(i -> links[i][toNodeIndex] == 1)
                .toArray();
    }

    /**
     * Returns an array of previous nodes for an array of nodes provided.
     *
     * @param toNodeIndices array of nodes' indices
     * @return an array of previous nodes
     */
    public int[] getPreviousNodes(int[] toNodeIndices) {
        if (toNodeIndices == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        int[] result = new int[0];
        for (int nodeIndex : toNodeIndices) {
            result = IntStream.concat(Arrays.stream(result), Arrays.stream(getPreviousNodes(nodeIndex)))
                    .toArray();
        }
        return Arrays.stream(result).distinct().toArray();
    }

    /**
     * Each graph has initial nodes where a calculation starts. This method returns all initial nodes.
     *
     * @return all initial nodes of a graph represented by the adjacency matrix.
     */
    public int[] getInitialNodes() {
        return IntStream.range(0, links.length)
                .filter(i -> getPreviousNodes(i).length == 0)
                .toArray();
    }

    /**
     * Each graph has final nodes where a calculation stops. This method returns all final nodes.
     *
     * @return all final nodes of a graph represented by the adjacency matrix.
     */
    public int[] getFinalNodes() {
        return IntStream.range(0, links.length)
                .filter(i -> getNextNodes(i).length == 0)
                .toArray();
    }

    /**
     * A group of nodes can have a mix of of previous and next nodes. This method selects only next nodes from
     * a group provided as arguments. In other words, this method eliminates all previous tasks if next task
     * exists in the provided group.
     *<pre>
     * Example:
     * Let's imagine our graph has 5 nodes: a, b, c, d, e
     *
     * Links:
     *         a -&gt; d
     *         b -&gt; d
     *         c -&gt; e
     *         d -&gt; e
     *
     * List&lt;String&gt; nodes = Arrays.asList("a", "b", "c", "d", "e");
     *
     * AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
     *         .link(nodes.indexOf("a"), nodes.indexOf("d"))
     *         .link(nodes.indexOf("b"), nodes.indexOf("d"))
     *         .link(nodes.indexOf("c"), nodes.indexOf("e"))
     *         .link(nodes.indexOf("d"), nodes.indexOf("e"));
     *
     * int[] frontLineNodes = matrix.getFrontLineNodes(
     *         new int[]{nodes.indexOf("a"), nodes.indexOf("b"), nodes.indexOf("d")}
     * );
     *
     * frontLineNodes will have index of the "d" node
     * </pre>
     *
     * @param nodeIndices node indices to filter
     * @return front line nodes
     */
    public int[] getFrontLineNodes(int[] nodeIndices) {
        final int[] localCopy = Arrays.copyOf(nodeIndices, nodeIndices.length);

        // do not iterate over the local copy array as we modify it
        for (int nodeIndex : nodeIndices) {
            for (int previousNode : getPreviousNodes(nodeIndex)) {

                int index = indexOf(previousNode, nodeIndices);

                if (index >= 0) {
                    localCopy[index] = -1;
                }
            }
        }
        return IntStream.range(0, localCopy.length)
                .filter(i -> localCopy[i] >= 0)
                .map(i -> localCopy[i])
                .toArray();
    }

    /**
     * This method is being used to select nodes that should be processed.
     * The criteria for the node to be processed is it needs be from the front line and it should have all
     * dependencies available.
     * The logic behind this requirement is simple. This matrix is being used to model calculations and to
     * progress further a task needs to have all data required for successful running.
     * If no data is available the task will not be invoked.
     * <p>
     * The algorithm is:
     * 1. from the group of incoming nodes filter front line nodes only
     * 2. select next nodes for the front line nodes
     * 3. ensure next nodes have required data to progress
     * </p>
     *
     * @param nodeIndices a group of nodes to filter
     * @return returns a group of nodes which can be processed further down to generate new values
     * specified by a calculation graph.
     * @see Graph
     */
    public int[] getNodesToProcess(int[] nodeIndices) {
        int[] frontLine = getFrontLineNodes(nodeIndices);
        return Arrays
                .stream(getNextNodes(frontLine))
                .filter(i -> containsAll(frontLine, getPreviousNodes(i)))
                .distinct()
                .toArray();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("from (row) -> to (column)\n");
        for (int[] row : links) {
            builder.append("        ").append(Arrays.toString(row)).append('\n');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdjacencyMatrix matrix = (AdjacencyMatrix) o;
        return Arrays.deepEquals(links, matrix.links);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(links);
    }
}
