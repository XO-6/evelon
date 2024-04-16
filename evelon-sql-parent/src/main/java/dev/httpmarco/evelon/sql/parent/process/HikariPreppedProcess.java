package dev.httpmarco.evelon.sql.parent.process;

import dev.httpmarco.evelon.layer.Layer;
import dev.httpmarco.evelon.process.Process;
import dev.httpmarco.evelon.repository.RepositoryExternalEntry;
import dev.httpmarco.evelon.repository.RepositoryEntry;
import dev.httpmarco.evelon.sql.parent.HikariRepositoryConstant;
import dev.httpmarco.evelon.sql.parent.SqlType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class HikariPreppedProcess extends Process<String, HikariPreppedProcess> {

    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s (%s%s);";

    @Override
    public String run(RepositoryEntry entry, @NotNull Layer<String> layer) {
        if (!(entry instanceof RepositoryExternalEntry objectEntry)) {
            throw new UnsupportedOperationException("Processes can only be run with external entries!");
        }

        var sqlEntries = new ArrayList<String>();
        for (var child : objectEntry.children()) {
            if (child instanceof RepositoryExternalEntry) {
                this.newSubProcess(new HikariPreppedProcess());
                continue;
            }

            var type = SqlType.find(child);
            // on first initialization, we put the sql type into the constants
            child.constants().put(HikariRepositoryConstant.SQL_TYPE, type);
            sqlEntries.add(child.id() + " " + type);
        }

        return CREATE_TABLE_SQL.formatted(entry.id(), String.join(", ", sqlEntries), "");
    }
}