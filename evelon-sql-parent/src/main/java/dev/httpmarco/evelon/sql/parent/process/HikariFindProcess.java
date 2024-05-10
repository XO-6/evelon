package dev.httpmarco.evelon.sql.parent.process;

import dev.httpmarco.evelon.Ordering;
import dev.httpmarco.evelon.RepositoryConstant;
import dev.httpmarco.evelon.RepositoryExternalEntry;
import dev.httpmarco.evelon.external.RepositoryCollectionEntry;
import dev.httpmarco.evelon.filtering.Filter;
import dev.httpmarco.evelon.process.kind.QueryProcess;
import dev.httpmarco.evelon.query.QueryConstant;
import dev.httpmarco.evelon.sql.parent.HikariFilter;
import dev.httpmarco.evelon.sql.parent.HikariFilterUtil;
import dev.httpmarco.evelon.sql.parent.reference.HikariProcessReference;
import dev.httpmarco.osgan.reflections.Reflections;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;

public final class HikariFindProcess extends QueryProcess<HikariProcessReference, HikariFilter<Object>> {

    private static final String SELECT_QUERY = "SELECT %s FROM %s";

    @Override
    public @NotNull Object run(@NotNull RepositoryExternalEntry entry, HikariProcessReference reference) {
        var items = new ArrayList<>();
        var searchedItems = new ArrayList<String>();

        for (var child : entry.children()) {
            if (child instanceof RepositoryExternalEntry) {
                continue;
            }
            searchedItems.add(child.id());
        }

        var itemStringList = String.join(", ", searchedItems);
        var query = HikariFilterUtil.appendFiltering(SELECT_QUERY.formatted(itemStringList, entry.id()), filters());

        if (constants().has(QueryConstant.ORDERING)) {
            query = query + " ORDER BY " + constants().constant(QueryConstant.ORDERING);

            if (constants().has(QueryConstant.ORDERING_TYPE)) {
                if (constants().constant(QueryConstant.ORDERING_TYPE) == Ordering.ASCENDING) {
                    query = query + " ASC";
                } else {
                    query = query + " DESC";
                }
            } else {
                query = query + " ASC";
            }
        }

        if (constants().has(QueryConstant.LIMIT)) {
            query = query + " LIMIT " + constants().constant(QueryConstant.LIMIT);
        }

        query = query + ";";

        reference.append(query, filters().stream().map(Filter::value).toArray(), resultSet -> {
            try {
                if (entry instanceof RepositoryCollectionEntry collectionEntry && !(collectionEntry.typeEntry() instanceof RepositoryExternalEntry)) {
                    items.add(resultSet.getObject(collectionEntry.typeEntry().id()));
                    return;
                }

                var reflections = Reflections.on(entry.clazz());

                // we must use the value type of collection entry to create the object
                if (entry instanceof RepositoryCollectionEntry collectionEntry) {
                    reflections = Reflections.on(collectionEntry.typeEntry().clazz());
                }

                var object = reflections.allocate();
                for (var child : entry.children()) {
                    // children need a separate statement
                    if (child instanceof RepositoryExternalEntry externalEntry) {
                        Reflections.on(object).modify(child.constants().constant(RepositoryConstant.PARAM_FIELD), new HikariFindProcess().run(externalEntry, reference));
                        continue;
                    }

                    var value = resultSet.getObject(child.id());
                    // jdbc cannot cast to char ... we handle this manuel
                    if (value instanceof String && child.clazz().equals(char.class)) {
                        value = ((String) value).charAt(0);
                    }

                    if (child.constants().has(RepositoryConstant.VALUE_RENDERING)) {
                        value = child.constants().constant(RepositoryConstant.VALUE_RENDERING).apply(value);
                    }

                    // modify the original field with a new value
                    var childFiled = child.constants().has(RepositoryConstant.PARAM_FIELD) ? child.constants().constant(RepositoryConstant.PARAM_FIELD) : Reflections.on(child.clazz()).field(child.id());
                    Reflections.on(object).modify(childFiled, value);
                }
                items.add(object);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return items;
    }
}
