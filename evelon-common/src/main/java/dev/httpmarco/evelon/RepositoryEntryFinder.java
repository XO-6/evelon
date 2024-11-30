package dev.httpmarco.evelon;

import dev.httpmarco.evelon.common.JavaUtils;
import dev.httpmarco.evelon.external.RepositoryCollectionEntry;
import dev.httpmarco.evelon.external.RepositoryMapEntry;
import dev.httpmarco.evelon.external.RepositoryObjectEntry;
import dev.httpmarco.evelon.transformers.RecordTransformer;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public final class RepositoryEntryFinder {

    @Contract("_, _, _, _ -> new")
    public static @NotNull RepositoryEntry find(Class<?> clazz, Field field, String id, RepositoryExternalEntry parent) {
        var externalId = parent != null ? parent.id() + "_" + id : id;

        if (JavaUtils.JAVA_ELEMENTS.contains(clazz) || clazz.isEnum() || clazz.isPrimitive() || clazz.equals(UUID.class)) {
            return new RepositoryEntry(id, clazz, parent);
        }

        if(Timestamp.class.isAssignableFrom(clazz)) {
            return new RepositoryEntry(id, Timestamp.class, parent);
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return new RepositoryMapEntry(externalId, field, parent);
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return new RepositoryCollectionEntry(externalId, field, parent);
        }

        if (!clazz.isSynthetic()) {
            var entry = new RepositoryObjectEntry(externalId, clazz, parent);

            if (clazz.isRecord()) {
                // we must set a reflection transformer for records
                entry.children().forEach(it -> it.constants().put(RepositoryConstant.TRANSFORMER, RecordTransformer.RECORD_TRANSFORMER));
            }
            return entry;
        }

        throw new IllegalArgumentException("Unsupported entry type: " + clazz);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull RepositoryEntry find(Field field, String id, RepositoryExternalEntry parent) {
        return find(field.getType(), field, id, parent);
    }
}
