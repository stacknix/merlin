package io.stacknix.merlin.db;

import io.stacknix.merlin.db.queries.Filter;

public class MerlinQuery<T extends MerlinObject> extends Filter {

    private final Class<T> tClass;

    public MerlinQuery(Class<T> tClass) {
        this.tClass = tClass;
    }

    public Class<T> getObjectClass(){
        return tClass;
    }

    public MerlinResult<T> find(){
        return Merlin.getInstance().db().search(this);
    }

}
