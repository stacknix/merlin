package io.stacknix.merlin.db.queries;

import org.jetbrains.annotations.NotNull;

public class NullCondition {

    public String key;
    public String opt;

    public NullCondition(String key, String opt) {
        this.key = key;
        this.opt = opt;
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s %s", key, opt);
    }
}
