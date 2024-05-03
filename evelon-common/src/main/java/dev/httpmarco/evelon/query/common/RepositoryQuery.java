package dev.httpmarco.evelon.query.common;

import dev.httpmarco.evelon.Repository;
import dev.httpmarco.evelon.query.Query;
import dev.httpmarco.evelon.query.QueryFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class RepositoryQuery<T> implements Query<T> {

    private final Repository<T> repository;

    @Override
    public void create(T value) {
        for (var layer : repository.layers()) {
            layer.query(repository).create(value);
        }
    }

    @Override
    public void update(T value) {
        for (var layer : repository.layers()) {
            layer.query(repository).update(value);
        }
    }

    @Override
    public void delete() {
        for (var layer : repository.layers()) {
            layer.query(repository).delete();
        }
    }

    @Override
    public boolean exists() {
        for (var layer : repository.layers()) {
            if(layer.query(repository).exists()){
                return true;
            }
        }
        return false;
    }

    @Override
    public T findFirst() {
        for (var layer : repository.layers()) {
            var value = layer.query(repository).findFirst();
            if(value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public QueryFilter<T> filter() {
        return new RepositoryFilter<>(this.repository, repository.layers());
    }
}