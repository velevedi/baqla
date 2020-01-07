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

package com.velevedi.baqla.annotation;

import com.velevedi.baqla.AbstractTask;
import com.velevedi.baqla.Log;
import com.velevedi.baqla.Task;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AnnotationsUsageTest {

    @Test
    void targetTypeUse() {
        Task<Integer> task = new @TaskType(id = "anonymousClass") AbstractTask<>() {
            @Override
            public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
                return 1;
            }
        };

        assertThat(task.id(), is("anonymousClass"));
        assertThat(task.callOn(Collections.emptySet()), is(1));
    }


    @Test
    void targetConstructor() {
        Task<Integer> task = new Two();

        assertThat(task.id(), is("two"));
        assertThat(task.callOn(Collections.emptySet()), is(2));
    }

    static class Two extends AbstractTask<Integer> {

        @TaskType(id = "two")
        public Two() {}

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            return 2;
        }
    }



    @Test
    void targetType() {

        Constant<Integer> constant = new Constant<>(1);

        assertThat(constant.id(), is("constant"));
        assertThat(constant.callOn(Collections.emptySet()), is(1));

        One one = new One();

        assertThat(one.id(), is("one"));
        assertThat(one.callOn(Collections.emptySet()), is(1));
    }

    @TaskType(id = "constant")
    static class Constant<V> extends AbstractTask<V> {

        private final V value;

        public Constant(V value) {
            this.value = value;
        }

        @Override
        public <I extends Comparable<? super I>> V callOn(Set<Log.Entry<I, V>> entries) {
            return value;
        }
    }

    @TaskType(id = "one")
    static class One extends AbstractTask<Integer> {

        @Override
        public <I extends Comparable<? super I>> Integer callOn(Set<Log.Entry<I, Integer>> entries) {
            return 1;
        }
    }


}
