package io.stacknix.merlin.db.queries;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final List<Object> filters;


    public static final class GroupOperator{

        private final Group group;

        private GroupOperator(Group group){
            this.group = group;
        }

        public Group and(){
            this.group.and();
            return this.group;
        }

        public Group or(){
            this.group.or();
            return this.group;
        }

    }

    public Group() {
        this.filters = new ArrayList<>();
    }

    public GroupOperator filter(Filter filter) {
        this.filters.add(filter);
        return new GroupOperator(this);
    }

    private void and() {
        this.filters.add(Operator.AND);
    }

    private void or() {
        this.filters.add(Operator.OR);
    }

    private void not() {
        this.filters.add(Operator.NOT);
    }

    public String build(){
        return null;
    }

}
