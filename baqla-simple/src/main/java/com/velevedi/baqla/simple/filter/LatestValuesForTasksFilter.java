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

package com.velevedi.baqla.simple.filter;

import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Filter that uses task identifiers for entries filtering.
 * It finds latest entries only for the tasks supplied.
 */
public class LatestValuesForTasksFilter implements Filter {

    private final Set<String> taskIds;
    private final Set<String> found;
    private volatile boolean complete = false;

    public LatestValuesForTasksFilter(String... taskIds) {
        this(Arrays.stream(taskIds).collect(toSet()));
    }

    public LatestValuesForTasksFilter(Set<String> taskIds) {
        this.taskIds = taskIds;
        this.found = new HashSet<>(taskIds.size());
    }

    @Override
    public boolean test(Entry entry) {
        if (entry == null || complete || found.contains(entry.taskId())) {
            return false;
        }
        boolean isValidEntry = taskIds.contains(entry.taskId());
        if (isValidEntry) {
            found.add(entry.taskId());
            if (found.size() == taskIds.size()) {
                complete = true;
            }
        }
        return isValidEntry;
    }

    @Override
    public boolean complete() {
        return complete;
    }
}
