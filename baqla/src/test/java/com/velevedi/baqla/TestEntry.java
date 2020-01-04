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

package com.velevedi.baqla;

import com.velevedi.baqla.Log;

public class TestEntry implements Log.Entry<Integer, String> {

    private String source;
    private Integer index;
    private String value;

    public TestEntry(String source, Integer index, String value) {
        this.source = source;
        this.index = index;
        this.value = value;
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public Integer index() {
        return index;
    }

    @Override
    public String value() {
        return value;
    }
}
