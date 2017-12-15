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

import com.velevedi.baqla.Context;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

/**
 * Provides context for a running service.
 */
public class RuntimeContext implements Context {

    private static final Map<String, Object> environment;

    static {
        Map<String, Object> env = new HashMap<>();
        env.putAll(System.getenv());
        env.putAll(
                System.getProperties().entrySet().stream().collect(
                        toMap(
                                entry -> entry.getKey().toString(),
                                entry -> entry.getValue()
                        )
                )
        );
        environment = unmodifiableMap(env);
    }

    private final Map<String, Object> arguments;

    public RuntimeContext(Map<String, Object> arguments) {
        this.arguments = unmodifiableMap(arguments);
    }

    @Override
    public Map<String, Object> environment() {
        return environment;
    }

    @Override
    public Map<String, Object> arguments() {
        return arguments;
    }

    @Override
    public Object lookup(String name) {
        Object result = arguments.get(name);
        if (result != null) {
            return result;
        }
        return environment.get(name);
    }
    
}
