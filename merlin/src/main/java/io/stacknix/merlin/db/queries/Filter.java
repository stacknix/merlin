package io.stacknix.merlin.db.queries;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private final List<Condition> conditions;

    public Filter() {
        this.conditions = new ArrayList<>();
    }

    public Filter equal(String key, String val) {
        this.conditions.add(new Condition(key, val, Substitute.EQUAL));
        return this;
    }

    public Filter notEqual(String key, String val) {
        this.conditions.add(new Condition(key, val, Substitute.NOT_EQUAL));
        return this;
    }

    public List<Object> build(){
        List<Object> data = new ArrayList<>();
        return data;
    }

}
