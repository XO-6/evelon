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

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytemc.evelon.local.LocalStorage;
import net.bytemc.evelon.sql.DatabaseStorage;
import net.bytemc.evelon.Storage;

@Getter
@AllArgsConstructor
public enum RepositoryDepartureOrder {

    CHRONOLOGICAL(null),
    LOCAL(LocalStorage.class),
    DATABASE(DatabaseStorage.class);

    final Class<? extends Storage> storage;

}
