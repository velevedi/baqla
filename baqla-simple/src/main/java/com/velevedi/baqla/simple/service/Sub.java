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

package com.velevedi.baqla.simple.service;

import com.velevedi.baqla.Context;
import com.velevedi.baqla.Result;
import com.velevedi.baqla.Service;
import com.velevedi.baqla.simple.CalculationResult;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * Sums up numbers. Throws {@link IllegalArgumentException} if supplied arguments do not represent numbers.
 */
public class Sub implements Service {

    @Override
    public Result apply(Context context) {
        Map<String, Object> arguments = context.arguments();

        if (arguments.size() == 0) {
            return new CalculationResult(0D, emptyMap());
        }

        Object a = context.arguments().get("a");
        Object b = context.arguments().get("b");

        if (a == null || b == null) {
            throw new IllegalArgumentException("Unable to locate required arguments");
        }
        if (a instanceof Number && b instanceof Number) {
            double result = ((Number)a).doubleValue() - ((Number)b).doubleValue();
            return new CalculationResult(result, emptyMap());
        } else {
            throw new IllegalArgumentException("Invalid arguments have been provided");
        }
    }
}
