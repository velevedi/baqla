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

import com.velevedi.baqla.log.Entry;

import java.util.function.Predicate;

/**
 * Evaluates entries available in the log.
 */
public interface Filter<T> extends Predicate<Entry<T>> {

    /**
     * Log object can hold a lot of entries. When filter completes the search Log scanning stops.
     * This method eliminates the need to scan entire log for the required data. It also allows
     * a filter have it's state that can affect log scanning.
     * @return true when log scanning should be stopped and false otherwise.
     */
    boolean complete();

}
