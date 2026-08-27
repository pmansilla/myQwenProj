package com.litesuits.orm.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.Querier;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.litesuits.orm.db.assit.SQLStatement;
import com.litesuits.orm.db.assit.Transaction;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.model.EntityTable;
import com.litesuits.orm.db.model.MapProperty;
import com.litesuits.orm.db.model.Primarykey;
import com.litesuits.orm.db.model.Property;
import com.litesuits.orm.db.model.SQLiteColumn;
import com.litesuits.orm.db.model.SQLiteTable;
import com.litesuits.orm.db.utils.DataUtil;
import com.litesuits.orm.db.utils.FieldUtil;
import com.litesuits.orm.log.OrmLog;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class TableManager {
    private static final String TAG = "TableManager";
    private String dbName;
    private final HashMap<String, SQLiteTable> mSqlTableMap = new HashMap<>();
    private static final String[] ID = {ConnectionModel.ID, FileDownloadModel.ID};
    private static final HashMap<String, EntityTable> mEntityTableMap = new HashMap<>();

    public TableManager(String str, SQLiteDatabase sQLiteDatabase) {
        this.dbName = "";
        this.dbName = str;
        initSqlTable(sQLiteDatabase);
    }

    private boolean checkExistAndColumns(SQLiteDatabase sQLiteDatabase, EntityTable entityTable) {
        SQLiteTable sQLiteTable = this.mSqlTableMap.get(entityTable.name);
        if (sQLiteTable == null) {
            if (OrmLog.isPrint) {
                OrmLog.d(TAG, "Table [" + entityTable.name + "] Not Exist");
            }
            return false;
        }
        if (OrmLog.isPrint) {
            OrmLog.d(TAG, "Table [" + entityTable.name + "] Exist");
        }
        if (!sQLiteTable.isTableChecked) {
            sQLiteTable.isTableChecked = true;
            if (OrmLog.isPrint) {
                OrmLog.i(TAG, "Table [" + entityTable.name + "] check column now.");
            }
            if (entityTable.key != null && sQLiteTable.columns.get(entityTable.key.column) == null) {
                SQLBuilder.buildDropTable(sQLiteTable.name).execute(sQLiteDatabase);
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "Table [" + entityTable.name + "] Primary Key has changed, so drop and recreate it later.");
                }
                return false;
            }
            if (entityTable.pmap != null) {
                ArrayList arrayList = new ArrayList();
                for (String str : entityTable.pmap.keySet()) {
                    if (sQLiteTable.columns.get(str) == null) {
                        arrayList.add(str);
                    }
                }
                if (!Checker.isEmpty(arrayList)) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        sQLiteTable.columns.put((String) it.next(), 1);
                    }
                    int insertNewColunms = insertNewColunms(sQLiteDatabase, entityTable.name, arrayList);
                    if (OrmLog.isPrint) {
                        if (insertNewColunms > 0) {
                            OrmLog.i(TAG, "Table [" + entityTable.name + "] add " + insertNewColunms + " new column ： " + arrayList);
                        } else {
                            OrmLog.e(TAG, "Table [" + entityTable.name + "] add " + insertNewColunms + " new column error ： " + arrayList);
                        }
                    }
                }
            }
        }
        return true;
    }

    private static void checkPrimaryKey(Primarykey primarykey) {
        if (primarykey.isAssignedBySystem()) {
            if (FieldUtil.isNumber(primarykey.field.getType())) {
                return;
            }
            throw new RuntimeException(AssignType.AUTO_INCREMENT + " Auto increment primary key must be a number ...\n 错误提示：自增主键必须设置为数字类型");
        }
        if (!primarykey.isAssignedByMyself()) {
            throw new RuntimeException(" Primary key without Assign Type ...\n 错误提示：主键无类型");
        }
        if (String.class == primarykey.field.getType() || FieldUtil.isNumber(primarykey.field.getType())) {
            return;
        }
        throw new RuntimeException(AssignType.BY_MYSELF + " Custom primary key must be string or number ...\n 错误提示：自定义主键值必须为String或者Number类型");
    }

    private boolean createTable(SQLiteDatabase sQLiteDatabase, EntityTable entityTable) {
        return SQLBuilder.buildCreateTable(entityTable).execute(sQLiteDatabase);
    }

    private static EntityTable getEntityTable(String str) {
        return mEntityTableMap.get(str);
    }

    public static String getMapTableName(EntityTable entityTable, EntityTable entityTable2) {
        return getMapTableName(entityTable.name, entityTable2.name);
    }

    public static String getMapTableName(Class cls, Class cls2) {
        return getMapTableName(getTableName(cls), getTableName(cls2));
    }

    public static String getMapTableName(String str, String str2) {
        if (str.compareTo(str2) < 0) {
            return str + "_" + str2;
        }
        return str2 + "_" + str;
    }

    private EntityTable getMappingTable(String str, String str2, String str3) {
        EntityTable entityTable = getEntityTable(this.dbName + str);
        if (entityTable != null) {
            return entityTable;
        }
        EntityTable entityTable2 = new EntityTable();
        entityTable2.name = str;
        entityTable2.pmap = new LinkedHashMap<>();
        entityTable2.pmap.put(str2, null);
        entityTable2.pmap.put(str3, null);
        putEntityTable(this.dbName + str, entityTable2);
        return entityTable2;
    }

    public static EntityTable getTable(Class<?> cls) {
        return getTable(cls, true);
    }

    public static synchronized EntityTable getTable(Class<?> cls, boolean z) {
        EntityTable entityTable;
        synchronized (TableManager.class) {
            entityTable = getEntityTable(cls.getName());
            if (entityTable == null) {
                entityTable = new EntityTable();
                entityTable.claxx = cls;
                entityTable.name = getTableName(cls);
                entityTable.pmap = new LinkedHashMap<>();
                for (Field field : FieldUtil.getAllDeclaredFields(cls)) {
                    if (!FieldUtil.isInvalid(field)) {
                        Column column = (Column) field.getAnnotation(Column.class);
                        Property property = new Property(column != null ? column.value() : field.getName(), field);
                        PrimaryKey primaryKey = (PrimaryKey) field.getAnnotation(PrimaryKey.class);
                        if (primaryKey != null) {
                            entityTable.key = new Primarykey(property, primaryKey.value());
                            checkPrimaryKey(entityTable.key);
                        } else {
                            Mapping mapping = (Mapping) field.getAnnotation(Mapping.class);
                            if (mapping != null) {
                                entityTable.addMapping(new MapProperty(property, mapping.value()));
                            } else {
                                entityTable.pmap.put(property.column, property);
                            }
                        }
                    }
                }
                if (entityTable.key == null) {
                    for (String str : entityTable.pmap.keySet()) {
                        String[] strArr = ID;
                        int length = strArr.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            if (strArr[i].equalsIgnoreCase(str)) {
                                Property property2 = entityTable.pmap.get(str);
                                if (property2.field.getType() == String.class) {
                                    entityTable.pmap.remove(str);
                                    entityTable.key = new Primarykey(property2, AssignType.BY_MYSELF);
                                    break;
                                }
                                if (FieldUtil.isNumber(property2.field.getType())) {
                                    entityTable.pmap.remove(str);
                                    entityTable.key = new Primarykey(property2, AssignType.AUTO_INCREMENT);
                                    break;
                                }
                            }
                            i++;
                        }
                        if (entityTable.key != null) {
                            break;
                        }
                    }
                }
                if (z && entityTable.key == null) {
                    throw new RuntimeException("你必须为[" + entityTable.claxx.getSimpleName() + "]设置主键(you must set the primary key...)\n 提示：在对象的属性上加PrimaryKey注解来设置主键。");
                }
                putEntityTable(cls.getName(), entityTable);
            }
        }
        return entityTable;
    }

    public static EntityTable getTable(Object obj) {
        return getTable(obj.getClass(), true);
    }

    public static String getTableName(Class<?> cls) {
        Table table = (Table) cls.getAnnotation(Table.class);
        return table != null ? table.value() : cls.getName().replaceAll("\\.", "_");
    }

    private void initAllTablesFromSQLite(SQLiteDatabase sQLiteDatabase) {
        synchronized (this.mSqlTableMap) {
            if (Checker.isEmpty(this.mSqlTableMap)) {
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "Initialize SQL table start--------------------->");
                }
                SQLStatement buildTableObtainAll = SQLBuilder.buildTableObtainAll();
                final EntityTable table = getTable(SQLiteTable.class, false);
                Querier.doQuery(sQLiteDatabase, buildTableObtainAll, new Querier.CursorParser() { // from class: com.litesuits.orm.db.TableManager.1
                    @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                    public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                        SQLiteTable sQLiteTable = new SQLiteTable();
                        DataUtil.injectDataToObject(cursor, sQLiteTable, table);
                        ArrayList<String> allColumnsFromSQLite = TableManager.this.getAllColumnsFromSQLite(sQLiteDatabase2, sQLiteTable.name);
                        if (Checker.isEmpty(allColumnsFromSQLite)) {
                            OrmLog.e(TableManager.TAG, "读数据库失败了，开始解析建表语句");
                            allColumnsFromSQLite = TableManager.this.transformSqlToColumns(sQLiteTable.sql);
                        }
                        sQLiteTable.columns = new HashMap<>();
                        Iterator<String> it = allColumnsFromSQLite.iterator();
                        while (it.hasNext()) {
                            sQLiteTable.columns.put(it.next(), 1);
                        }
                        if (OrmLog.isPrint) {
                            OrmLog.i(TableManager.TAG, "Find One SQL Table: " + sQLiteTable);
                            OrmLog.i(TableManager.TAG, "Table Column: " + allColumnsFromSQLite);
                        }
                        TableManager.this.mSqlTableMap.put(sQLiteTable.name, sQLiteTable);
                    }
                });
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "Initialize SQL table end  ---------------------> " + this.mSqlTableMap.size());
                }
            }
        }
    }

    private int insertNewColunms(SQLiteDatabase sQLiteDatabase, final String str, final List<String> list) {
        Integer num = !Checker.isEmpty(list) ? (Integer) Transaction.execute(sQLiteDatabase, new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.TableManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Integer doTransaction(SQLiteDatabase sQLiteDatabase2) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    SQLBuilder.buildAddColumnSql(str, (String) it.next()).execute(sQLiteDatabase2);
                }
                return Integer.valueOf(list.size());
            }
        }) : null;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private static EntityTable putEntityTable(String str, EntityTable entityTable) {
        return mEntityTableMap.put(str, entityTable);
    }

    private void putNewSqlTableIntoMap(EntityTable entityTable) {
        if (OrmLog.isPrint) {
            OrmLog.i(TAG, "Table [" + entityTable.name + "] Create Success");
        }
        SQLiteTable sQLiteTable = new SQLiteTable();
        sQLiteTable.name = entityTable.name;
        sQLiteTable.columns = new HashMap<>();
        if (entityTable.key != null) {
            sQLiteTable.columns.put(entityTable.key.column, 1);
        }
        if (entityTable.pmap != null) {
            Iterator<String> it = entityTable.pmap.keySet().iterator();
            while (it.hasNext()) {
                sQLiteTable.columns.put(it.next(), 1);
            }
        }
        sQLiteTable.isTableChecked = true;
        this.mSqlTableMap.put(sQLiteTable.name, sQLiteTable);
    }

    public synchronized void checkOrCreateMappingTable(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3) {
        EntityTable mappingTable = getMappingTable(str, str2, str3);
        if (!checkExistAndColumns(sQLiteDatabase, mappingTable) && createTable(sQLiteDatabase, mappingTable)) {
            putNewSqlTableIntoMap(mappingTable);
        }
    }

    public synchronized EntityTable checkOrCreateTable(SQLiteDatabase sQLiteDatabase, Class cls) {
        EntityTable table;
        table = getTable((Class<?>) cls);
        if (!checkExistAndColumns(sQLiteDatabase, table) && createTable(sQLiteDatabase, table)) {
            putNewSqlTableIntoMap(table);
        }
        return table;
    }

    public EntityTable checkOrCreateTable(SQLiteDatabase sQLiteDatabase, Object obj) {
        return checkOrCreateTable(sQLiteDatabase, (Class) obj.getClass());
    }

    public void clearSqlTable() {
        synchronized (this.mSqlTableMap) {
            this.mSqlTableMap.clear();
        }
    }

    public ArrayList<String> getAllColumnsFromSQLite(SQLiteDatabase sQLiteDatabase, String str) {
        final EntityTable table = getTable(SQLiteColumn.class, false);
        final ArrayList<String> arrayList = new ArrayList<>();
        Querier.doQuery(sQLiteDatabase, SQLBuilder.buildColumnsObtainAll(str), new Querier.CursorParser() { // from class: com.litesuits.orm.db.TableManager.3
            @Override // com.litesuits.orm.db.assit.Querier.CursorParser
            public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                SQLiteColumn sQLiteColumn = new SQLiteColumn();
                DataUtil.injectDataToObject(cursor, sQLiteColumn, table);
                arrayList.add(sQLiteColumn.name);
            }
        });
        return arrayList;
    }

    public void initSqlTable(SQLiteDatabase sQLiteDatabase) {
        initAllTablesFromSQLite(sQLiteDatabase);
    }

    public boolean isSQLMapTableCreated(String str, String str2) {
        return this.mSqlTableMap.get(getMapTableName(str, str2)) != null;
    }

    public boolean isSQLTableCreated(String str) {
        return this.mSqlTableMap.get(str) != null;
    }

    public void release() {
        clearSqlTable();
        mEntityTableMap.clear();
    }

    public ArrayList<String> transformSqlToColumns(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(SQLBuilder.PARENTHESES_LEFT);
        int lastIndexOf = str.lastIndexOf(SQLBuilder.PARENTHESES_RIGHT);
        if (indexOf <= 0 || lastIndexOf <= 0) {
            return null;
        }
        String substring = str.substring(indexOf + 1, lastIndexOf);
        String[] split = substring.split(",");
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : split) {
            String trim = str2.trim();
            int indexOf2 = trim.indexOf(SQLBuilder.BLANK);
            if (indexOf2 > 0) {
                trim = trim.substring(0, indexOf2);
            }
            arrayList.add(trim);
        }
        OrmLog.e(TAG, "降级：语义分析表结构（" + arrayList.toString() + " , Origin SQL is: " + substring);
        return arrayList;
    }
}
