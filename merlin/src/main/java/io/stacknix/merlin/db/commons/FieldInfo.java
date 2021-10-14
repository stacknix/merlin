package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

public class FieldInfo {


    @NotNull
    private final String name;
    @NotNull
    private final Class<?> type;
    private final boolean is_internal;

    public FieldInfo(@NotNull String name, @NotNull Class<?> type, boolean is_internal) {
        this.name = name;
        this.type = type;
        this.is_internal = is_internal;
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

    public boolean isInternal(){
        return is_internal;
    }
}
