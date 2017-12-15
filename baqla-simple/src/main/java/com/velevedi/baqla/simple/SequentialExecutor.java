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

import com.velevedi.baqla.Executor;
import com.velevedi.baqla.Flow;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Executor implementation that uses single thread to perform calculation.
 */
public class SequentialExecutor implements Executor {

    private final ExecutorService service;

    public SequentialExecutor() {
        this.service = Executors.newSingleThreadExecutor();
    }

    @Override
    public Future submit(Flow flow, Log log) {
        return service.submit(() -> {
            for (Task task : flow.sources()) {
                task.callOn(log);
            }
        });
    }

    @Override
    public Future resubmit(Flow flow, Log log) {
        return service.submit(() -> {
            for (Task task : flow.readyToRunOn(log)) {
                task.callOn(log);
            }
        });
    }
}
