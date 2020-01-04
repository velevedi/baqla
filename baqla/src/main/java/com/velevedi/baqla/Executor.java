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

/**
 * Executor is an entity that performs calculation of a processing task defined by Graph of interlinked Tasks.
 * Depending on the implementation of the executor all graph tasks could be executed in one go or incrementally.
 *
 * @see Graph
 * @see Task
 */
public interface Executor {

    /**
     * Submits Graph of Tasks to run on Log
     *
     * @param graph graph to run
     * @param log   log to store execution results
     * @param <I>   type of the index of an entry in the Log
     * @param <V>   type of a value of an entry in the log
     */
    <I extends Comparable<? super I>, V> void submit(Graph<V> graph, Log<I, V> log);

}
