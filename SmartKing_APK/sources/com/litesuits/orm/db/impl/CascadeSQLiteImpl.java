package com.litesuits.orm.db.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.Querier;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.litesuits.orm.db.assit.SQLStatement;
import com.litesuits.orm.db.assit.Transaction;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.db.model.EntityTable;
import com.litesuits.orm.db.model.MapProperty;
import com.litesuits.orm.db.model.Property;
import com.litesuits.orm.db.model.RelationKey;
import com.litesuits.orm.db.utils.ClassUtil;
import com.litesuits.orm.db.utils.DataUtil;
import com.litesuits.orm.db.utils.FieldUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class CascadeSQLiteImpl extends LiteOrm {
    public static final String TAG = "CascadeSQLiteImpl";
    public static final int TYPE_DELETE = 3;
    public static final int TYPE_INSERT = 1;
    public static final int TYPE_UPDATE = 2;

    /* JADX INFO: Access modifiers changed from: protected */
    public CascadeSQLiteImpl(LiteOrm liteOrm) {
        super(liteOrm);
    }

    private CascadeSQLiteImpl(DataBaseConfig dataBaseConfig) {
        super(dataBaseConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkTableAndDeleteRecursive(Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        if (this.mTableManager.isSQLTableCreated(TableManager.getTable(obj).name)) {
            return deleteRecursive(SQLBuilder.buildDeleteSql(obj), obj, sQLiteDatabase, hashMap);
        }
        return -1;
    }

    private <T> ArrayList<T> checkTableAndQuery(final Class<T> cls, QueryBuilder queryBuilder) {
        acquireReference();
        final ArrayList<T> arrayList = new ArrayList<>();
        try {
            try {
                final EntityTable table = TableManager.getTable(cls, false);
                if (this.mTableManager.isSQLTableCreated(table.name)) {
                    final HashMap<String, Object> hashMap = new HashMap<>();
                    HashMap<String, Integer> hashMap2 = new HashMap<>();
                    SQLiteDatabase readableDatabase = this.mHelper.getReadableDatabase();
                    Querier.doQuery(readableDatabase, queryBuilder.createStatement(), new Querier.CursorParser() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.5
                        @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                        public void parseEachCursor(SQLiteDatabase sQLiteDatabase, Cursor cursor) throws Exception {
                            Object newInstance = ClassUtil.newInstance(cls);
                            DataUtil.injectDataToObject(cursor, newInstance, table);
                            arrayList.add(newInstance);
                            hashMap.put(table.name + FieldUtil.get(table.key.field, newInstance), newInstance);
                        }
                    });
                    Iterator<T> it = arrayList.iterator();
                    while (it.hasNext()) {
                        queryForMappingRecursive(it.next(), readableDatabase, hashMap2, hashMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        } finally {
            releaseReference();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long checkTableAndSaveRecursive(Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        this.mTableManager.checkOrCreateTable(sQLiteDatabase, obj);
        return insertRecursive(SQLBuilder.buildReplaceSql(obj), obj, sQLiteDatabase, hashMap);
    }

    private <T> int deleteCollectionIfTableHasCreated(final Collection<T> collection) {
        Integer num;
        if (Checker.isEmpty((Collection<?>) collection)) {
            return -1;
        }
        final Iterator<T> it = collection.iterator();
        final T next = it.next();
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(next).name) || (num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                HashMap hashMap = new HashMap();
                SQLStatement buildDeleteSql = SQLBuilder.buildDeleteSql(next);
                CascadeSQLiteImpl.this.deleteRecursive(buildDeleteSql, next, sQLiteDatabase, hashMap);
                while (it.hasNext()) {
                    Object next2 = it.next();
                    buildDeleteSql.bindArgs = CascadeSQLiteImpl.getDeleteStatementArgs(next2);
                    CascadeSQLiteImpl.this.deleteRecursive(buildDeleteSql, next2, sQLiteDatabase, hashMap);
                }
                return Integer.valueOf(collection.size());
            }
        })) == null) {
            return -1;
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int deleteRecursive(SQLStatement sQLStatement, Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        EntityTable table = TableManager.getTable(obj);
        Object obj2 = FieldUtil.get(table.key.field, obj);
        if (hashMap.get(table.name + obj2) != null) {
            return -1;
        }
        int execDelete = sQLStatement.execDelete(sQLiteDatabase);
        hashMap.put(table.name + obj2, 1);
        handleMapping(obj2, obj, sQLiteDatabase, false, hashMap);
        return execDelete;
    }

    public static Object[] getDeleteStatementArgs(Object obj) throws IllegalAccessException {
        EntityTable table = TableManager.getTable(obj);
        int i = 0;
        if (table.key != null) {
            return new String[]{String.valueOf(FieldUtil.get(table.key.field, obj))};
        }
        if (Checker.isEmpty(table.pmap)) {
            return null;
        }
        Object[] objArr = new Object[table.pmap.size()];
        Iterator<Property> it = table.pmap.values().iterator();
        while (it.hasNext()) {
            objArr[i] = FieldUtil.get(it.next().field, obj);
            i++;
        }
        return objArr;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private long handleEntityRecursive(int i, SQLStatement sQLStatement, Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        Object obj2;
        EntityTable table = TableManager.getTable(obj);
        Object obj3 = FieldUtil.get(table.key.field, obj);
        long j = -1;
        if (hashMap.get(table.name + obj3) != null) {
            return -1L;
        }
        switch (i) {
            case 1:
                j = sQLStatement.execInsert(sQLiteDatabase, obj);
                obj2 = FieldUtil.get(table.key.field, obj);
                break;
            case 2:
                j = sQLStatement.execUpdate(sQLiteDatabase);
                obj2 = obj3;
                break;
            case 3:
                j = sQLStatement.execDelete(sQLiteDatabase);
                obj2 = obj3;
                break;
            default:
                obj2 = obj3;
                break;
        }
        hashMap.put(table.name + obj2, 1);
        handleMapping(obj2, obj, sQLiteDatabase, i != 3, hashMap);
        return j;
    }

    private void handleMapToMany(EntityTable entityTable, EntityTable entityTable2, Object obj, Collection collection, SQLiteDatabase sQLiteDatabase, boolean z, HashMap<String, Integer> hashMap) throws Exception {
        if (collection != null) {
            for (Object obj2 : collection) {
                if (obj2 != null) {
                    if (z) {
                        checkTableAndSaveRecursive(obj2, sQLiteDatabase, hashMap);
                    } else {
                        checkTableAndDeleteRecursive(obj2, sQLiteDatabase, hashMap);
                    }
                }
            }
        }
        String mapTableName = TableManager.getMapTableName(entityTable, entityTable2);
        this.mTableManager.checkOrCreateMappingTable(sQLiteDatabase, mapTableName, entityTable.name, entityTable2.name);
        SQLBuilder.buildMappingDeleteSql(mapTableName, obj, entityTable).execDelete(sQLiteDatabase);
        if (!z || Checker.isEmpty((Collection<?>) collection)) {
            return;
        }
        ArrayList<SQLStatement> buildMappingToManySql = SQLBuilder.buildMappingToManySql(obj, entityTable, entityTable2, collection);
        if (Checker.isEmpty(buildMappingToManySql)) {
            return;
        }
        Iterator<SQLStatement> it = buildMappingToManySql.iterator();
        while (it.hasNext()) {
            it.next().execInsert(sQLiteDatabase);
        }
    }

    private void handleMapToOne(EntityTable entityTable, EntityTable entityTable2, Object obj, Object obj2, SQLiteDatabase sQLiteDatabase, boolean z, HashMap<String, Integer> hashMap) throws Exception {
        SQLStatement buildMappingToOneSql;
        if (obj2 != null) {
            if (z) {
                checkTableAndSaveRecursive(obj2, sQLiteDatabase, hashMap);
            } else {
                checkTableAndDeleteRecursive(obj2, sQLiteDatabase, hashMap);
            }
        }
        String mapTableName = TableManager.getMapTableName(entityTable, entityTable2);
        this.mTableManager.checkOrCreateMappingTable(sQLiteDatabase, mapTableName, entityTable.name, entityTable2.name);
        SQLBuilder.buildMappingDeleteSql(mapTableName, obj, entityTable).execDelete(sQLiteDatabase);
        if (!z || obj2 == null || (buildMappingToOneSql = SQLBuilder.buildMappingToOneSql(mapTableName, obj, FieldUtil.get(entityTable2.key.field, obj2), entityTable, entityTable2)) == null) {
            return;
        }
        buildMappingToOneSql.execInsert(sQLiteDatabase);
    }

    private void handleMapping(Object obj, Object obj2, SQLiteDatabase sQLiteDatabase, boolean z, HashMap<String, Integer> hashMap) throws Exception {
        EntityTable table = TableManager.getTable(obj2);
        if (table.mappingList != null) {
            Iterator<MapProperty> it = table.mappingList.iterator();
            while (it.hasNext()) {
                MapProperty next = it.next();
                if (next.isToOne()) {
                    handleMapToOne(table, TableManager.getTable(next.field.getType()), obj, FieldUtil.get(next.field, obj2), sQLiteDatabase, z, hashMap);
                } else if (next.isToMany()) {
                    Object obj3 = FieldUtil.get(next.field, obj2);
                    if (ClassUtil.isCollection(next.field.getType())) {
                        handleMapToMany(table, TableManager.getTable(FieldUtil.getGenericType(next.field)), obj, (Collection) obj3, sQLiteDatabase, z, hashMap);
                    } else {
                        if (!ClassUtil.isArray(next.field.getType())) {
                            throw new RuntimeException("OneToMany and ManyToMany Relation, you must use collection or array object");
                        }
                        handleMapToMany(table, TableManager.getTable(FieldUtil.getComponentType(next.field)), obj, obj3 != null ? Arrays.asList((Object[]) obj3) : null, sQLiteDatabase, z, hashMap);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private <T> int insertCollection(final Collection<T> collection, final ConflictAlgorithm conflictAlgorithm) {
        Integer num;
        if (Checker.isEmpty((Collection<?>) collection) || (num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                HashMap hashMap = new HashMap();
                Iterator it = collection.iterator();
                Object next = it.next();
                SQLStatement buildInsertSql = SQLBuilder.buildInsertSql(next, conflictAlgorithm);
                CascadeSQLiteImpl.this.mTableManager.checkOrCreateTable(sQLiteDatabase, next);
                CascadeSQLiteImpl.this.insertRecursive(buildInsertSql, next, sQLiteDatabase, hashMap);
                while (it.hasNext()) {
                    Object next2 = it.next();
                    buildInsertSql.bindArgs = SQLBuilder.buildInsertSqlArgsOnly(next2);
                    CascadeSQLiteImpl.this.insertRecursive(buildInsertSql, next2, sQLiteDatabase, hashMap);
                }
                return Integer.valueOf(collection.size());
            }
        })) == null) {
            return -1;
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long insertRecursive(SQLStatement sQLStatement, Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        EntityTable table = TableManager.getTable(obj);
        if (hashMap.get(table.name + FieldUtil.get(table.key.field, obj)) != null) {
            return -1L;
        }
        long execInsert = sQLStatement.execInsert(sQLiteDatabase, obj);
        Object obj2 = FieldUtil.get(table.key.field, obj);
        hashMap.put(table.name + obj2, 1);
        handleMapping(obj2, obj, sQLiteDatabase, true, hashMap);
        return execInsert;
    }

    public static synchronized LiteOrm newInstance(DataBaseConfig dataBaseConfig) {
        CascadeSQLiteImpl cascadeSQLiteImpl;
        synchronized (CascadeSQLiteImpl.class) {
            cascadeSQLiteImpl = new CascadeSQLiteImpl(dataBaseConfig);
        }
        return cascadeSQLiteImpl;
    }

    private void queryForMappingRecursive(Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap, HashMap<String, Object> hashMap2) throws IllegalAccessException, InstantiationException {
        EntityTable table = TableManager.getTable(obj);
        Object assignedKeyObject = FieldUtil.getAssignedKeyObject(table.key, obj);
        String str = table.name + assignedKeyObject;
        if (hashMap.get(str) == null) {
            hashMap.put(str, 1);
            if (table.mappingList != null) {
                Iterator<MapProperty> it = table.mappingList.iterator();
                while (it.hasNext()) {
                    MapProperty next = it.next();
                    if (next.isToOne()) {
                        queryMapToOne(table, assignedKeyObject, obj, next.field, sQLiteDatabase, hashMap, hashMap2);
                    } else if (next.isToMany()) {
                        queryMapToMany(table, assignedKeyObject, obj, next.field, sQLiteDatabase, hashMap, hashMap2);
                    }
                }
            }
        }
    }

    private void queryMapToMany(EntityTable entityTable, Object obj, Object obj2, Field field, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap, final HashMap<String, Object> hashMap2) throws IllegalAccessException, InstantiationException {
        Class<?> componentType;
        if (Collection.class.isAssignableFrom(field.getType())) {
            componentType = FieldUtil.getGenericType(field);
        } else {
            if (!field.getType().isArray()) {
                throw new RuntimeException("OneToMany and ManyToMany Relation, you must use collection or array object");
            }
            componentType = FieldUtil.getComponentType(field);
        }
        final Class<?> cls = componentType;
        final EntityTable table = TableManager.getTable(cls);
        if (this.mTableManager.isSQLMapTableCreated(entityTable.name, table.name)) {
            SQLStatement buildQueryRelationSql = SQLBuilder.buildQueryRelationSql(entityTable, table, obj);
            final ArrayList arrayList = new ArrayList();
            Querier.doQuery(sQLiteDatabase, buildQueryRelationSql, new Querier.CursorParser() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.7
                @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                    arrayList.add(cursor.getString(cursor.getColumnIndex(table.name)));
                }
            });
            if (Checker.isEmpty(arrayList)) {
                return;
            }
            final ArrayList arrayList2 = new ArrayList();
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                Object obj3 = hashMap2.get(table.name + ((String) arrayList.get(size)));
                if (obj3 != null) {
                    arrayList2.add(obj3);
                    arrayList.remove(size);
                }
            }
            int i = 0;
            int i2 = 0;
            while (i < arrayList.size()) {
                int i3 = i2 + 1;
                int i4 = i3 * 999;
                List subList = arrayList.subList(i, Math.min(arrayList.size(), i4));
                final EntityTable entityTable2 = table;
                Querier.doQuery(sQLiteDatabase, QueryBuilder.create(cls).whereIn(table.key.column, subList.toArray(new String[subList.size()])).createStatement(), new Querier.CursorParser() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.8
                    @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                    public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                        Object newInstance = ClassUtil.newInstance(cls);
                        DataUtil.injectDataToObject(cursor, newInstance, entityTable2);
                        arrayList2.add(newInstance);
                        hashMap2.put(entityTable2.name + FieldUtil.get(entityTable2.key.field, newInstance), newInstance);
                    }
                });
                i2 = i3;
                arrayList = arrayList;
                table = table;
                i = i4;
            }
            if (Checker.isEmpty(arrayList2)) {
                return;
            }
            if (Collection.class.isAssignableFrom(field.getType())) {
                Collection collection = (Collection) ClassUtil.newCollectionForField(field);
                collection.addAll(arrayList2);
                FieldUtil.set(field, obj2, collection);
            } else {
                if (!field.getType().isArray()) {
                    throw new RuntimeException("OneToMany and ManyToMany Relation, you must use collection or array object");
                }
                FieldUtil.set(field, obj2, arrayList2.toArray((Object[]) ClassUtil.newArray(cls, arrayList2.size())));
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                queryForMappingRecursive(it.next(), sQLiteDatabase, hashMap, hashMap2);
            }
        }
    }

    private void queryMapToOne(final EntityTable entityTable, Object obj, Object obj2, Field field, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap, HashMap<String, Object> hashMap2) throws IllegalAccessException, InstantiationException {
        final EntityTable table = TableManager.getTable(field.getType());
        if (this.mTableManager.isSQLMapTableCreated(entityTable.name, table.name)) {
            SQLStatement buildQueryRelationSql = SQLBuilder.buildQueryRelationSql(entityTable, table, obj);
            final RelationKey relationKey = new RelationKey();
            Querier.doQuery(sQLiteDatabase, buildQueryRelationSql, new Querier.CursorParser() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.6
                @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                    relationKey.key1 = cursor.getString(cursor.getColumnIndex(entityTable.name));
                    relationKey.key2 = cursor.getString(cursor.getColumnIndex(table.name));
                    stopParse();
                }
            });
            if (relationKey.isOK()) {
                String str = table.name + relationKey.key2;
                Object obj3 = hashMap2.get(str);
                if (obj3 == null) {
                    obj3 = SQLBuilder.buildQueryMapEntitySql(table, relationKey.key2).queryOneEntity(sQLiteDatabase, table.claxx);
                    hashMap2.put(str, obj3);
                }
                if (obj3 != null) {
                    FieldUtil.set(field, obj2, obj3);
                    queryForMappingRecursive(obj3, sQLiteDatabase, hashMap, hashMap2);
                }
            }
        }
    }

    private <T> int saveCollection(final Collection<T> collection) {
        Integer num;
        if (Checker.isEmpty((Collection<?>) collection) || (num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                HashMap hashMap = new HashMap();
                Iterator it = collection.iterator();
                Object next = it.next();
                SQLStatement buildReplaceSql = SQLBuilder.buildReplaceSql(next);
                CascadeSQLiteImpl.this.mTableManager.checkOrCreateTable(sQLiteDatabase, next);
                CascadeSQLiteImpl.this.insertRecursive(buildReplaceSql, next, sQLiteDatabase, hashMap);
                while (it.hasNext()) {
                    Object next2 = it.next();
                    buildReplaceSql.bindArgs = SQLBuilder.buildInsertSqlArgsOnly(next2);
                    CascadeSQLiteImpl.this.insertRecursive(buildReplaceSql, next2, sQLiteDatabase, hashMap);
                }
                return Integer.valueOf(collection.size());
            }
        })) == null) {
            return -1;
        }
        return num.intValue();
    }

    private <T> int updateCollection(final Collection<T> collection, final ColumnsValue columnsValue, final ConflictAlgorithm conflictAlgorithm) {
        Integer num;
        if (Checker.isEmpty((Collection<?>) collection) || (num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                HashMap hashMap = new HashMap();
                Iterator it = collection.iterator();
                Object next = it.next();
                SQLStatement buildUpdateSql = SQLBuilder.buildUpdateSql(next, columnsValue, conflictAlgorithm);
                CascadeSQLiteImpl.this.mTableManager.checkOrCreateTable(sQLiteDatabase, next);
                CascadeSQLiteImpl.this.updateRecursive(buildUpdateSql, next, sQLiteDatabase, hashMap);
                while (it.hasNext()) {
                    Object next2 = it.next();
                    buildUpdateSql.bindArgs = SQLBuilder.buildUpdateSqlArgsOnly(next2, columnsValue);
                    CascadeSQLiteImpl.this.updateRecursive(buildUpdateSql, next2, sQLiteDatabase, hashMap);
                }
                return Integer.valueOf(collection.size());
            }
        })) == null) {
            return -1;
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int updateRecursive(SQLStatement sQLStatement, Object obj, SQLiteDatabase sQLiteDatabase, HashMap<String, Integer> hashMap) throws Exception {
        EntityTable table = TableManager.getTable(obj);
        if (hashMap.get(table.name + FieldUtil.get(table.key.field, obj)) != null) {
            return -1;
        }
        int execUpdate = sQLStatement.execUpdate(sQLiteDatabase);
        Object obj2 = FieldUtil.get(table.key.field, obj);
        hashMap.put(table.name + obj2, 1);
        handleMapping(obj2, obj, sQLiteDatabase, true, hashMap);
        return execUpdate;
    }

    @Override // com.litesuits.orm.LiteOrm
    public LiteOrm cascade() {
        return this;
    }

    @Override // com.litesuits.orm.db.DataBase
    public int delete(WhereBuilder whereBuilder) {
        acquireReference();
        try {
            try {
                deleteCollectionIfTableHasCreated(query(QueryBuilder.create(whereBuilder.getTableClass()).columns(new String[]{TableManager.getTable((Class<?>) whereBuilder.getTableClass()).key.column}).where(whereBuilder)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseReference();
            return -1;
        } catch (Throwable th) {
            releaseReference();
            throw th;
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Class<T> cls) {
        return deleteAll(cls);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Class<T> cls, long j, long j2, String str) {
        acquireReference();
        try {
            if (j < 0 || j2 < j) {
                throw new RuntimeException("start must >=0 and smaller than end");
            }
            if (j != 0) {
                j--;
            }
            long j3 = j2 == 2147483647L ? -1L : j2 - j;
            EntityTable table = TableManager.getTable((Class<?>) cls);
            return delete((Collection) query(QueryBuilder.create(cls).limit(j + "," + j3).appendOrderAscBy(str).columns(new String[]{table.key.column})));
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Class<T> cls, WhereBuilder whereBuilder) {
        acquireReference();
        try {
            try {
                delete((Collection) query(QueryBuilder.create(cls).columns(new String[]{TableManager.getTable((Class<?>) cls).key.column}).where(whereBuilder)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseReference();
            return -1;
        } catch (Throwable th) {
            releaseReference();
            throw th;
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public int delete(final Object obj) {
        acquireReference();
        try {
            try {
                Integer num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.litesuits.orm.db.assit.Transaction.Worker
                    public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                        return Integer.valueOf(CascadeSQLiteImpl.this.checkTableAndDeleteRecursive(obj, sQLiteDatabase, new HashMap()));
                    }
                });
                if (num != null) {
                    return num.intValue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseReference();
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Collection<T> collection) {
        acquireReference();
        try {
            try {
                return deleteCollectionIfTableHasCreated(collection);
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int deleteAll(Class<T> cls) {
        acquireReference();
        try {
            return delete((Collection) query(QueryBuilder.create(cls).columns(new String[]{TableManager.getTable((Class<?>) cls).key.column})));
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int insert(Collection<T> collection) {
        return insert((Collection) collection, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int insert(Collection<T> collection, ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            try {
                return insertCollection(collection, conflictAlgorithm);
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public long insert(Object obj) {
        return insert(obj, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public long insert(final Object obj, final ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            Long l = (Long) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Long>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.litesuits.orm.db.assit.Transaction.Worker
                public Long doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                    CascadeSQLiteImpl.this.mTableManager.checkOrCreateTable(sQLiteDatabase, obj);
                    return Long.valueOf(CascadeSQLiteImpl.this.insertRecursive(SQLBuilder.buildInsertSql(obj, conflictAlgorithm), obj, sQLiteDatabase, new HashMap()));
                }
            });
            return l == null ? -1L : l.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> ArrayList<T> query(QueryBuilder<T> queryBuilder) {
        return checkTableAndQuery(queryBuilder.getQueryClass(), queryBuilder);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> ArrayList<T> query(Class<T> cls) {
        return checkTableAndQuery(cls, new QueryBuilder(cls));
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> T queryById(long j, Class<T> cls) {
        return (T) queryById(String.valueOf(j), cls);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> T queryById(String str, Class<T> cls) {
        ArrayList<T> checkTableAndQuery = checkTableAndQuery(cls, new QueryBuilder(cls).whereEquals(TableManager.getTable((Class<?>) cls).key.column, String.valueOf(str)));
        if (Checker.isEmpty(checkTableAndQuery)) {
            return null;
        }
        return checkTableAndQuery.get(0);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int save(Collection<T> collection) {
        acquireReference();
        try {
            return saveCollection(collection);
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public long save(final Object obj) {
        acquireReference();
        try {
            Long l = (Long) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Long>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.litesuits.orm.db.assit.Transaction.Worker
                public Long doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                    return Long.valueOf(CascadeSQLiteImpl.this.checkTableAndSaveRecursive(obj, sQLiteDatabase, new HashMap()));
                }
            });
            return l == null ? -1L : l.longValue();
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.LiteOrm
    public LiteOrm single() {
        if (this.otherDatabase == null) {
            this.otherDatabase = new SingleSQLiteImpl(this);
        }
        return this.otherDatabase;
    }

    @Override // com.litesuits.orm.db.DataBase
    public int update(Object obj) {
        return update(obj, (ColumnsValue) null, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public int update(final Object obj, final ColumnsValue columnsValue, final ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            Integer num = (Integer) Transaction.execute(this.mHelper.getWritableDatabase(), new Transaction.Worker<Integer>() { // from class: com.litesuits.orm.db.impl.CascadeSQLiteImpl.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.litesuits.orm.db.assit.Transaction.Worker
                public Integer doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception {
                    HashMap hashMap = new HashMap();
                    SQLStatement buildUpdateSql = SQLBuilder.buildUpdateSql(obj, columnsValue, conflictAlgorithm);
                    CascadeSQLiteImpl.this.mTableManager.checkOrCreateTable(sQLiteDatabase, obj);
                    return Integer.valueOf(CascadeSQLiteImpl.this.updateRecursive(buildUpdateSql, obj, sQLiteDatabase, hashMap));
                }
            });
            return num == null ? -1 : num.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public int update(Object obj, ConflictAlgorithm conflictAlgorithm) {
        return update(obj, (ColumnsValue) null, conflictAlgorithm);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int update(Collection<T> collection) {
        return update((Collection) collection, (ColumnsValue) null, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int update(Collection<T> collection, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            try {
                return updateCollection(collection, columnsValue, conflictAlgorithm);
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int update(Collection<T> collection, ConflictAlgorithm conflictAlgorithm) {
        return update((Collection) collection, (ColumnsValue) null, conflictAlgorithm);
    }
}
