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

import com.velevedi.baqla.Entry;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

/**
 * This object represents a result of a function calculation. Once calculated it will never change.
 */
public class LogEntry implements Entry {
    private final long id;
    private final Map<String, Object> meta;
    private final String taskId;
    private final Object payload;
    private final Set<String> inputIds;

    private LogEntry(long id, Map<String, Object> meta, String taskId, Object payload, Set<String> inputIds) {
        this.id = id;
        this.meta = meta;
        this.taskId = taskId;
        this.payload = payload;
        this.inputIds = inputIds;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public Map<String, Object> meta() {
        return meta;
    }

    @Override
    public String taskId() {
        return taskId;
    }

    @Override
    public Object payload() {
        return payload;
    }

    @Override
    public Set<String> inputIds() {
        return inputIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry that = (LogEntry) o;

        return id == that.id
                && meta.equals(that.meta)
                && taskId.equals(that.taskId)
                && payload.equals(that.payload)
                && inputIds.equals(that.inputIds);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + meta.hashCode();
        result = 31 * result + taskId.hashCode();
        result = 31 * result + payload.hashCode();
        result = 31 * result + inputIds.hashCode();
        return result;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Provides a way to build immutable entries.
     */
    public static class Builder {
        private long id;
        private Map<String, Object> meta = new HashMap<>();
        private String taskId;
        private Object payload;
        private Set<String> inputIds = new HashSet<>();

        Builder() {
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder meta(final Map<String, Object> meta) {
            this.meta = meta;
            return this;
        }

        public Builder taskId(final String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder payload(final Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder inputIds(final Set<String> inputIds) {
            this.inputIds = inputIds;
            return this;
        }

        public LogEntry build() {
            if (taskId == null || taskId.isEmpty()) {
                throw new IllegalArgumentException("Task id can not me empty");
            }
            if (payload == null) {
                throw new IllegalArgumentException("Payload can not be empty");
            }
            return new LogEntry(id, unmodifiableMap(meta), taskId, payload, unmodifiableSet(inputIds));
        }

    }

}
