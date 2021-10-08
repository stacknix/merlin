package io.stacknix.merlin.db;

import java.util.ArrayList;

public class MerlinResult<T extends MerlinObject> extends ArrayList<T> {

    private final MerlinQuery<T> query;

    public MerlinResult(MerlinQuery<T> query) {
        this.query = query;
    }
}
