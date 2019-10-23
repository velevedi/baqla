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

import com.velevedi.baqla.*;
import com.velevedi.baqla.simple.service.Constant;
import com.velevedi.baqla.simple.service.Multiply;
import com.velevedi.baqla.simple.service.Sum;
import com.velevedi.baqla.simple.util.ClassloaderResourceLocator;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import static com.velevedi.baqla.simple.util.NamingUtils.tasksToNames;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

public class RunFlowTest {

    @Test
    public void testFindReadyToRunTasks() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new Constant(2L)).
                register("b", new Constant(3L)).
                register("d", new Constant(4L)).
                register("sum", new Sum()).
                register("mul", new Multiply());

        Flow flow = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("find-ready-to-run-tasks.json"))
        );

        Log log = new ArrayLog(1000);

        log.add(
                LogEntry.newBuilder().
                        id(log.nextEntryId()).
                        meta(emptyMap()).
                        taskId("a").
                        payload(2L).
                        inputIds(emptySet()).
                        build()
        );

        log.add(
                LogEntry.newBuilder().
                        id(log.nextEntryId()).
                        meta(emptyMap()).
                        taskId("b").
                        payload(3L).
                        inputIds(emptySet()).
                        build()
        );

        log.add(
                LogEntry.newBuilder().
                        id(log.nextEntryId()).
                        meta(emptyMap()).
                        taskId("d").
                        payload(4L).
                        inputIds(emptySet()).
                        build()
        );

        Set<Task> tasks = flow.readyToRunOn(log);

        assertTrue(tasksToNames(tasks).contains("c"));
        assertFalse(tasksToNames(tasks).containsAll(List.of("a", "b", "d")));
    }
}