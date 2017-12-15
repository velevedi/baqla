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

package com.velevedi.baqla;

import java.util.Set;

/**
 * Flow defines what needs be running on incoming data. It is a representation of a program that should run
 * on incoming data. Logically Flow is a Directed Acyclic Graph (DAG) of tasks to run. Tasks have input and
 * output values. Each task can provide a single result that will be stored in the Log object and will be used
 * by upstream tasks to calculate the final result.
 * It is possible to run multiple versions of the Flow running on the same data which can produce different results.
 * Sources define the places where we load data into a calculation engine and destinations are the places where
 * we can obtain results. There is an obvious outcome of such model. It is possible to have multiple results
 * calculated by the same Flow.
 */
public interface Flow {

    /**
     * Name of the process. This name is set up by a user and is being used to locate all running processes.
     * @return human readable name of the process
     */
    String name();

    /**
     * Version of the process. Over the time a process can be changed. This field reflects the fact that the same
     * process can go through the changing process.
     * @return version of the process
     */
    String version();

    /**
     * Flow can have multiple source tasks which can be interlinked and assemble complex calculation networks.
     * This method adds a source to the process definition
     * @param task source task to add
     * @return reference to this object to be able to chain several calls
     */
    Flow withTask(Task task);

    /**
     * Source tasks are the tasks that do not require any previous calculations.
     * @return source tasks
     */
    Set<Task> sources();

    /**
     * Destinations are the tasks where the expected results could be taken from. These tasks do not have any further tasks configured.
     * @return all nodes where the results can be taken from
     */
    Set<Task> destinations();

    /**
     * Returns a set of all available tasks within the flow.
     * @return all available tasks
     */
    Set<Task> all();

    /**
     * If some data is not available during execution Flow execution stops. This method finds all Tasks where the
     * calculation has been stopped. This method is useful when calculation reinitiation is required.
     * @param log  Log available after last execution
     * @return set of tasks where calculation can be restarted
     */
    Set<Task> readyToRunOn(Log log);

}
