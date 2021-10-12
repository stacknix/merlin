package io.stacknix.merlin.db.queries;

public final class Substitute {
    public static final String IS_NULL = "IS NULL";
    public static final String IS_NOT_NULL = "IS NOT NULL";

    public static final String EQUAL = "=?";
    public static final String NOT_EQUAL = "!=?";
    public static final String GREATER_THAN = ">?";
    public static final String GREATER_THAN_EQUAL = ">=?";
    public static final String LESS_THAN = "<?";
    public static final String LESS_THAN_EQUAL = "<=?";

    public static final String IN = " IN ?";
    public static final String NOT_IN = " NOT IN ?";
    public static final String LIKE = " LIKE %?%";
    public static final String NOT_LIKE = " NOT LIKE %?%";
    public static final String ILIKE = " ILIKE %?%";
    public static final String NOT_ILIKE = " NOT ILIKE %?%";
    public static final String STARTS_WITH = " LIKE ?%";
    public static final String NOT_STARTS_WITH = " NOT LIKE ?%";
    public static final String I_STARTS_WITH = " ILIKE ?%";
    public static final String NOT_I_STARTS_WITH = " NOT ILIKE ?%";
    public static final String ENDS_WITH = " LIKE %?";
    public static final String NOT_ENDS_WITH = " NOT LIKE %?";
    public static final String I_ENDS_WITH = " ILIKE %?";
    public static final String NOT_I_ENDS_WITH = " NOT ILIKE %?";
}
