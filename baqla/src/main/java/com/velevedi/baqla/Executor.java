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

import com.velevedi.baqla.log.Log;

import java.util.concurrent.Future;

/**
 * Executor is an entity that performs execution of the provided Flow
 */
public interface Executor {

    /**
     * Submits flow for execution. This is the place where two sides of the calculation meet.
     * One side is the data which has been abstracted by the Log object.
     * Another side is the application code that will use the data from the Log and perform calculations on it.
     * The code has been abstracted via the Flow object. Flow defines Directed Acyclic Graph (DAG) of services to run.
     * @param flow flow to run
     * @param log  log to run on
     * @return Future object to track when execution has been finished
     */
    Future submit(Flow flow, Log log);

    /**
     * This method inspects elements of the Flow and runs only those that have not been run before.
     * @param flow flow to rerun
     * @param log log to run on
     * @return Future object to track when execution has been finished
     */
    Future resubmit(Flow flow, Log log);
    
}
