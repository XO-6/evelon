package dev.httpmarco.evelon.external;

import dev.httpmarco.evelon.RepositoryEntry;
import dev.httpmarco.evelon.RepositoryEntryFinder;
import dev.httpmarco.evelon.RepositoryExternalEntry;
import dev.httpmarco.osgan.reflections.Reflections;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Collection;

@Getter
@Accessors(fluent = true)
public final class RepositoryCollectionEntry extends RepositoryExternalEntry {

    private final RepositoryEntry typeEntry;

    public RepositoryCollectionEntry(String id, @NotNull Field field, RepositoryExternalEntry parent) {
        super(id, field.getType(), parent);

        this.typeEntry = RepositoryEntryFinder.find(Reflections.on(field).generic(0), null, field.getName(), this);

        if (!(typeEntry instanceof RepositoryExternalEntry)) {
             this.children(typeEntry);
        } else {
            this.children().addAll(((RepositoryExternalEntry) typeEntry).children());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> readValues(Object parent) {
        return ((Collection<Object>) parent);
    }
}