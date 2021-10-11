package io.stacknix.merlin.db.queries;

import android.util.Pair;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Filter equal(String key, String val) {
        ensureBasic();
        if (val != null) {
            this.data.add(new Condition(key, Substitute.EQUAL, val));
        } else {
            this.data.add(new NullCondition(key, Substitute.IS_NULL));
        }
        return this;
    }

    public Filter notEqual(String key, String val) {
        ensureBasic();
        if (val != null) {
            this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
        } else {
            this.data.add(new NullCondition(key, Substitute.IS_NOT_NULL));
        }
        return this;
    }

    public Filter equal(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.EQUAL, val));
        return this;
    }

    public Filter notEqual(String key, int val) {
        ensureBasic();
        this.data.add(new Condition(key, Substitute.NOT_EQUAL, val));
        return this;
    }

    public Filter in(String key, @NotNull Long @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.IN, val));
        }
        return this;
    }

    public Filter notIn(String key, @NotNull Long @NotNull [] val) {
        if (val.length != 0) {
            ensureBasic();
            this.data.add(new Condition(key, Substitute.NOT_IN, val));
        }
        return this;
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

    public Map<String, Object> toSQL() {
        List<String> selectionArgs = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sql", objectsToSQL(build(), selectionArgs));
        map.put("args", selectionArgs);
        return map;
    }

    @SuppressWarnings("unchecked")
    private @Nullable String objectsToSQL(@NotNull List<Object> buildObject, List<String> selectionArgs) {
        if (!buildObject.isEmpty()) {
            List<String> strings = new ArrayList<>();
            for (Object obj : buildObject) {
                if (obj instanceof List) {
                    strings.add(String.format("(%s)", objectsToSQL((List<Object>) obj, selectionArgs)));
                }else if (obj instanceof Condition) {
                    strings.add(obj.toString());
                    selectionArgs.addAll(((Condition) obj).getSelectionsArgs());
                }else {
                    strings.add(obj.toString());
                }
            }
            return Joiner.on(" ").join(strings);
        }
        return null;
    }

}
