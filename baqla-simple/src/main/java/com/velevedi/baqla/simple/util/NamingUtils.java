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

package com.velevedi.baqla.simple.util;

import com.velevedi.baqla.Entry;
import com.velevedi.baqla.Task;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 */
public final class NamingUtils {

    private NamingUtils() {
    }


    public static Set<String> tasksToNames(Set<Task> tasks) {
        return tasks.stream().
                map(Task::name).
                collect(toSet());
    }

    public static Set<String> entriesToTasksNames(List<Entry> entries) {
        return entries.stream().
                map(Entry::taskId).
                collect(toSet());
    }

}
