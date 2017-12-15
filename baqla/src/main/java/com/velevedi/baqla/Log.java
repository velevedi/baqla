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

import java.io.Closeable;
import java.util.List;
import java.util.UUID;

/**
 * Collects data for the running process. This interface is an abstraction of a data store.
 * Log data structure allows data insertions only. This interface allows multiple implementations depending
 * on a persistence requirements. It is up to the implementor to handle serialization and atomicity.
 */
public interface Log extends Closeable, AutoCloseable {

    /**
     * Id of the Log
     * @return id of the Log
     */
    UUID id();

    /**
     * Each Log can be forked to a new one with copying the data. This method will return the id of the
     * parent Log object where the data has been copied from.
     * @return id of the parent Log or null if the Log has not been forked previously.
     */
    UUID parent();

    /**
     * Each task calculation result ends up in the Log. Each result is wrapped using the Entry object.
     * Each Entry object has id assigned to it. This id is unique within the Log.
     * @return next id to use for an entry
     * @see Entry
     */
    long nextEntryId();

    /**
     * Adds entry to the Log.
     * @param entry entry to add
     * @return true if operation was successful and false otherwise
     */
    boolean add(Entry entry);

    /**
     * Finds entries based on the filter provided.
     * @param filter filter to search for required entries
     * @return list of entries
     */
    List<Entry> find(Filter filter);

    /**
     * Copies entries from this Log into another Log based on the filter provided.
     * @param toLog Log to copy data to
     * @param filter filter to search for required entries
     */
    void copyTo(Log toLog, Filter filter);

    /**
     * Log can be forked to a new one. Existing data will be copied to the new Log.
     * Newly forked Log will be of the same type as the parent one.
     * @return new Log object with data copied form the parent Log object.
     */
    Log fork();

    /**
     * Number of entries in the Log
     * @return number of entries
     */
    int size();

}
