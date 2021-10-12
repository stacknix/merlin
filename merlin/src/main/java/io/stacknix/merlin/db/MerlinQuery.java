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
    public MerlinQuery<T> isNull(String key) {
        super.isNull(key);
        return this;
    }

    @Override
    public MerlinQuery<T> isNotNull(String key) {
        super.isNotNull(key);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, @NotNull String val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, @NotNull String val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public void equal(String key, short val) {
        super.equal(key, val);
    }

    @Override
    public void notEqual(String key, short val) {
        super.notEqual(key, val);
    }

    @Override
    public void equal(String key, int val) {
        super.equal(key, val);
    }

    @Override
    public MerlinQuery<T> notEqual(String key, int val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public void equal(String key, long val) {
        super.equal(key, val);
    }

    @Override
    public void notEqual(String key, long val) {
        super.notEqual(key, val);
    }

    @Override
    public void equal(String key, float val) {
        super.equal(key, val);
    }

    @Override
    public void notEqual(String key, float val) {
        super.notEqual(key, val);
    }

    @Override
    public void equal(String key, double val) {
        super.equal(key, val);
    }

    @Override
    public void notEqual(String key, double val) {
        super.notEqual(key, val);
    }

    @Override
    public void equal(String key, boolean val) {
        super.equal(key, val);
    }

    @Override
    public void notEqual(String key, boolean val) {
        super.notEqual(key, val);
    }

    @Override
    public void gt(String key, @NotNull String val) {
        super.gt(key, val);
    }

    @Override
    public void gt(String key, short val) {
        super.gt(key, val);
    }

    @Override
    public void gt(String key, int val) {
        super.gt(key, val);
    }

    @Override
    public void gt(String key, long val) {
        super.gt(key, val);
    }

    @Override
    public void gt(String key, float val) {
        super.gt(key, val);
    }

    @Override
    public void gt(String key, double val) {
        super.gt(key, val);
    }

    @Override
    public void gte(String key, @NotNull String val) {
        super.gte(key, val);
    }

    @Override
    public void gte(String key, short val) {
        super.gte(key, val);
    }

    @Override
    public void gte(String key, int val) {
        super.gte(key, val);
    }

    @Override
    public void gte(String key, long val) {
        super.gte(key, val);
    }

    @Override
    public void gte(String key, float val) {
        super.gte(key, val);
    }

    @Override
    public void gte(String key, double val) {
        super.gte(key, val);
    }

    @Override
    public void lt(String key, @NotNull String val) {
        super.lt(key, val);
    }

    @Override
    public void lt(String key, short val) {
        super.lt(key, val);
    }

    @Override
    public void lt(String key, int val) {
        super.lt(key, val);
    }

    @Override
    public void lt(String key, long val) {
        super.lt(key, val);
    }

    @Override
    public void lt(String key, float val) {
        super.lt(key, val);
    }

    @Override
    public void lt(String key, double val) {
        super.lt(key, val);
    }

    @Override
    public void lte(String key, @NotNull String val) {
        super.lte(key, val);
    }

    @Override
    public void lte(String key, short val) {
        super.lte(key, val);
    }

    @Override
    public void lte(String key, int val) {
        super.lte(key, val);
    }

    @Override
    public void lte(String key, long val) {
        super.lte(key, val);
    }

    @Override
    public void lte(String key, float val) {
        super.lte(key, val);
    }

    @Override
    public void lte(String key, double val) {
        super.lte(key, val);
    }

    @Override
    public void in(String key, @NotNull Short @NotNull [] val) {
        super.in(key, val);
    }

    @Override
    public void in(String key, @NotNull Integer @NotNull [] val) {
         super.in(key, val);
    }

    @Override
    public MerlinQuery<T> in(String key, @NotNull Long @NotNull [] val) {
         super.in(key, val);
         return this;
    }

    @Override
    public void in(String key, @NotNull Float @NotNull [] val) {
        super.in(key, val);
    }

    @Override
    public void in(String key, @NotNull Double @NotNull [] val) {
        super.in(key, val);
    }

    @Override
    public void like(String key, @NotNull String val) {
        super.like(key, val);
    }

    @Override
    public void ilike(String key, @NotNull String val) {
        super.ilike(key, val);
    }

    @Override
    public void startsWith(String key, @NotNull String val) {
        super.startsWith(key, val);
    }

    @Override
    public void iStartsWith(String key, @NotNull String val) {
        super.iStartsWith(key, val);
    }

    @Override
    public void endsWith(String key, @NotNull String val) {
        super.endsWith(key, val);
    }

    @Override
    public void iEndsWith(String key, @NotNull String val) {
        super.iEndsWith(key, val);
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
