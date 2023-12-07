package net.bytemc.evelon.local;

import net.bytemc.evelon.filters.Filter;
import net.bytemc.evelon.filters.LayerFilterHandler;
import net.bytemc.evelon.local.filters.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class LocalFilterHandler implements LayerFilterHandler<Boolean, Object> {

    @Override
    public Filter<Boolean, Number> min(String id, int min) {
        return new MinimalLocalFilter(id, min);
    }

    @Override
    public Filter<Boolean, Number> max(String id, int max) {
        return new MinimalLocalFilter(id, max);
    }

    @Override
    public Filter<Boolean, Number> between(String id, int min, int max) {
        return new BetweenLocalFilter(id, min, max);
    }

    @Override
    public Filter<Boolean, Object> match(String id, Object value) {
        return new MatchLocalFilter(id, value);
    }

    @Override
    public Filter<Boolean, Object> noneMatch(String id, Object value) {
        return new NoneMatchFilter(id, value);
    }

    @Override
    public Filter<Boolean, String> like(String id, String value) {
        return new LikeLocalFilter(id, value);
    }

    @Override
    public Filter<Boolean, Object> someDate(String id, Date date, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Filter<Boolean, Object> betweenTime(String id, Date date, String date2) {
        return null;
    }

    @Override
    public Filter<Boolean, Object> sameTime(String id, Date date) {
        return null;
    }
}