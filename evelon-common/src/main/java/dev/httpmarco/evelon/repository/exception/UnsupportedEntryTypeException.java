package dev.httpmarco.evelon.repository.exception;

public final class UnsupportedEntryTypeException extends RuntimeException {

    public UnsupportedEntryTypeException(Class<?> type) {
        super("For type '" + type.getName() + "' no entry selection found.");
    }
}
