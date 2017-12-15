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

import java.util.Map;

/**
 * Context of a service execution. All data a service needs will be supplied by this object before invoking it.
 */
public interface Context {

    /**
     * Runtime environment provides read only settings.
     * @return read only map with environment settings.
     */
    Map<String, Object> environment();

    /**
     * Task can have many input values. These values are mapped to the arguments with the key holding the id of the
     * input task and the value holding the result of the calculation of that task.
     * @return read only arguments used by a function
     */
    Map<String, Object> arguments();

    /**
     * Finds a value for the given name. Lookup process searches arguments first and then environment variables
     * to get the data.
     * @param name name of the variable to lookup
     * @return object representing value or null if value has not been found
     */
    Object lookup(String name);

}
