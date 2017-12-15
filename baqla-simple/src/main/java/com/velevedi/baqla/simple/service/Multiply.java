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
 * Multiplies numbers. Throws {@link IllegalArgumentException} if supplied arguments do not represent numbers.
 */
public class Multiply implements Service {

    @Override
    public Result apply(Context context) {
        Map<String, Object> arguments = context.arguments();

        if (arguments.size() == 0) {
            return new CalculationResult(0D, emptyMap());
        }

        double result = 1D;
        for (Map.Entry<String, Object> entry : arguments.entrySet()) {

            Object value = entry.getValue();

            if (value instanceof Number) {
                result *= ((Number) value).doubleValue();
            } else {
                throw new IllegalArgumentException("Invalid arguments have been provided");
            }
        }

        return new CalculationResult(result, emptyMap());
    }
}
