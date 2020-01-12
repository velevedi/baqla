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

import java.io.Closeable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.velevedi.baqla.Log.Entry;

/**
 * Data structure that allows adding new values only without ability to delete entries.
 * A Log can be forked into another Log. Data from the original Log will be copied into a new one.
 * This is the way how data evolution is implemented.
 * Log interface implementation could use any storage type. To cleanup the state Log implements Closable.
 *
 * @param <I> type of the index of an entry
 * @param <V> type of a value of an entry
 * @see Task
 * @see java.util.Spliterator
 * @see java.lang.Comparable
 */
public interface Log<I extends Comparable<? super I>, V> extends Iterable<Entry<I, V>>, Closeable {

    /**
     * Each Log has id which uniquely identifies it.
     *
     * @return id of a Log
     */
    UUID id();

    /**
     * Once forked new Log has a link to the source Log
     *
     * @return null if this Log is newly created and a reference to the parent Log if it was forked.
     */
    UUID parent();

    /**
     * Forks current Log into a new one. Data from the parent Log will be copied into a child Log.
     *
     * @return new instance of the Log
     */
    default Log<I, V> fork() {
        return fork(entry -> true);
    }

    /**
     * Forks current Log into a new one. Data from the parent Log will be copied into a child Log.
     *
     * @param filter filter that will be applied before copying entries to the new Log
     * @return new instance of the Log
     */
    Log<I, V> fork(Predicate<Entry<I, V>> filter);

    /**
     * Number of entries in the Log
     *
     * @return number of entries
     */
    int size();

    /**
     * Returns true if this Log does not have any entries and false otherwise
     *
     * @return true if this Log does not have entries and false otherwise
     */
    boolean isEmpty();

    /**
     * Returns an instance of the iterator to go through entries in the Log.
     *
     * @return an iterator over Log entries
     */
    Iterator<Entry<I, V>> iterator();

    void add(String source, V value);

    int hashCode();

    boolean equals(Object o);

    /**
     * Creates Spliterator over the entries in the Log
     *
     * @return new instance of Spliterator
     */
    Spliterator<Entry<I, V>> spliterator();

    default Stream<Entry<I, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<Entry<I, V>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    /**
     * Entry is a key value pair that keeps data in the Log. Key is an index of the entry and value is a payload.
     * Multiple entries can be added to a Lg from different sources at different time. Index of the entry identifies
     * the order of insertion of the entries. This is why index data type must implement Comparable.
     *
     * @param <I> data type of an index value. In must implement Comparable interface
     * @param <V> value of a payload
     */
    interface Entry<I extends Comparable<? super I>, V> extends Comparable<Entry<I, V>> {

        /**
         * An entry has a source field which identifies an entity which generated the value of this entry.
         *
         * @return source which calculate the value for this entry
         */
        String source();

        /**
         * Index is being used to define the order of the Entry generation.
         *
         * @return index of the entry
         */
        I index();

        /**
         * The payload of the entry. The actual value that is being stored in a Log.
         *
         * @return payload of the entry.
         */
        V value();

        int hashCode();

        boolean equals(Object o);

        @Override
        default int compareTo(Entry<I, V> e) {
            return index().compareTo(e.index());
        }
    }


}
