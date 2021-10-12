package io.stacknix.merlin.db.queries;

import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLBuilder {

    private final String sql;
    private final List<String> selectionArgs;

    public SQLBuilder(@NotNull Filter filter){
        this.selectionArgs = new ArrayList<>();
        this.sql = objectsToSQL(filter.build(), selectionArgs);
    }

    public String getSQL(){
        return sql;
    }

    public String[] getSelectionArgs(){
        String[] args = new String[selectionArgs.size()];
        for (int i=0;i<selectionArgs.size();i++){
            args[i] = selectionArgs.get(i);
        }
        return args;
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
