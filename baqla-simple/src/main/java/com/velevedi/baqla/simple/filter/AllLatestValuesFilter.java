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

import java.util.HashSet;
import java.util.Set;

/**
 * Selects latest values of for all tasks ever stored in the Log.
 */
public class AllLatestValuesFilter implements Filter {

    private final Set<String> found = new HashSet<>();

    public AllLatestValuesFilter() {
    }

    @Override
    public boolean test(Entry entry) {
        if (entry == null || found.contains(entry.taskId())) {
            return false;
        }
        found.add(entry.taskId());
        return true;
    }

    @Override
    public boolean complete() {
        return false;
    }
}
