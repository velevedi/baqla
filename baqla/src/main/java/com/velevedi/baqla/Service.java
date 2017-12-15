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

import java.util.function.Function;

/**
 * Functional interface each service should implement.
 */
public interface Service extends Function<Context, Result> {

    /**
     * Runs service for arguments provided.
     * @param context object that encapsulates environment configuration as well as arguments required
     *                to perform  calculation
     * @return Result object
     */
    @Override
    Result apply(Context context);
    
    
}
