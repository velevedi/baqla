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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AdjacencyMatrixTest {

    @Test
    void creation() {
        AdjacencyMatrix matrix = new AdjacencyMatrix(3);
        assertThat(matrix.length(), is(3));
        assertThat(matrix.size(), is(9L));
    }

    @Test
    void incorrectCreation() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AdjacencyMatrix(0));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AdjacencyMatrix(-1));
    }

    @Test
    void readablePrinting() {
        AdjacencyMatrix matrix = new AdjacencyMatrix(3);
        assertThat(matrix.toString(),
                is("from (row) -> to (column)\n" +
                        "        [0, 0, 0]\n" +
                        "        [0, 0, 0]\n" +
                        "        [0, 0, 0]\n"));
    }

    @Test
    void getFrontLineNodes() {
        List<String> nodes = Arrays.asList("a", "b", "c", "d", "e", "f");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] frontLineNodes = matrix.getFrontLineNodes(
                new int[]{nodes.indexOf("a"), nodes.indexOf("b"), nodes.indexOf("d")}
        );
        assertThat(frontLineNodes.length, is(1));
        assertThat(nodes.get(frontLineNodes[0]), is("d"));

        frontLineNodes = matrix.getFrontLineNodes(
                new int[]{nodes.indexOf("d"), nodes.indexOf("c")}
        );
        assertThat(frontLineNodes.length, is(2));
        assertThat(nodes.get(frontLineNodes[0]), is("d"));
        assertThat(nodes.get(frontLineNodes[1]), is("c"));

        // changing the order of nodes to ensure order of elements is not important
        nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        frontLineNodes = matrix.getFrontLineNodes(
                new int[]{nodes.indexOf("a"), nodes.indexOf("b"), nodes.indexOf("d")}
        );

        assertThat(frontLineNodes.length, is(1));
        assertThat(nodes.get(frontLineNodes[0]), is("d"));
    }

    @Test
    void getNextNodes() {
        List<String> nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] nextNodes = matrix.getNextNodes(nodes.indexOf("a"));

        assertThat(nextNodes.length, is(1));
        assertThat(nodes.get(nextNodes[0]), is("d"));


        nextNodes = matrix.getNextNodes(nodes.indexOf("d"));

        assertThat(nextNodes.length, is(2));
        assertThat(nodes.get(nextNodes[0]), is("e"));
        assertThat(nodes.get(nextNodes[1]), is("f"));


        nextNodes = matrix.getNextNodes(new int[]{nodes.indexOf("a"), nodes.indexOf("b")});

        assertThat(nextNodes.length, is(1));
        assertThat(nodes.get(nextNodes[0]), is("d"));


        nextNodes = matrix.getNextNodes(new int[0]);

        assertThat(nextNodes.length, is(0));


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.getNextNodes(null));


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.getNextNodes(-1));


        nextNodes = matrix.getNextNodes(nodes.indexOf("f"));

        assertThat(nextNodes.length, is(0));

    }

    @Test
    void getPreviousNodes() {
        List<String> nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] previousNodes = matrix.getPreviousNodes(nodes.indexOf("d"));

        assertThat(previousNodes.length, is(2));
        assertThat(nodes.get(previousNodes[0]), is("a"));
        assertThat(nodes.get(previousNodes[1]), is("b"));


        previousNodes = matrix.getPreviousNodes(nodes.indexOf("f"));

        assertThat(previousNodes.length, is(1));
        assertThat(nodes.get(previousNodes[0]), is("d"));


        previousNodes = matrix.getPreviousNodes(new int[]{nodes.indexOf("f"), nodes.indexOf("e")});

        assertThat(previousNodes.length, is(2));
        assertThat(nodes.get(previousNodes[0]), is("d"));
        assertThat(nodes.get(previousNodes[1]), is("c"));


        previousNodes = matrix.getNextNodes(new int[0]);

        assertThat(previousNodes.length, is(0));


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.getPreviousNodes(null));


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> matrix.getPreviousNodes(-1));


        previousNodes = matrix.getPreviousNodes(nodes.indexOf("a"));

        assertThat(previousNodes.length, is(0));
    }

    @Test
    void getInitialNodes() {
        List<String> nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] initialNodes = matrix.getInitialNodes();

        assertThat(initialNodes.length, is(3));
        assertThat(nodes.get(initialNodes[0]), is("a"));
        assertThat(nodes.get(initialNodes[1]), is("b"));
        assertThat(nodes.get(initialNodes[2]), is("c"));


        nodes = Arrays.asList("a");

        matrix = new AdjacencyMatrix(nodes.size());

        initialNodes = matrix.getInitialNodes();

        assertThat(initialNodes.length, is(1));
    }

    @Test
    void getFinalNodes() {
        List<String> nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] finalNodes = matrix.getFinalNodes();

        assertThat(finalNodes.length, is(2));
        assertThat(nodes.get(finalNodes[0]), is("e"));
        assertThat(nodes.get(finalNodes[1]), is("f"));


        nodes = Arrays.asList("a");

        matrix = new AdjacencyMatrix(nodes.size());

        finalNodes = matrix.getInitialNodes();

        assertThat(finalNodes.length, is(1));
    }

    @Test
    void getNodesToProcess() {
        List<String> nodes = Arrays.asList("e", "f", "a", "b", "c", "d");

        AdjacencyMatrix matrix = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("c"), nodes.indexOf("e"))
                .link(nodes.indexOf("a"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("e"))
                .link(nodes.indexOf("b"), nodes.indexOf("d"))
                .link(nodes.indexOf("d"), nodes.indexOf("f"));

        int[] nodesToProcess = matrix.getNodesToProcess(new int[]{
                nodes.indexOf("a"),
                nodes.indexOf("b"),
                nodes.indexOf("c")
        });

        assertThat(nodesToProcess.length, is(1));
        assertThat(nodes.get(nodesToProcess[0]), is("d"));


        nodesToProcess = matrix.getNodesToProcess(new int[]{
                nodes.indexOf("a"),
                nodes.indexOf("b"),
                nodes.indexOf("d")
        });

        assertThat(nodesToProcess.length, is(1));
        assertThat(nodes.get(nodesToProcess[0]), is("f"));


        nodesToProcess = matrix.getNodesToProcess(new int[]{
                nodes.indexOf("a"),
                nodes.indexOf("b"),
                nodes.indexOf("c"),
                nodes.indexOf("d")
        });

        assertThat(nodesToProcess.length, is(2));
        assertThat(nodes.get(nodesToProcess[0]), is("e"));
        assertThat(nodes.get(nodesToProcess[1]), is("f"));
    }

    @Test
    void hashCodeEquals() {
        AdjacencyMatrix matrix11 = new AdjacencyMatrix(1);
        AdjacencyMatrix matrix12 = new AdjacencyMatrix(1);
        assertEquals(matrix11, matrix12);
        assertEquals(matrix11.hashCode(), matrix12.hashCode());

        AdjacencyMatrix matrix21 = new AdjacencyMatrix(10);
        AdjacencyMatrix matrix22 = new AdjacencyMatrix(10);
        assertEquals(matrix21, matrix22);
        assertEquals(matrix21.hashCode(), matrix22.hashCode());

        AdjacencyMatrix matrix31 = new AdjacencyMatrix(1);
        AdjacencyMatrix matrix32 = new AdjacencyMatrix(10);
        assertNotEquals(matrix31, matrix32);
        assertNotEquals(matrix31.hashCode(), matrix32.hashCode());

        List<String> nodes = Arrays.asList("a", "b", "c");

        AdjacencyMatrix matrix41 = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("a"), nodes.indexOf("c"))
                .link(nodes.indexOf("b"), nodes.indexOf("c"));
        AdjacencyMatrix matrix42 = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("a"), nodes.indexOf("c"))
                .link(nodes.indexOf("b"), nodes.indexOf("c"));
        assertEquals(matrix41, matrix42);
        assertEquals(matrix41.hashCode(), matrix42.hashCode());

        AdjacencyMatrix matrix51 = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("a"), nodes.indexOf("c"))
                .link(nodes.indexOf("b"), nodes.indexOf("c"));
        AdjacencyMatrix matrix52 = new AdjacencyMatrix(nodes.size())
                .link(nodes.indexOf("a"), nodes.indexOf("b"))
                .link(nodes.indexOf("b"), nodes.indexOf("c"));
        assertNotEquals(matrix51, matrix52);
        assertNotEquals(matrix51.hashCode(), matrix52.hashCode());
    }

}