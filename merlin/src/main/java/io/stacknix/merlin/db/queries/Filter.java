package io.stacknix.merlin.db.queries;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class Filter {

    private final List<Object> data;
    private Filter parent;

    public Filter() {
        this.data = new ArrayList<>();
    }

    private Filter(Filter parent) {
        this.data = new ArrayList<>();
        this.parent = parent;
    }

    private void ensureBasic() {
        if (!data.isEmpty()) {
            Object prev = data.get(data.size() - 1);
            if (prev instanceof Condition
                    || prev instanceof NullCondition
                    || prev instanceof List) {
                data.add(Operator.AND);
            }
        }
    }

    public Filter isNull(String key) {
        ensureBasic();
        this.data.add(new NullCondition(key, Substitute.IS_NULL));
        return this;
    }

    public Filter isNotNull(String key) {
        ensureBasic();
        this.data.add(new NullCondition(key, Substitute.IS_NOT_NULL));
        return this;
    }

    public Filter equal(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
        return this;
    }

    public Filter notEqual(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
        return this;
    }

    public void equal(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public void notEqual(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
    }

    public void equal(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public Filter notEqual(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
        return this;
    }

    public void equal(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public void notEqual(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
    }

    public void equal(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public void notEqual(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
    }

    public void equal(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public void notEqual(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
    }

    public void equal(String key, boolean val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
    }

    public void notEqual(String key, boolean val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
    }

    public void gt(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gt(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gt(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gt(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gt(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gt(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN, val));
    }

    public void gte(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void gte(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void gte(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void gte(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void gte(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void gte(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.GREATER_THAN_EQUAL, val));
    }

    public void lt(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lt(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lt(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lt(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lt(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lt(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN, val));
    }

    public void lte(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void lte(String key, short val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void lte(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void lte(String key, long val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void lte(String key, float val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void lte(String key, double val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LESS_THAN_EQUAL, val));
    }

    public void in(String key, @NotNull Short @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
    }

    public void in(String key, @NotNull Integer @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
    }

    public Filter in(String key, @NotNull Long @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
        return this;
    }

    public void in(String key, @NotNull Float @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
    }

    public void in(String key, @NotNull Double @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
    }

    public void like(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.LIKE, val));
    }

    public void ilike(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.ILIKE, val));
    }

    public void startsWith(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.STARTS_WITH, val));
    }

    public void iStartsWith(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.I_STARTS_WITH, val));
    }

    public void endsWith(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.ENDS_WITH, val));
    }

    public void iEndsWith(String key, @NotNull String val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.I_ENDS_WITH, val));
    }

    /*
     * Can add AND if expressions is not empty.
     * Can't add if previous expressions is also AND.
     */
    public Filter and() {
        if (data.isEmpty()) {
            throw new RuntimeException("Expression should not start with 'AND'.");
        }
        if (data.get(data.size() - 1).equals(Operator.AND)) {
            throw new RuntimeException("Two 'AND' expression should not contiguous.");
        }
        this.data.add(Operator.AND);
        return this;
    }

    /*
     * Can add OR if expressions is not empty.
     * Can't add if previous expressions is also OR
     */
    public Filter or() {
        if (data.isEmpty()) {
            throw new RuntimeException("Expression should not start with 'OR'.");
        }
        if (data.get(data.size() - 1).equals(Operator.OR)) {
            throw new RuntimeException("Two 'OR' expression should not contiguous.");
        }
        this.data.add(Operator.OR);
        return this;
    }

    /*
     * Can add NOT if expressions is empty.
     * Can't add NOT if previous expressions neither AND or OR while expressions is not empty.
     * Can't add if previous expressions is also NOT
     */
    public Filter not() {
        if (!data.isEmpty()) {
            Object prev = data.get(data.size() - 1);
            if (!(prev.equals(Operator.AND) || prev.equals(Operator.OR))) {
                throw new RuntimeException("Invalid literal for 'NOT' should use with 'AND' or 'OR'.");
            }
        }
        this.data.add(Operator.NOT);
        return this;
    }

    public Filter beginGroup() {
        return new Filter(this);
    }

    public Filter endGroup() {
        if (parent == null) {
            throw new RuntimeException("Can't endGroup as it's not started.");
        }
        if (data.isEmpty()) {
            throw new RuntimeException("Group should contain one or more expressions.");
        }
        parent.ensureBasic();
        parent.data.add(data);
        return parent;
    }

    /*
     * Should not end with 'AND' or 'OR' or 'NOT'
     */
    public List<Object> build() {
        if (!data.isEmpty()) {
            Object prev = data.get(data.size() - 1);
            if (prev.equals(Operator.AND) || prev.equals(Operator.OR) || prev.equals(Operator.NOT)) {
                throw new RuntimeException("Incomplete closer expression given.");
            }
        }
        return data;
    }

}
