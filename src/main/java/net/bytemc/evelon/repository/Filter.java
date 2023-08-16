/*
 * Copyright 2019-2023 ByteMC team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bytemc.evelon.repository;

import net.bytemc.evelon.repository.filters.*;

import java.util.stream.Stream;

public interface Filter {

    String sqlFilter(String id);

    <T> Stream<T> localFilter(Stream<T> stream);

    static Filter min(String id, Number min) {
        return new MinimumFilter(id, min);
    }

    static Filter max(String id, Number max) {
        return new MaximumFilter(id, max);
    }

    static Filter between(String id, Number min, Number max) {
        return new BetweenFilter(id, min, max);
    }

    static Filter match(String id, Object object) {
        return new MatchFilter(id, object);
    }

    static Filter noneMatch(String id, Object object) {
        return new NoneMatchFilter(id, object);
    }

}
