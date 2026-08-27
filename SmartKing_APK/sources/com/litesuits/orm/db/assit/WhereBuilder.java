package com.litesuits.orm.db.assit;

import com.litesuits.orm.db.TableManager;

/* loaded from: classes.dex */
public class WhereBuilder {
    public static final String AND = " AND ";
    public static final String COMMA_HOLDER = ",?";
    public static final String DELETE = "DELETE FROM ";
    public static final String EQUAL_HOLDER = "=?";
    public static final String GREATER_THAN_HOLDER = ">?";
    public static final String HOLDER = "?";
    private static final String IN = " IN ";
    public static final String LESS_THAN_HOLDER = "<?";
    public static final String NOT = " NOT ";
    public static final String NOTHING = "";
    public static final String NOT_EQUAL_HOLDER = "!=?";
    public static final String OR = " OR ";
    private static final String PARENTHESES_LEFT = "(";
    private static final String PARENTHESES_RIGHT = ")";
    public static final String WHERE = " WHERE ";
    protected Class tableClass;
    protected String where;
    protected Object[] whereArgs;

    public WhereBuilder(Class cls) {
        this.tableClass = cls;
    }

    public WhereBuilder(Class cls, String str, Object[] objArr) {
        this.where = str;
        this.whereArgs = objArr;
        this.tableClass = cls;
    }

    private String buildWhereIn(String str, int i) {
        StringBuilder sb = new StringBuilder(str);
        sb.append(" IN ");
        sb.append("(");
        sb.append("?");
        for (int i2 = 1; i2 < i; i2++) {
            sb.append(",?");
        }
        sb.append(")");
        return sb.toString();
    }

    public static WhereBuilder create(Class cls) {
        return new WhereBuilder(cls);
    }

    public static WhereBuilder create(Class cls, String str, Object[] objArr) {
        return new WhereBuilder(cls, str, objArr);
    }

    public WhereBuilder and() {
        if (this.where != null) {
            this.where += " AND ";
        }
        return this;
    }

    public WhereBuilder and(String str, Object... objArr) {
        return append(" AND ", str, objArr);
    }

    public WhereBuilder andEquals(String str, Object obj) {
        return append(" AND ", str + "=?", obj);
    }

    public WhereBuilder andIn(String str, Object... objArr) {
        return append(" AND ", buildWhereIn(str, objArr.length), objArr);
    }

    public WhereBuilder append(String str, String str2, Object... objArr) {
        if (this.where == null) {
            this.where = str2;
            this.whereArgs = objArr;
        } else {
            if (str != null) {
                this.where += str;
            }
            this.where += str2;
            if (this.whereArgs == null) {
                this.whereArgs = objArr;
            } else {
                Object[] objArr2 = new Object[this.whereArgs.length + objArr.length];
                System.arraycopy(this.whereArgs, 0, objArr2, 0, this.whereArgs.length);
                System.arraycopy(objArr, 0, objArr2, this.whereArgs.length, objArr.length);
                this.whereArgs = objArr2;
            }
        }
        return this;
    }

    public SQLStatement createStatementDelete() {
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "DELETE FROM " + TableManager.getTableName(this.tableClass) + createWhereString();
        sQLStatement.bindArgs = transToStringArray();
        return sQLStatement;
    }

    public String createWhereString() {
        if (this.where == null) {
            return "";
        }
        return " WHERE " + this.where;
    }

    public WhereBuilder equals(String str, Object obj) {
        return append(null, str + "=?", obj);
    }

    public Class getTableClass() {
        return this.tableClass;
    }

    public String getWhere() {
        return this.where;
    }

    public Object[] getWhereArgs() {
        return this.whereArgs;
    }

    public WhereBuilder greaterThan(String str, Object obj) {
        return append(null, str + GREATER_THAN_HOLDER, obj);
    }

    public WhereBuilder in(String str, Object... objArr) {
        return append(null, buildWhereIn(str, objArr.length), objArr);
    }

    public WhereBuilder lessThan(String str, Object obj) {
        return append(null, str + LESS_THAN_HOLDER, obj);
    }

    public WhereBuilder noEquals(String str, Object obj) {
        return append(null, str + NOT_EQUAL_HOLDER, obj);
    }

    public WhereBuilder not() {
        if (this.where != null) {
            this.where += " NOT ";
        }
        return this;
    }

    public WhereBuilder or() {
        if (this.where != null) {
            this.where += " OR ";
        }
        return this;
    }

    public WhereBuilder or(String str, Object... objArr) {
        return append(" OR ", str, objArr);
    }

    public WhereBuilder orEquals(String str, Object obj) {
        return append(" OR ", str + "=?", obj);
    }

    public WhereBuilder orIn(String str, Object... objArr) {
        return append(" OR ", buildWhereIn(str, objArr.length), objArr);
    }

    public void setWhere(String str) {
        this.where = str;
    }

    public void setWhereArgs(Object[] objArr) {
        this.whereArgs = objArr;
    }

    public String[] transToStringArray() {
        if (this.whereArgs == null || this.whereArgs.length <= 0) {
            return null;
        }
        if (this.whereArgs instanceof String[]) {
            return (String[]) this.whereArgs;
        }
        String[] strArr = new String[this.whereArgs.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = String.valueOf(this.whereArgs[i]);
        }
        return strArr;
    }

    public WhereBuilder where(String str, Object... objArr) {
        this.where = str;
        this.whereArgs = objArr;
        return this;
    }
}
