package io.stacknix.merlin.db.queries;

public class Condition {

    private final String key;
    private final Object val;
    private final String opt;

    public Condition(String key, Object val, String opt) {
        this.key = key;
        this.val = val;
        this.opt = opt;
    }


}
