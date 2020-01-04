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

package com.velevedi.baqla.graph;

import java.util.Objects;

/**
 * Link represents association between a source task and a destination task.
 * The link is directional and operates using task ids (from -> to).
 */
public class Link {
    private String from;
    private String to;
    private int weight = 0;

    public Link() {
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public Link from(String from) {
        this.from = from;
        return this;
    }

    public Link to(String to) {
        this.to = to;
        return this;
    }

    public Link weight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Link weight must be a positive number");
        }
        this.weight = weight;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Double.compare(link.weight, weight) == 0 &&
                from.equals(link.from) &&
                to.equals(link.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight);
    }
}
