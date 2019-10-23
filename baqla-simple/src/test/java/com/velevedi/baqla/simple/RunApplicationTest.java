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
import com.velevedi.baqla.simple.filter.LatestValuesForTasksFilter;
import com.velevedi.baqla.simple.service.Constant;
import com.velevedi.baqla.simple.service.Sub;
import com.velevedi.baqla.simple.service.Sum;
import com.velevedi.baqla.simple.util.ClassloaderResourceLocator;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static java.util.stream.Collectors.toSet;


class RunApplicationTest {

    @Test
    void runApplicationWithArrayLog() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new Constant(2L)).
                register("b", new Constant(3L)).
                register("sum", new Sum());

        Flow flow = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("add-formula-flow.json"))
        );

        Executor executor = new SequentialExecutor();

        Log log = new ArrayLog(1000);

        executor.submit(flow, log).get();

        Set<String> resultTasks = flow.destinations().stream().
                map(Task::name).
                collect(toSet());
        List<Entry> results = log.find(new LatestValuesForTasksFilter(resultTasks));

        assertEquals(results.size(), 1);
        assertEquals(results.get(0).payload(), 5.0);
    }

    /**
     * Different Log implementation but result calculated is the same
     */
    @Test
    void runApplicationWithHashMapLog() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new Constant(2L)).
                register("b", new Constant(3L)).
                register("sum", new Sum());

        Flow flow = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("add-formula-flow.json"))
        );

        Executor executor = new SequentialExecutor();

        Log log = new HashMapLog();

        executor.submit(flow, log).get();

        Set<String> resultTasks = flow.destinations().stream().
                map(Task::name).
                collect(toSet());
        List<Entry> results = log.find(new LatestValuesForTasksFilter(resultTasks));

        assertEquals(results.size(), 1);
        assertEquals(results.get(0).payload(), 5.0);
    }

    /**
     * Different Log implementation but result calculated is the same
     */
    @Test
    void runApplicationWithFileLog() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new Constant(2L)).
                register("b", new Constant(3L)).
                register("sum", new Sum());

        Flow flow = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("add-formula-flow.json"))
        );

        Executor executor = new SequentialExecutor();

        try (Log log = new FileLog(System.getProperty("buildDirectory"), "run-app-test")) {

            executor.submit(flow, log).get();

            Set<String> resultTasks = flow.destinations().stream().
                    map(Task::name).
                    collect(toSet());
            List<Entry> results = log.find(new LatestValuesForTasksFilter(resultTasks));

            assertEquals(results.size(), 1);
            assertEquals(results.get(0).payload(), 5.0);
        }
    }

    @Test
    void reusingDataStore() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new Constant(2L)).
                register("b", new Constant(3L)).
                register("sum", new Sum()).
                register("sub", new Sub());

        Flow application1 = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("add-formula-flow.json"))
        );

        Flow application2 = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("sub-formula-flow.json"))
        );

        Executor executor = new SequentialExecutor();

        try (Log log = new FileLog(System.getProperty("buildDirectory"), "run-app-test")) {

            // Running application1
            executor.submit(application1, log).get();

            Set<String> resultTasksApp1 = application1.destinations().stream().
                    map(Task::name).
                    collect(toSet());
            List<Entry> results1 = log.find(new LatestValuesForTasksFilter(resultTasksApp1));

            assertEquals(results1.size(), 1);
            assertEquals(results1.get(0).payload(), 5.0);

            // Running application2
            executor.resubmit(application2, log).get();

            Set<String> resultTasksApp2 = application2.destinations().stream().
                    map(Task::name).
                    collect(toSet());
            List<Entry> results2 = log.find(new LatestValuesForTasksFilter(resultTasksApp2));

            assertEquals(results2.size(), 1);
            assertEquals(results2.get(0).payload(), -1.0);
        }
    }

}
