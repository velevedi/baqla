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

package com.velevedi.baqla;

import java.util.Set;

/**
 * A task that runs on a log entries and generates result.
 * Each task has id which identifies it.
 *
 * @param <V> a data type returned by a task
 */
public interface Task<V> {

    /**
     * Each task has unique identifier
     *
     * @return identifier of the task
     */
    String id();

    int hashCode();

    boolean equals(Object o);

    /**
     * Task is being called on the data present in a Log to produce result. The result of the calculation
     * will be put back into the Log for further processing.
     *
     * @param entries Log entries to run task on
     * @param <I>     type of the key of the Log entry
     * @return calculation result
     * @see Log
     * @see Log.Entry
     */
    <I extends Comparable<? super I>> V callOn(Set<Log.Entry<I, V>> entries);

}