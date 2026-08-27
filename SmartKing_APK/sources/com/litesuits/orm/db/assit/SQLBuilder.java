package com.litesuits.orm.db.assit;

import android.util.SparseArray;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.annotation.Check;
import com.litesuits.orm.db.annotation.Collate;
import com.litesuits.orm.db.annotation.Conflict;
import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.Temporary;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.annotation.UniqueCombine;
import com.litesuits.orm.db.assit.CollSpliter;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.db.model.EntityTable;
import com.litesuits.orm.db.model.MapInfo;
import com.litesuits.orm.db.model.MapProperty;
import com.litesuits.orm.db.model.Property;
import com.litesuits.orm.db.utils.ClassUtil;
import com.litesuits.orm.db.utils.DataUtil;
import com.litesuits.orm.db.utils.FieldUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class SQLBuilder {
    public static final String AND = " AND ";
    public static final String ASC = " ASC ";
    public static final String ASTERISK = "*";
    public static final String BLANK = " ";
    public static final String CHECK = "CHECK ";
    public static final String COLLATE = "COLLATE ";
    public static final String COMMA = ",";
    public static final String COMMA_HOLDER = ",?";
    public static final String CREATE = "CREATE ";
    public static final String DEFAULT = "DEFAULT ";
    public static final String DELETE_FROM = "DELETE FROM ";
    public static final String DESC = " DESC ";
    public static final String DROP_TABLE = "DROP TABLE ";
    public static final String EQUALS_HOLDER = "=?";
    public static final String FROM = " FROM ";
    public static final String HOLDER = "?";
    public static final String IN = " IN ";
    public static final String INSERT = "INSERT ";
    public static final String INTO = "INTO ";
    public static final String LIMIT = " LIMIT ";
    public static final String NOT = " NOT ";
    public static final String NOT_NULL = "NOT NULL ";
    public static final String ON_CONFLICT = "ON CONFLICT ";
    public static final String OR = " OR ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String PARENTHESES_LEFT = "(";
    public static final String PARENTHESES_RIGHT = ")";
    public static final String PRAGMA_TABLE_INFO = "PRAGMA table_info(";
    public static final String PRIMARY_KEY = "PRIMARY KEY ";
    public static final String PRIMARY_KEY_AUTOINCREMENT = "PRIMARY KEY AUTOINCREMENT ";
    public static final String REPLACE = "REPLACE ";
    public static final String SELECT = "SELECT ";
    public static final String SELECT_ANY_FROM = "SELECT * FROM ";
    public static final String SELECT_MAX = "SELECT MAX ";
    public static final String SELECT_TABLES = "SELECT * FROM sqlite_master WHERE type='table' ORDER BY name";
    public static final String SET = " SET ";
    public static final String TABLE_IF_NOT_EXISTS = "TABLE IF NOT EXISTS ";
    public static final String TEMP = "TEMP ";
    public static final String TWO_HOLDER = "(?,?)";
    public static final int TYPE_INSERT = 1;
    public static final int TYPE_REPLACE = 2;
    public static final int TYPE_UPDATE = 3;
    public static final String UNIQUE = "UNIQUE ";
    public static final String UPDATE = "UPDATE ";
    public static final String VALUES = "VALUES";
    public static final String WHERE = " WHERE ";

    public static SQLStatement buildAddColumnSql(String str, String str2) {
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "ALTER TABLE " + str + " ADD COLUMN " + str2;
        return sQLStatement;
    }

    public static SQLStatement buildColumnsObtainAll(String str) {
        return new SQLStatement(PRAGMA_TABLE_INFO + str + PARENTHESES_RIGHT, null);
    }

    public static SQLStatement buildCreateTable(EntityTable entityTable) {
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append(CREATE);
        if (entityTable.getAnnotation(Temporary.class) != null) {
            sb.append(TEMP);
        }
        sb.append(TABLE_IF_NOT_EXISTS);
        sb.append(entityTable.name);
        sb.append(PARENTHESES_LEFT);
        if (entityTable.key != null) {
            if (entityTable.key.assign == AssignType.AUTO_INCREMENT) {
                sb.append(entityTable.key.column);
                sb.append(DataUtil.INTEGER);
                sb.append(PRIMARY_KEY_AUTOINCREMENT);
            } else {
                sb.append(entityTable.key.column);
                sb.append(DataUtil.getSQLDataType(entityTable.key.classType));
                sb.append(PRIMARY_KEY);
            }
            z = true;
        } else {
            z = false;
        }
        if (!Checker.isEmpty(entityTable.pmap)) {
            if (z) {
                sb.append(",");
            }
            SparseArray sparseArray = null;
            boolean z2 = false;
            for (Map.Entry<String, Property> entry : entityTable.pmap.entrySet()) {
                if (z2) {
                    sb.append(",");
                } else {
                    z2 = true;
                }
                String key = entry.getKey();
                sb.append(key);
                if (entry.getValue() == null) {
                    sb.append(DataUtil.TEXT);
                } else {
                    Field field = entry.getValue().field;
                    sb.append(DataUtil.getSQLDataType(entry.getValue().classType));
                    if (field.getAnnotation(NotNull.class) != null) {
                        sb.append(NOT_NULL);
                    }
                    if (field.getAnnotation(Default.class) != null) {
                        sb.append(DEFAULT);
                        sb.append(((Default) field.getAnnotation(Default.class)).value());
                        sb.append(BLANK);
                    }
                    if (field.getAnnotation(Unique.class) != null) {
                        sb.append(UNIQUE);
                    }
                    if (field.getAnnotation(Conflict.class) != null) {
                        sb.append(ON_CONFLICT);
                        sb.append(((Conflict) field.getAnnotation(Conflict.class)).value().getSql());
                        sb.append(BLANK);
                    }
                    if (field.getAnnotation(Check.class) != null) {
                        sb.append("CHECK (");
                        sb.append(((Check) field.getAnnotation(Check.class)).value());
                        sb.append(PARENTHESES_RIGHT);
                        sb.append(BLANK);
                    }
                    if (field.getAnnotation(Collate.class) != null) {
                        sb.append(COLLATE);
                        sb.append(((Collate) field.getAnnotation(Collate.class)).value());
                        sb.append(BLANK);
                    }
                    UniqueCombine uniqueCombine = (UniqueCombine) field.getAnnotation(UniqueCombine.class);
                    if (uniqueCombine != null) {
                        if (sparseArray == null) {
                            sparseArray = new SparseArray();
                        }
                        ArrayList arrayList = (ArrayList) sparseArray.get(uniqueCombine.value());
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                            sparseArray.put(uniqueCombine.value(), arrayList);
                        }
                        arrayList.add(key);
                    }
                }
            }
            if (sparseArray != null) {
                int size = sparseArray.size();
                for (int i = 0; i < size; i++) {
                    ArrayList arrayList2 = (ArrayList) sparseArray.valueAt(i);
                    if (arrayList2.size() > 1) {
                        sb.append(",");
                        sb.append(UNIQUE);
                        sb.append(PARENTHESES_LEFT);
                        int size2 = arrayList2.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            if (i2 != 0) {
                                sb.append(",");
                            }
                            sb.append((String) arrayList2.get(i2));
                        }
                        sb.append(PARENTHESES_RIGHT);
                    }
                }
            }
        }
        sb.append(PARENTHESES_RIGHT);
        return new SQLStatement(sb.toString(), null);
    }

    public static MapInfo buildDelAllMappingSql(Class cls) {
        EntityTable table = TableManager.getTable((Class<?>) cls);
        if (Checker.isEmpty(table.mappingList)) {
            return null;
        }
        try {
            MapInfo mapInfo = new MapInfo();
            Iterator<MapProperty> it = table.mappingList.iterator();
            while (it.hasNext()) {
                EntityTable table2 = TableManager.getTable((Class<?>) getTypeByRelation(it.next()));
                mapInfo.addTable(new MapInfo.MapTable(TableManager.getMapTableName(table, table2), table.name, table2.name));
                mapInfo.addDelOldRelationSQL(buildMappingDeleteAllSql(table, table2));
            }
            return mapInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SQLStatement buildDeleteAllSql(Class<?> cls) {
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "DELETE FROM " + TableManager.getTable(cls).name;
        return sQLStatement;
    }

    public static SQLStatement buildDeleteSql(Class<?> cls, long j, long j2, String str) {
        SQLStatement sQLStatement = new SQLStatement();
        EntityTable table = TableManager.getTable(cls);
        String str2 = table.key.column;
        if (Checker.isEmpty(str)) {
            str = str2;
        }
        sQLStatement.sql = "DELETE FROM " + table.name + " WHERE " + str2 + IN + PARENTHESES_LEFT + "SELECT " + str2 + " FROM " + table.name + " ORDER BY " + str + ASC + " LIMIT " + j + "," + j2 + PARENTHESES_RIGHT;
        return sQLStatement;
    }

    public static SQLStatement buildDeleteSql(Object obj) {
        SQLStatement sQLStatement = new SQLStatement();
        try {
            EntityTable table = TableManager.getTable(obj);
            int i = 0;
            if (table.key != null) {
                sQLStatement.sql = "DELETE FROM " + table.name + " WHERE " + table.key.column + "=?";
                sQLStatement.bindArgs = new String[]{String.valueOf(FieldUtil.get(table.key.field, obj))};
            } else if (!Checker.isEmpty(table.pmap)) {
                StringBuilder sb = new StringBuilder();
                sb.append("DELETE FROM ");
                sb.append(table.name);
                sb.append(" WHERE ");
                Object[] objArr = new Object[table.pmap.size()];
                for (Map.Entry<String, Property> entry : table.pmap.entrySet()) {
                    if (i == 0) {
                        sb.append(entry.getKey());
                        sb.append("=?");
                    } else {
                        sb.append(" AND ");
                        sb.append(entry.getKey());
                        sb.append("=?");
                    }
                    objArr[i] = FieldUtil.get(entry.getValue().field, obj);
                    i++;
                }
                sQLStatement.sql = sb.toString();
                sQLStatement.bindArgs = objArr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sQLStatement;
    }

    public static SQLStatement buildDeleteSql(Collection<?> collection) {
        SQLStatement sQLStatement = new SQLStatement();
        try {
            StringBuilder sb = new StringBuilder(256);
            EntityTable entityTable = null;
            Object[] objArr = new Object[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                if (i == 0) {
                    entityTable = TableManager.getTable(obj);
                    sb.append("DELETE FROM ");
                    sb.append(entityTable.name);
                    sb.append(" WHERE ");
                    sb.append(entityTable.key.column);
                    sb.append(IN);
                    sb.append(PARENTHESES_LEFT);
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
                objArr[i] = FieldUtil.get(entityTable.key.field, obj);
                i++;
            }
            sb.append(PARENTHESES_RIGHT);
            sQLStatement.sql = sb.toString();
            sQLStatement.bindArgs = objArr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sQLStatement;
    }

    public static SQLStatement buildDropTable(EntityTable entityTable) {
        return new SQLStatement(DROP_TABLE + entityTable.name, null);
    }

    public static SQLStatement buildDropTable(String str) {
        return new SQLStatement(DROP_TABLE + str, null);
    }

    public static SQLStatement buildGetLastRowId(EntityTable entityTable) {
        return new SQLStatement("SELECT MAX (" + entityTable.key.column + PARENTHESES_RIGHT + " FROM " + entityTable.name, null);
    }

    public static SQLStatement buildInsertAllSql(Object obj, ConflictAlgorithm conflictAlgorithm) {
        return buildInsertSql(obj, false, 1, conflictAlgorithm);
    }

    public static SQLStatement buildInsertSql(Object obj, ConflictAlgorithm conflictAlgorithm) {
        return buildInsertSql(obj, true, 1, conflictAlgorithm);
    }

    private static SQLStatement buildInsertSql(Object obj, boolean z, int i, ConflictAlgorithm conflictAlgorithm) {
        SQLStatement sQLStatement = new SQLStatement();
        try {
            EntityTable table = TableManager.getTable(obj);
            StringBuilder sb = new StringBuilder(128);
            if (i != 2) {
                sb.append(INSERT);
                if (conflictAlgorithm != null) {
                    sb.append(conflictAlgorithm.getAlgorithm());
                    sb.append(INTO);
                } else {
                    sb.append(INTO);
                }
            } else {
                sb.append(REPLACE);
                sb.append(INTO);
            }
            sb.append(table.name);
            sb.append(PARENTHESES_LEFT);
            sb.append(table.key.column);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(PARENTHESES_RIGHT);
            sb2.append(VALUES);
            sb2.append(PARENTHESES_LEFT);
            sb2.append("?");
            int i2 = 0;
            int size = !Checker.isEmpty(table.pmap) ? table.pmap.size() + 1 : 1;
            Object[] objArr = null;
            if (z) {
                objArr = new Object[size];
                objArr[0] = FieldUtil.getAssignedKeyObject(table.key, obj);
                i2 = 1;
            }
            if (!Checker.isEmpty(table.pmap)) {
                for (Map.Entry<String, Property> entry : table.pmap.entrySet()) {
                    sb.append(",");
                    sb.append(entry.getKey());
                    sb2.append(",?");
                    if (z) {
                        objArr[i2] = FieldUtil.get(entry.getValue().field, obj);
                    }
                    i2++;
                }
            }
            sb.append((CharSequence) sb2);
            sb.append(PARENTHESES_RIGHT);
            sQLStatement.bindArgs = objArr;
            sQLStatement.sql = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sQLStatement;
    }

    public static Object[] buildInsertSqlArgsOnly(Object obj) throws IllegalAccessException {
        EntityTable table = TableManager.getTable(obj);
        int i = 1;
        Object[] objArr = new Object[!Checker.isEmpty(table.pmap) ? table.pmap.size() + 1 : 1];
        objArr[0] = FieldUtil.getAssignedKeyObject(table.key, obj);
        if (!Checker.isEmpty(table.pmap)) {
            Iterator<Property> it = table.pmap.values().iterator();
            while (it.hasNext()) {
                objArr[i] = FieldUtil.get(it.next().field, obj);
                i++;
            }
        }
        return objArr;
    }

    private static SQLStatement buildMappingDeleteAllSql(EntityTable entityTable, EntityTable entityTable2) throws IllegalArgumentException, IllegalAccessException {
        if (entityTable2 == null) {
            return null;
        }
        String mapTableName = TableManager.getMapTableName(entityTable, entityTable2);
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "DELETE FROM " + mapTableName;
        return sQLStatement;
    }

    public static SQLStatement buildMappingDeleteSql(Object obj, EntityTable entityTable, EntityTable entityTable2) throws IllegalArgumentException, IllegalAccessException {
        if (entityTable2 != null) {
            return buildMappingDeleteSql(TableManager.getMapTableName(entityTable, entityTable2), obj, entityTable);
        }
        return null;
    }

    public static SQLStatement buildMappingDeleteSql(String str, Object obj, EntityTable entityTable) throws IllegalArgumentException, IllegalAccessException {
        if (str == null) {
            return null;
        }
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "DELETE FROM " + str + " WHERE " + entityTable.name + "=?";
        sQLStatement.bindArgs = new Object[]{obj};
        return sQLStatement;
    }

    public static MapInfo buildMappingInfo(Object obj, boolean z, TableManager tableManager) {
        Object obj2;
        ArrayList<SQLStatement> buildMappingToManySql;
        EntityTable table = TableManager.getTable(obj);
        if (!Checker.isEmpty(table.mappingList)) {
            try {
                Object obj3 = FieldUtil.get(table.key.field, obj);
                if (obj3 == null) {
                    return null;
                }
                MapInfo mapInfo = new MapInfo();
                Iterator<MapProperty> it = table.mappingList.iterator();
                while (it.hasNext()) {
                    MapProperty next = it.next();
                    EntityTable table2 = TableManager.getTable((Class<?>) getTypeByRelation(next));
                    mapInfo.addTable(new MapInfo.MapTable(TableManager.getMapTableName(table, table2), table.name, table2.name));
                    if (tableManager.isSQLMapTableCreated(table.name, table2.name)) {
                        mapInfo.addDelOldRelationSQL(buildMappingDeleteSql(obj3, table, table2));
                    }
                    if (z && (obj2 = FieldUtil.get(next.field, obj)) != null) {
                        if (next.isToMany()) {
                            if (obj2 instanceof Collection) {
                                buildMappingToManySql = buildMappingToManySql(obj3, table, table2, (Collection) obj2);
                            } else {
                                if (!(obj2 instanceof Object[])) {
                                    throw new RuntimeException("OneToMany and ManyToMany Relation, You must use array or collection object");
                                }
                                buildMappingToManySql = buildMappingToManySql(obj3, table, table2, Arrays.asList((Object[]) obj2));
                            }
                            if (Checker.isEmpty(buildMappingToManySql)) {
                                mapInfo.addNewRelationSQL(buildMappingToManySql);
                            }
                        } else {
                            SQLStatement buildMappingToOneSql = buildMappingToOneSql(obj3, table, table2, obj2);
                            if (buildMappingToOneSql != null) {
                                mapInfo.addNewRelationSQL(buildMappingToOneSql);
                            }
                        }
                    }
                }
                return mapInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> ArrayList<SQLStatement> buildMappingToManySql(final Object obj, final EntityTable entityTable, final EntityTable entityTable2, Collection<T> collection) throws Exception {
        final ArrayList<SQLStatement> arrayList = new ArrayList<>();
        CollSpliter.split(collection, 499, new CollSpliter.Spliter<T>() { // from class: com.litesuits.orm.db.assit.SQLBuilder.1
            @Override // com.litesuits.orm.db.assit.CollSpliter.Spliter
            public int oneSplit(ArrayList<T> arrayList2) throws Exception {
                SQLStatement buildMappingToManySqlFragment = SQLBuilder.buildMappingToManySqlFragment(obj, entityTable, entityTable2, arrayList2);
                if (buildMappingToManySqlFragment == null) {
                    return 0;
                }
                arrayList.add(buildMappingToManySqlFragment);
                return 0;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SQLStatement buildMappingToManySqlFragment(Object obj, EntityTable entityTable, EntityTable entityTable2, Collection<?> collection) throws IllegalArgumentException, IllegalAccessException {
        String mapTableName = TableManager.getMapTableName(entityTable, entityTable2);
        if (Checker.isEmpty(collection)) {
            return null;
        }
        boolean z = true;
        StringBuilder sb = new StringBuilder(128);
        ArrayList arrayList = new ArrayList();
        String valueOf = String.valueOf(obj);
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            Object assignedKeyObject = FieldUtil.getAssignedKeyObject(entityTable2.key, it.next());
            if (assignedKeyObject != null) {
                if (z) {
                    sb.append(TWO_HOLDER);
                    z = false;
                } else {
                    sb.append(",");
                    sb.append(TWO_HOLDER);
                }
                arrayList.add(valueOf);
                arrayList.add(String.valueOf(assignedKeyObject));
            }
        }
        Object[] array = arrayList.toArray(new String[arrayList.size()]);
        if (Checker.isEmpty(array)) {
            return null;
        }
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = "REPLACE INTO " + mapTableName + PARENTHESES_LEFT + entityTable.name + "," + entityTable2.name + PARENTHESES_RIGHT + VALUES + ((Object) sb);
        sQLStatement.bindArgs = array;
        return sQLStatement;
    }

    public static SQLStatement buildMappingToOneSql(Object obj, EntityTable entityTable, EntityTable entityTable2, Object obj2) throws IllegalArgumentException, IllegalAccessException {
        Object assignedKeyObject = FieldUtil.getAssignedKeyObject(entityTable2.key, obj2);
        if (assignedKeyObject != null) {
            return buildMappingToOneSql(TableManager.getMapTableName(entityTable, entityTable2), obj, assignedKeyObject, entityTable, entityTable2);
        }
        return null;
    }

    public static SQLStatement buildMappingToOneSql(String str, Object obj, Object obj2, EntityTable entityTable, EntityTable entityTable2) throws IllegalArgumentException, IllegalAccessException {
        if (obj2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(INSERT);
        sb.append(INTO);
        sb.append(str);
        sb.append(PARENTHESES_LEFT);
        sb.append(entityTable.name);
        sb.append(",");
        sb.append(entityTable2.name);
        sb.append(PARENTHESES_RIGHT);
        sb.append(VALUES);
        sb.append(TWO_HOLDER);
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = sb.toString();
        sQLStatement.bindArgs = new Object[]{obj, obj2};
        return sQLStatement;
    }

    public static SQLStatement buildQueryMapEntitySql(EntityTable entityTable, Object obj) {
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = SELECT_ANY_FROM + entityTable.name + " WHERE " + entityTable.key.column + "=?";
        sQLStatement.bindArgs = new String[]{String.valueOf(obj)};
        return sQLStatement;
    }

    public static SQLStatement buildQueryRelationSql(EntityTable entityTable, EntityTable entityTable2, Object obj) {
        SQLStatement sQLStatement = new SQLStatement();
        sQLStatement.sql = SELECT_ANY_FROM + TableManager.getMapTableName(entityTable, entityTable2) + " WHERE " + entityTable.name + "=?";
        sQLStatement.bindArgs = new String[]{String.valueOf(obj)};
        return sQLStatement;
    }

    public static SQLStatement buildQueryRelationSql(Class cls, Class cls2, List<String> list) {
        return buildQueryRelationSql(cls, cls2, list, null);
    }

    private static SQLStatement buildQueryRelationSql(Class cls, Class cls2, List<String> list, List<String> list2) {
        StringBuilder sb;
        EntityTable table = TableManager.getTable((Class<?>) cls);
        EntityTable table2 = TableManager.getTable((Class<?>) cls2);
        QueryBuilder queryMappingInfo = new QueryBuilder(cls).queryMappingInfo(cls2);
        ArrayList arrayList = new ArrayList();
        if (Checker.isEmpty(list)) {
            sb = null;
        } else {
            sb = new StringBuilder();
            sb.append(table.name);
            sb.append(IN);
            sb.append(PARENTHESES_LEFT);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
            }
            sb.append(PARENTHESES_RIGHT);
            arrayList.addAll(list);
        }
        if (!Checker.isEmpty(list2)) {
            if (sb == null) {
                sb = new StringBuilder();
            } else {
                sb.append(" AND ");
            }
            sb.append(table2.name);
            sb.append(IN);
            sb.append(PARENTHESES_LEFT);
            int size2 = list2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                if (i2 == 0) {
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
            }
            sb.append(PARENTHESES_RIGHT);
            arrayList.addAll(list2);
        }
        if (sb != null) {
            queryMappingInfo.where(sb.toString(), arrayList.toArray(new String[arrayList.size()]));
        }
        return queryMappingInfo.createStatement();
    }

    public static SQLStatement buildReplaceAllSql(Object obj) {
        return buildInsertSql(obj, false, 2, null);
    }

    public static SQLStatement buildReplaceSql(Object obj) {
        return buildInsertSql(obj, true, 2, null);
    }

    public static SQLStatement buildTableObtainAll() {
        return new SQLStatement(SELECT_TABLES, null);
    }

    public static SQLStatement buildUpdateAllSql(Object obj, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        return buildUpdateSql(obj, columnsValue, conflictAlgorithm, false);
    }

    public static SQLStatement buildUpdateSql(WhereBuilder whereBuilder, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        Object[] whereArgs;
        SQLStatement sQLStatement = new SQLStatement();
        try {
            EntityTable table = TableManager.getTable((Class<?>) whereBuilder.getTableClass());
            StringBuilder sb = new StringBuilder(128);
            sb.append(UPDATE);
            if (conflictAlgorithm != null) {
                sb.append(conflictAlgorithm.getAlgorithm());
            }
            sb.append(table.name);
            sb.append(SET);
            if (columnsValue == null || !columnsValue.checkColumns()) {
                whereArgs = whereBuilder.getWhereArgs();
            } else {
                Object[] whereArgs2 = whereBuilder.getWhereArgs();
                whereArgs = whereArgs2 != null ? new Object[columnsValue.columns.length + whereArgs2.length] : new Object[columnsValue.columns.length];
                int i = 0;
                int i2 = 0;
                while (i2 < columnsValue.columns.length) {
                    if (i2 > 0) {
                        sb.append(",");
                    }
                    sb.append(columnsValue.columns[i2]);
                    sb.append("=?");
                    whereArgs[i2] = columnsValue.getValue(columnsValue.columns[i2]);
                    i2++;
                }
                if (whereArgs2 != null) {
                    int length = whereArgs2.length;
                    while (i < length) {
                        whereArgs[i2] = whereArgs2[i];
                        i++;
                        i2++;
                    }
                }
            }
            sb.append(whereBuilder.createWhereString());
            sQLStatement.sql = sb.toString();
            sQLStatement.bindArgs = whereArgs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sQLStatement;
    }

    public static SQLStatement buildUpdateSql(Object obj, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        return buildUpdateSql(obj, columnsValue, conflictAlgorithm, true);
    }

    private static SQLStatement buildUpdateSql(Object obj, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm, boolean z) {
        int i;
        Object[] objArr;
        int i2;
        SQLStatement sQLStatement = new SQLStatement();
        try {
            EntityTable table = TableManager.getTable(obj);
            StringBuilder sb = new StringBuilder(128);
            sb.append(UPDATE);
            if (conflictAlgorithm != null) {
                sb.append(conflictAlgorithm.getAlgorithm());
            }
            sb.append(table.name);
            sb.append(SET);
            int i3 = 0;
            if (columnsValue != null && columnsValue.checkColumns()) {
                if (z) {
                    i2 = columnsValue.columns.length + 1;
                    objArr = new Object[i2];
                } else {
                    objArr = null;
                    i2 = 1;
                }
                while (i3 < columnsValue.columns.length) {
                    if (i3 > 0) {
                        sb.append(",");
                    }
                    sb.append(columnsValue.columns[i3]);
                    sb.append("=?");
                    if (z) {
                        objArr[i3] = columnsValue.getValue(columnsValue.columns[i3]);
                        if (objArr[i3] == null) {
                            objArr[i3] = FieldUtil.get(table.pmap.get(columnsValue.columns[i3]).field, obj);
                        }
                    }
                    i3++;
                }
                i = i2;
                r3 = objArr;
            } else if (Checker.isEmpty(table.pmap)) {
                r3 = z ? new Object[1] : null;
                i = 1;
            } else {
                if (z) {
                    i = table.pmap.size() + 1;
                    r3 = new Object[i];
                } else {
                    i = 1;
                }
                for (Map.Entry<String, Property> entry : table.pmap.entrySet()) {
                    if (i3 > 0) {
                        sb.append(",");
                    }
                    sb.append(entry.getKey());
                    sb.append("=?");
                    if (z) {
                        r3[i3] = FieldUtil.get(entry.getValue().field, obj);
                    }
                    i3++;
                }
            }
            if (z) {
                r3[i - 1] = FieldUtil.getAssignedKeyObject(table.key, obj);
            }
            sb.append(" WHERE ");
            sb.append(table.key.column);
            sb.append("=?");
            sQLStatement.sql = sb.toString();
            sQLStatement.bindArgs = r3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sQLStatement;
    }

    public static Object[] buildUpdateSqlArgsOnly(Object obj, ColumnsValue columnsValue) throws IllegalAccessException {
        Object[] objArr;
        int i;
        EntityTable table = TableManager.getTable(obj);
        int i2 = 0;
        if (columnsValue != null && columnsValue.checkColumns()) {
            int length = columnsValue.columns.length + 1;
            objArr = new Object[length];
            while (i2 < columnsValue.columns.length) {
                objArr[i2] = columnsValue.getValue(columnsValue.columns[i2]);
                if (objArr[i2] == null) {
                    objArr[i2] = FieldUtil.get(table.pmap.get(columnsValue.columns[i2]).field, obj);
                }
                i2++;
            }
            i = length;
        } else if (Checker.isEmpty(table.pmap)) {
            objArr = new Object[1];
            i = 1;
        } else {
            i = table.pmap.size() + 1;
            objArr = new Object[i];
            Iterator<Map.Entry<String, Property>> it = table.pmap.entrySet().iterator();
            while (it.hasNext()) {
                objArr[i2] = FieldUtil.get(it.next().getValue().field, obj);
                i2++;
            }
        }
        objArr[i - 1] = FieldUtil.getAssignedKeyObject(table.key, obj);
        return objArr;
    }

    private static Class getTypeByRelation(MapProperty mapProperty) {
        if (!mapProperty.isToMany()) {
            return mapProperty.field.getType();
        }
        Class<?> type = mapProperty.field.getType();
        if (ClassUtil.isCollection(type)) {
            return FieldUtil.getGenericType(mapProperty.field);
        }
        if (ClassUtil.isArray(type)) {
            return FieldUtil.getComponentType(mapProperty.field);
        }
        throw new RuntimeException("OneToMany and ManyToMany Relation, you must use collection or array object");
    }
}
