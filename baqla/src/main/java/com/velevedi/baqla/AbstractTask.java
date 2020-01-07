/*
 * Copyright (C) 2017. The Baqla Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. The License can be obtained at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.velevedi.baqla;

import com.velevedi.baqla.annotation.TaskType;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * Abstract Task which has basic methods implemented
 *
 * @param <V> data type of the Task calculation result
 */
public abstract class AbstractTask<V> implements Task<V> {

    private final String id;

    public AbstractTask() {
        Class<?> clazz = this.getClass();

        String localId = extractIdFromClass(clazz);

        localId = localId == null ? extractIdFromSuperClass(clazz) : localId;

        localId = localId == null ? extractIdFromClassConstructor(clazz) : localId;

        if (localId != null) {
            this.id = localId;
        } else {
            throw new IllegalStateException("Unable to initialize task [" + clazz.getName() + "]. Task id must be provided.");
        }
    }

    private String extractIdFromClass(Class<?> clazz) {
        TaskType taskTypeAnnotation = clazz.getAnnotation(TaskType.class);
        if (taskTypeAnnotation != null) {
            return taskTypeAnnotation.id();
        }
        return null;
    }

    private String extractIdFromSuperClass(Class<?> clazz) {
        AnnotatedType annotatedSuperclass = clazz.getAnnotatedSuperclass();
        if (annotatedSuperclass != null) {
            TaskType taskTypeAnnotation = annotatedSuperclass.getAnnotation(TaskType.class);
            if (taskTypeAnnotation != null) {
                return taskTypeAnnotation.id();
            }
        }
        return null;
    }

    private String extractIdFromClassConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            TaskType taskTypeAnnotation = constructor.getAnnotation(TaskType.class);
            if (taskTypeAnnotation != null) {
                return taskTypeAnnotation.id();
            }
        }
        return null;
    }

    public AbstractTask(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task id can not be null or empty");
        }
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return "Task [" + id + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTask<?> that = (AbstractTask<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
