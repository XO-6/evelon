package dev.httpmarco.evelon.demo.models;

import dev.httpmarco.evelon.repository.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class SimpleListModel {

    @PrimaryKey
    private final UUID uuid;
    private final List<String> permissions;
    private final List<String> groups;

}