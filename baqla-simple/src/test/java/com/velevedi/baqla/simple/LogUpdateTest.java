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
import com.velevedi.baqla.simple.filter.TaskIdFilter;
import com.velevedi.baqla.simple.service.NullValue;
import com.velevedi.baqla.simple.service.Sum;
import com.velevedi.baqla.simple.util.ClassloaderResourceLocator;
import org.junit.Test;

import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import static com.velevedi.baqla.simple.util.NamingUtils.tasksToNames;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class LogUpdateTest {

    @Test
    public void reinitializeCalculation() throws Exception {

        Registry registry = new PojoRegistry().
                register("a", new NullValue()).
                register("b", new NullValue()).
                register("sum", new Sum());

        Flow flow = registry.resolve(
                Files.readAllBytes(new ClassloaderResourceLocator().locate("add-formula-flow.json"))
        );

        Executor executor = new SequentialExecutor();

        Log log = new ArrayLog(1000);

        executor.submit(flow, log).get();

        Set<Task> tasks = flow.readyToRunOn(log);

        assertThat(tasksToNames(tasks), hasItems("a", "b"));

        log.add(
                LogEntry.newBuilder().
                        id(log.nextEntryId()).
                        meta(emptyMap()).
                        taskId("a").
                        payload(1L).
                        inputIds(emptySet()).
                        build()
        );

        tasks = flow.readyToRunOn(log);

        assertThat(tasksToNames(tasks), hasItems("b"));
        assertThat(tasksToNames(tasks), not(hasItems("a")));

        log.add(
                LogEntry.newBuilder().
                        id(log.nextEntryId()).
                        meta(emptyMap()).
                        taskId("b").
                        payload(1L).
                        inputIds(emptySet()).
                        build()
        );

        tasks = flow.readyToRunOn(log);

        assertThat(tasksToNames(tasks), hasItems("sum"));
        assertThat(tasksToNames(tasks), not(hasItems("a", "b")));

        executor.resubmit(flow, log).get();

        List<Entry> results = log.find(new TaskIdFilter(
                flow.destinations().stream().
                        map(Task::name).
                        collect(toSet())
        ));

        assertThat(results.size(), is(1));
        assertThat(results.get(0).payload(), is(2.0));
    }

}
