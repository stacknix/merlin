package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

public class FieldInfo {


    @NotNull
    private final String name;
    @NotNull
    private final Class<?> type;

    public FieldInfo(@NotNull String name, @NotNull Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Class<?> getType() {
        return type;
    }

    @Override
    public @NotNull String toString() {
        return name;
    }
}
