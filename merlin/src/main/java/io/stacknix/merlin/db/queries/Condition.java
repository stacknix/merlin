package io.stacknix.merlin.db.queries;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.stacknix.merlin.db.commons.Pair;
import io.stacknix.merlin.db.commons.Utils;


public class Condition {

    public String key;
    public String sub;
    public Object val;

    public Condition(String key, String sub, Object val) {
        this.key = key;
        this.sub = sub;
        this.val = val;
    }

    public @NotNull Pair<String, List<String>> build() {
        final List<String> selectionsArgs = new ArrayList<>();
        final String placeholder = "?";
        final List<String> subWithSpace = Arrays.asList(Substitute.IN, Substitute.NOT_IN,
                Substitute.LIKE, Substitute.NOT_LIKE, Substitute.ILIKE, Substitute.NOT_ILIKE);
        String fmt = subWithSpace.contains(sub) ? String.format("%s %s", key, sub) : String.format("%s%s", key, sub);

        if (val instanceof Short || val instanceof Integer || val instanceof Long || val instanceof Float
                || val instanceof Double || val instanceof Boolean || val instanceof String) {
            fmt = fmt.replaceFirst("\\?", placeholder);
            selectionsArgs.add(String.valueOf(val));
        } else if (val instanceof Object[]) {
            Object[] values = (Object[]) val;
            if (values.length == 0) {
                throw new RuntimeException("No empty array value required.");
            }
            String str = Joiner.on(", ").join(Utils.cloneArray(placeholder, values.length));
            fmt = fmt.replaceFirst("\\?", String.format("(%s)", str));
            for (Object item : values) {
                selectionsArgs.add(String.valueOf(item));
            }
        } else {
            throw new RuntimeException("Unsupported value type given.");
        }
        return new Pair<>(fmt, selectionsArgs);
    }


}
