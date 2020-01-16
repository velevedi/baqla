package com.velevedi.baqla.util;

import com.velevedi.baqla.Graph;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;
import com.velevedi.baqla.predicate.OneValuePerSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

public final class GraphUtils {

    public static <I extends Comparable<? super I>, V> List<Log.Entry<I, V>> extractResults(Graph<V> graph, Log<I, V> log) {
        Set<String> finalTasksNames = graph.finalTasks().stream()
                .map(Task::id)
                .collect(Collectors.toSet());
        return log.stream()
                .sorted(reverseOrder())
                .filter(e -> finalTasksNames.contains(e.source()))
                .filter(new OneValuePerSource<>())
                .collect(Collectors.toList());
    }

}
