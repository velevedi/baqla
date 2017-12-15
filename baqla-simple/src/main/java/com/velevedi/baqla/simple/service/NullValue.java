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

/**
 * If a service returns null value calculation stops. This service returns null on purpose for testing reasons.
 */
public class NullValue implements Service {

    @Override
    public Result apply(Context context) {
        return null;
    }
}
