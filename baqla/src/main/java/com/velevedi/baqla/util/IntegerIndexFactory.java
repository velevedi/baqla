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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates Integer indices. The first value this factory will provide is 0.
 */
public class IntegerIndexFactory implements IndexFactory<Integer> {

    private final AtomicInteger generator = new AtomicInteger(0);

    @Override
    public Integer nextIndex() {
        return generator.getAndIncrement();
    }
}
