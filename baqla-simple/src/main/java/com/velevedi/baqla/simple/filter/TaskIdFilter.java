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

import java.util.Set;

/**
 * Filter that uses task identifiers for entries filtering.
 * Scans the entire log for the entries with specified tasks ids.
 */
public class TaskIdFilter implements Filter {

    private final Set<String> taskIds;

    public TaskIdFilter(Set<String> taskIds) {
        this.taskIds = taskIds;
    }

    @Override
    public boolean test(Entry entry) {
        return entry != null && taskIds.contains(entry.taskId());
    }

    @Override
    public boolean complete() {
        return false;
    }
}
