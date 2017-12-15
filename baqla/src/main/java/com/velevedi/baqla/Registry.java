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

/**
 * Builds Flow object based on the configuration provided.
 * Registry has access to all functions available for the Flow running.
 */
public interface Registry {

    /**
     * Registers service with a given name
     * @param name service name
     * @param service service instance
     * @return reference to registry to chain methods.
     */
    Registry register(String name, Service service);

    /**
     * Builds Flow object using configuration provided
     * @param configuration configuration to resolve
     * @return process ready for running
     */
    Flow resolve(byte[] configuration);

}
