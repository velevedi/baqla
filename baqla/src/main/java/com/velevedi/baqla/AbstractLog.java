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

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract Log which has basic methods implemented.
 *
 * @param <I> type of the index of an entry
 * @param <V> type of a value of an entry
 */
public abstract class AbstractLog<I extends Comparable<? super I>, V> implements Log<I, V> {

    protected UUID id;
    protected UUID parent;

    public AbstractLog(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        this.id = id;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public UUID parent() {
        return parent;
    }

}
