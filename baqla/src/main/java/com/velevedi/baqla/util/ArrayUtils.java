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

import java.util.stream.IntStream;

/**
 * Utility class for managing arrays
 */
public class ArrayUtils {

    /**
     * Checks if an array contains all elements
     *
     * @param array    array to check contents on
     * @param elements elements to check in the array
     * @return true if arrays contains all elements and false otherwise
     */
    public static boolean containsAll(int[] array, int... elements) {
        for (int e : elements) {
            boolean found = false;
            for (int a : array) {
                if (a == e) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Detects the index of a value in array
     *
     * @param value   value to find
     * @param inArray array to search on
     * @return index of a value in array or a negative value if not found
     */
    public static int indexOf(int value, int[] inArray) {
        return IntStream.range(0, inArray.length)
                .filter(i -> value == inArray[i])
                .findFirst()
                .orElse(-1);
    }
}
