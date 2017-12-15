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

package com.velevedi.baqla.simple;

import com.google.gson.Gson;
import com.velevedi.baqla.Flow;
import com.velevedi.baqla.Registry;
import com.velevedi.baqla.Service;
import com.velevedi.baqla.Task;
import com.velevedi.baqla.simple.configuration.JsonFlow;
import com.velevedi.baqla.simple.configuration.JsonTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Pojo implementation of the repository. It uses json configuration file for specifying  calculation.
 */
public class PojoRegistry implements Registry {

    private final Map<String, Service> store = new HashMap<>();

    public PojoRegistry() {
    }

    @Override
    public PojoRegistry register(String key, Service service) {
        store.put(key, service);
        return this;
    }

    @Override
    public Flow resolve(byte[] configuration) {

        JsonFlow loaded = new Gson().fromJson(new String(configuration), JsonFlow.class);

        Flow flow = new RunFlow(loaded.getId(), loaded.getVersion());

        Map<String, Task> tasks = new HashMap<>();

        // creating tasks
        for (JsonTask jsonTask : loaded.getTasks()) {

            String id = jsonTask.getTask();

            String serviceName = jsonTask.getServiceName();
            Service service = store.get(serviceName);
            if (service == null) {
                throw new ConfigurationException("Unable to find service [" + serviceName + "]");
            }

            tasks.put(id, new ServiceTask(id, service));
        }

        // linking tasks
        for (JsonTask jsonTask : loaded.getTasks()) {

            String id = jsonTask.getTask();

            Task task = tasks.get(id);

            for (String name : jsonTask.getInput()) {
                task.withInput(tasks.get(name));
            }

            for (String name : jsonTask.getOutput()) {
                task.withOutput(tasks.get(name));
            }
        }

        tasks.forEach((key, value) -> flow.withTask(value));

        return flow;
    }

}
