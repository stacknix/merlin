package io.stacknix.merlin.db;


import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.stacknix.merlin.db.annotations.Order;
import io.stacknix.merlin.db.annotations.SortKey;
import io.stacknix.merlin.db.commons.Pair;
import io.stacknix.merlin.db.queries.Filter;

public class MerlinQuery<T extends MerlinObject> extends Filter {

    private final Class<T> tClass;
    private Pair<String, Order> sorting;
    private int limit;

    public MerlinQuery(Class<T> tClass) {
        this.tClass = tClass;
        this.sorting = getDefaultSorting(tClass);
        this.limit = -1;
    }

    public static <T extends MerlinObject> @NotNull Pair<String, Order> getDefaultSorting(Class<T> tClass) {
        return new Pair<>(MerlinObject.getPrimaryKey(tClass), Order.DESC);
    }

    public Class<T> getObjectClass() {
        return tClass;
    }

    public MerlinResult<T> find() {
        return Merlin.getInstance().db().search(this);
    }

    public void observe(@NotNull ResultChangeListener<T> resultChangeListener) {
        MerlinResult<T> result = find();
        resultChangeListener.onChange(result);
        new Handler(Looper.getMainLooper()).post(() -> result.observe(resultChangeListener));
    }

    public MerlinQuery<T> limit(int count) {
        this.limit = count;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public T first() {
        MerlinResult<T> result = find();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public T last() {
        MerlinResult<T> result = find();
        if (!result.isEmpty()) {
            return result.get(result.size() - 1);
        }
        return null;
    }

    public MerlinQuery<T> sort(String key, Order order) {
        this.sorting = new Pair<>(key, order);
        return this;
    }

    public @NotNull Pair<String, Order> getSorting() {
        return sorting;
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
    public MerlinQuery<T> equal(String key, short val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, short val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, int val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, int val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, long val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, long val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, float val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, float val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, double val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, double val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> equal(String key, boolean val) {
        super.equal(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEqual(String key, boolean val) {
        super.notEqual(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, @NotNull String val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, short val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, int val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, long val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, float val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gt(String key, double val) {
        super.gt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, @NotNull String val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, short val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, int val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, long val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, float val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> gte(String key, double val) {
        super.gte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, @NotNull String val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, short val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, int val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, long val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, float val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lt(String key, double val) {
        super.lt(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, @NotNull String val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, short val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, int val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, long val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, float val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> lte(String key, double val) {
        super.lte(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> in(String key, @NotNull Short @NotNull [] val) {
        super.in(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIn(String key, @NotNull Short @NotNull [] val) {
        super.notIn(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> in(String key, @NotNull Integer @NotNull [] val) {
        super.in(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIn(String key, @NotNull Integer @NotNull [] val) {
        super.notIn(key, val);
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
    public MerlinQuery<T> in(String key, @NotNull Float @NotNull [] val) {
        super.in(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIn(String key, @NotNull Float @NotNull [] val) {
        super.notIn(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> in(String key, @NotNull Double @NotNull [] val) {
        super.in(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIn(String key, @NotNull Double @NotNull [] val) {
        super.notIn(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> like(String key, @NotNull String val) {
        super.like(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notLike(String key, @NotNull String val) {
        super.notLike(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> ilike(String key, @NotNull String val) {
        super.ilike(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIlike(String key, @NotNull String val) {
        super.notIlike(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> startsWith(String key, @NotNull String val) {
        super.startsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notStartsWith(String key, @NotNull String val) {
        super.notStartsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> iStartsWith(String key, @NotNull String val) {
        super.iStartsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIStartsWith(String key, @NotNull String val) {
        super.notIStartsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> endsWith(String key, @NotNull String val) {
        super.endsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notEndsWith(String key, @NotNull String val) {
        super.notEndsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> iEndsWith(String key, @NotNull String val) {
        super.iEndsWith(key, val);
        return this;
    }

    @Override
    public MerlinQuery<T> notIEndsWith(String key, @NotNull String val) {
        super.notIEndsWith(key, val);
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
