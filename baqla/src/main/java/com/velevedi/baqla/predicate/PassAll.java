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

package com.velevedi.baqla.predicate;

import com.velevedi.baqla.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Predicate that passes all entries.
 *
 * @param <I> type of the index of an entry
 * @param <V> type of a value of an entry
 */
public class PassAll<I extends Comparable<? super I>, V> implements Predicate<Log.Entry<I, V>> {

    @Override
    public boolean test(Log.Entry<I, V> entry) {
        return true;
    }
}
