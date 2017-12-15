/*
 *
 *  * Copyright (C) 2017. The Baqla Authors
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.velevedi.baqla.simple;

import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Flow;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;
import com.velevedi.baqla.simple.filter.LatestValuesForTasksFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.velevedi.baqla.simple.util.NamingUtils.entriesToTasksNames;
import static com.velevedi.baqla.simple.util.NamingUtils.tasksToNames;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Defines the calculation flow of tasks.
 */
public class RunFlow implements Flow {

    private final String name;
    private final String version;
    private final Set<Task> store = new HashSet<>();

    public RunFlow(String name, String version) {
        this.name = name;
        this.version = version;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public Flow withTask(Task task) {
        store.add(task);
        return this;
    }

    @Override
    public Set<Task> sources() {
        return store.stream().
                filter(Task::isSource).
                collect(toSet());
    }

    @Override
    public Set<Task> destinations() {
        return store.stream().
                filter(Task::isDestination).
                collect(toSet());
    }

    @Override
    public Set<Task> all() {
        return unmodifiableSet(store);
    }

    @Override
    public Set<Task> readyToRunOn(Log log) {
        Map<String, Task> allTasks = all().stream().
                collect(toMap(Task::name, task -> task));

        List<Entry> entries = log.find(new LatestValuesForTasksFilter(allTasks.keySet()));

        allTasks.keySet().removeAll(
                entriesToTasksNames(entries)
        );

        Set<String> rootNames = new HashSet<>();

        for (Map.Entry<String, Task> entry : allTasks.entrySet()) {
            rootNames.add(entry.getKey());
        }

        for (Map.Entry<String, Task> entry : allTasks.entrySet()) {
            rootNames.removeAll(
                    tasksToNames(entry.getValue().output())
            );
        }
        
        return rootNames.stream().
                map(allTasks::get).collect(toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunFlow that = (RunFlow) o;

        return name.equals(that.name)
                && version.equals(that.version)
                && store.equals(that.store);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + store.hashCode();
        return result;
    }

}
