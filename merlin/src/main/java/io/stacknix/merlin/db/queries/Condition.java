package io.stacknix.merlin.db.queries;

import android.util.Pair;

import com.google.common.base.Joiner;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.commons.Utils;


public class Condition {

    public String key;
    public String sub;
    public Object val;
    private List<String> selectionsArgs;

    public Condition(String key, String sub, Object val) {
        this.key = key;
        this.sub = sub;
        this.val = val;
    }

    public @NotNull String toString() {
        this.selectionsArgs = new ArrayList<>();
        String output = String.format("%s%s", key, sub);
        String q = "?";

        if (val instanceof Integer || val instanceof Long || val instanceof Float || val instanceof Double || val instanceof Boolean) {
            output = output.replaceFirst("\\?", q);
            selectionsArgs.add(String.valueOf(val));
        } else if (val instanceof String) {
            output = output.replaceFirst("\\?", String.format("'%s'", q));
            selectionsArgs.add(String.format("'%s'", val));
        } else if (val instanceof Integer[]) {
            String str = Joiner.on(", ").join(Utils.cloneArray(q, ((Integer[]) val).length));
            output = output.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : (Object[]) val) {
                selectionsArgs.add(String.valueOf(item));
            }
        } else if (val instanceof Long[]) {
            String str = Joiner.on(", ").join(Utils.cloneArray(q, ((Long[]) val).length));
            output = output.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : (Object[]) val) {
                selectionsArgs.add(String.valueOf(item));
            }
        } else if (val instanceof Float[]) {
            String str = Joiner.on(", ").join(Utils.cloneArray(q, ((Float[]) val).length));
            output = output.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : (Object[]) val) {
                selectionsArgs.add(String.valueOf(item));
            }
        } else if (val instanceof Double[]) {
            String str = Joiner.on(", ").join(Utils.cloneArray(q, ((Double[]) val).length));
            output = output.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : (Object[]) val) {
                selectionsArgs.add(String.valueOf(item));
            }
        } else if (val instanceof String[]) {
            String str = Joiner.on(", ").join(Utils.cloneArray(q, ((String[]) val).length));
            output = output.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : (Object[]) val) {
                selectionsArgs.add(String.format("'%s'", item));
            }
        } else {
            throw new RuntimeException("Unsupported value type given.");
        }
        return output;
    }

    public List<String> getSelectionsArgs(){
        return selectionsArgs;
    }

}
