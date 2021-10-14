package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

public class RecordNotFound extends RuntimeException {

    public int code;
    @NotNull
    public String message;

    public RecordNotFound() {
        super("RecordNotFound");
        this.message = "RecordNotFound";
    }

    public RecordNotFound(@NotNull String message) {
        this.message = message;
    }

    public RecordNotFound(int code, @NotNull String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
