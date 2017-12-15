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
 * Task is a building block of a more complex calculation data structures. It is the smallest element that can provide
 * result. It is a wrapper on top of a running function which hides complexity on wiring complex calculations. Task can have input,
 * output and links to other tasks. It is important to mention that a task does not have any way to store results in it.
 * Instead it delegates this task (storing calculation results) to the Log object.
 * Task execution stops if required dependencies are not available for successful running.
 * Interlinked tasks form DAG (Directed Acyclic Graph).
 */
public interface Task {

    /**
     * Name of the task. This name is being used to associate calculation results in the Log.
     * @return name of the task
     */
    String name();

    /**
     * All inputs required to perform calculation. If all required input data is not available calculation does not
     * progress further.
     * @return input for the calculation
     */
    Set<Task> input();

    /**
     * Tasks in a Flow form a graph. This method adds an input task to the list of tasks required for calculation.
     * @param input input task that will provide its calculation result to this task
     * @return reference to this object to chain method calls
     */
    Task withInput(Task input);

    /**
     * Once calculation is being performed and result is being stored in the Log output tasks will be notified.
     * @return set of output tasks to notify
     */
    Set<Task> output();

    /**
     * Adds output task to the set of the tasks to notify.
     * @param output task to add as output
     * @return reference to this object to chain methods
     */
    Task withOutput(Task output);

    /**
     * A task is a source if it does not have any input tasks to get data from
     * @return true if the task is a source task and false otherwise
     */
    boolean isSource();

    /**
     * Task can be a final point in the calculation. This means that the calculation process has arrived to the
     * destination and there are no any tasks left to perform. Usually the destination tasks are the tasks where the user
     * collects results of the calculation.
     * @return return true if a task is a destination task and false otherwise
     */
    boolean isDestination();

    /**
     * The task is being called on the Log to calculate results. When task is being called it takes dependencies
     * available in the Log to perform calculation. If any of the dependencies are missing calculation stops without
     * storing any data to the Log.
     * Calculation stops in case of any exceptions.
     * @param log log to perform calculation
     */
    void callOn(Log log);

}
