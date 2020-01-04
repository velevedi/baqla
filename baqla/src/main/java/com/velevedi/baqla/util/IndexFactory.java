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

package com.velevedi.baqla.util;

/**
 * Each entry has index associated with it. Entries are being added to Log sequentially.
 * The index is a way to let the Log know what is the latest entry added.
 * Classes implementing this interface generate indices for Log entries.
 *
 * @param <I> type of the index value
 * @see com.velevedi.baqla.Log
 * @see com.velevedi.baqla.Log.Entry
 */
public interface IndexFactory<I extends Comparable<? super I>> {

    /**
     * Generates next index value.The actual value is not important.
     * All what matters is the order.
     * The next call should provide "bigger value" according to Comparator of the index.
     *
     * @return next index value
     */
    I nextIndex();

}
