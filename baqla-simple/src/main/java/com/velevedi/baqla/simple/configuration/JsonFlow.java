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
 * Json flow configuration. Value object to handle json serialization.
 */
public class JsonFlow {

    private String id;
    private String version;
    private String documentation = "";
    private Set<JsonTask> tasks = new HashSet<>();

    public JsonFlow() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<JsonTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<JsonTask> tasks) {
        this.tasks = tasks;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonFlow jsonFlow = (JsonFlow) o;

        return id.equals(jsonFlow.id)
                && version.equals(jsonFlow.version)
                && documentation.equals(jsonFlow.documentation)
                && tasks.equals(jsonFlow.tasks);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + documentation.hashCode();
        result = 31 * result + tasks.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JsonFlow{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", documentation='" + documentation + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
