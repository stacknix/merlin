package io.stacknix.merlin.db;


import org.jetbrains.annotations.NotNull;

import io.stacknix.merlin.db.queries.Filter;

public class MerlinQuery<T extends MerlinObject> extends Filter{

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

    @Override
    public MerlinQuery<T> equal(String key, int val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, String val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, String val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, int val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> in(String key, @NotNull Long @NotNull [] val) {
        super.in(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIn(String key, @NotNull Long @NotNull [] val) {
        super.notIn(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> and() {
        super.and();
        return this;
    }

    @Override
    public MerlinQuery<T> or() {
        super.or();
        return this;
    }

    @Override
    public MerlinQuery<T> not() {
        super.not();
        return this;
    }

    @Override
    public MerlinQuery<T> beginGroup() {
        super.beginGroup();
        return this;
    }

    @Override
    public MerlinQuery<T> endGroup() {
        super.endGroup();
        return this;
    }
}
