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

import com.velevedi.baqla.*;
import com.velevedi.baqla.simple.filter.LatestValuesForTasksFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Task which calls a service to calculate result.
 */
public class ServiceTask implements Task {

    private final String name;
    private final Set<Task> inputs = new HashSet<>();
    private final Set<Task> outputs = new HashSet<>();
    private final Service service;

    public ServiceTask(String name, Service service) {
        this.name = name;
        this.service = service;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Set<Task> input() {
        return inputs;
    }

    @Override
    public Task withInput(Task input) {
        inputs.add(input);
        return this;
    }

    @Override
    public Set<Task> output() {
        return outputs;
    }

    @Override
    public Task withOutput(Task output) {
        outputs.add(output);
        return this;
    }

    @Override
    public boolean isSource() {
        return inputs.isEmpty();
    }

    @Override
    public boolean isDestination() {
        return outputs.isEmpty();
    }

    @Override
    public void callOn(Log log) {
        if (service != null) {
            Set<String> inputIds = inputs.stream().map(Task::name).collect(toSet());
            List<Entry> entries = log.find(new LatestValuesForTasksFilter(inputIds));

            if (inputIds.size() == entries.size()) {

                Map<String, Object> arguments = entries.stream().collect(
                        toMap(Entry::taskId, Entry::payload)
                );

                Result result = service.apply(new RuntimeContext(arguments));

                if (result == null) {
                    return; // stop calculation if a task returns null
                }

                log.add(
                        LogEntry.newBuilder().
                                id(log.nextEntryId()).
                                meta(result.meta()).
                                taskId(name).
                                payload(result.payload()).
                                inputIds(inputIds).
                                build()
                );
            } else {
                return; // stop calculation if some arguments are not available
            }
        }
        for (Task task : outputs) {
            task.callOn(log);
        }
    }
}
