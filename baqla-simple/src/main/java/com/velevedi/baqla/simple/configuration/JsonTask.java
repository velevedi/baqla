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

package com.velevedi.baqla.simple.configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Json task configuration. Value object to handle json serialization.
 */
public class JsonTask {

    private String task;
    private String serviceName;
    private Set<String> input = new HashSet<>();
    private Set<String> output = new HashSet<>();

    public JsonTask() {
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Set<String> getInput() {
        return input;
    }

    public void setInput(Set<String> input) {
        this.input = input;
    }

    public Set<String> getOutput() {
        return output;
    }

    public void setOutput(Set<String> output) {
        this.output = output;
    }

    public JsonTask withInput(String input) {
        this.input.add(input);
        return this;
    }

    public JsonTask withOutput(String output) {
        this.output.add(output);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonTask jsonTask = (JsonTask) o;

        return task.equals(jsonTask.task)
                && serviceName.equals(jsonTask.serviceName)
                && input.equals(jsonTask.input)
                && output.equals(jsonTask.output);
    }

    @Override
    public int hashCode() {
        int result = task.hashCode();
        result = 31 * result + serviceName.hashCode();
        result = 31 * result + input.hashCode();
        result = 31 * result + output.hashCode();
        return result;
    }
}
