package com.velevedi.baqla.graph;

import com.velevedi.baqla.AbstractTask;
import com.velevedi.baqla.Graph;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;
import com.velevedi.baqla.annotation.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

class GraphBuilderTest {

    @Test
    void buildingGraph() {
        TestTask a = new TestTask("a");
        TestTask b = new TestTask("b");
        TestTask c = new TestTask("c");
        TestTask d = new TestTask("d");
        TestTask e = new TestTask("e");
        TestTask f = new TestTask("f");
        Graph<Integer> graph = GraphBuilder.<Integer>directedGraph("test")
                .connect(a, d)
                .connect(b, d)
                .connect(c, e)
                .connect(d, e)
                .connect(d, f)
                .build();

        assertThat(graph, not(nullValue()));
        assertThat(graph.name(), is("test"));
        assertThat(graph.numberOfTasks(), is(6));
    }

    @Test
    void buildingGraphUsingAnnotationsAndInnerClass() {
        Task<Integer> a = new @TaskType(id = "a") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 1;
            }
        };
        Task<Integer> b = new @TaskType(id = "b") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 2;
            }
        };
        Task<Integer> c = new @TaskType(id = "c") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 3;
            }
        };
        Task<Integer> d = new @TaskType(id = "d") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 4;
            }
        };
        Task<Integer> e = new @TaskType(id = "e") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 5;
            }
        };
        Task<Integer> f = new @TaskType(id = "f") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 6;
            }
        };
        Graph<Integer> graph = GraphBuilder.<Integer>directedGraph("test")
                .connect(a, d)
                .connect(b, d)
                .connect(c, e)
                .connect(d, e)
                .connect(d, f)
                .build();

        assertThat(graph, not(nullValue()));
        assertThat(graph.name(), is("test"));
        assertThat(graph.numberOfTasks(), is(6));
    }

    @Test
    void incorrectBuildingGraph() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph(null).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("   ").build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test").build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test")
                        .connect(null, null)
                        .build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test")
                        .connect(new TestTask("a"), null)
                        .build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test")
                        .connect(null, new TestTask("a"))
                        .build());
    }

    @Test
    void duplicateTaskIdsFound() {
        Task<Integer> a = new NullIdTask();
        Task<Integer> b = new NullIdTask();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test")
                        .connect(a, b)
                        .build());

        Task<Integer> c = new SameIdTask();
        Task<Integer> d = new SameIdTask();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> GraphBuilder.<Integer>directedGraph("test")
                        .connect(c, d)
                        .build());
    }

    private static class NullIdTask implements Task<Integer> {

        @Override
        public String id() {
            return null;
        }

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            return 1;
        }
    }

    private static class SameIdTask implements Task<Integer> {

        @Override
        public String id() {
            return "id";
        }

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            return 1;
        }
    }

}
