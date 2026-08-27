package com.litesuits.orm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteClosable;
import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.db.DataBase;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.CollSpliter;
import com.litesuits.orm.db.assit.Querier;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.litesuits.orm.db.assit.SQLStatement;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.impl.CascadeSQLiteImpl;
import com.litesuits.orm.db.impl.SingleSQLiteImpl;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.db.model.EntityTable;
import com.litesuits.orm.db.model.MapProperty;
import com.litesuits.orm.db.model.RelationKey;
import com.litesuits.orm.db.utils.ClassUtil;
import com.litesuits.orm.db.utils.DataUtil;
import com.litesuits.orm.db.utils.FieldUtil;
import com.litesuits.orm.log.OrmLog;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class LiteOrm extends SQLiteClosable implements DataBase {
    public static final String TAG = "LiteOrm";
    protected DataBaseConfig mConfig;
    protected SQLiteHelper mHelper;
    protected TableManager mTableManager;
    protected LiteOrm otherDatabase;

    /* JADX INFO: Access modifiers changed from: protected */
    public LiteOrm(LiteOrm liteOrm) {
        this.mHelper = liteOrm.mHelper;
        this.mConfig = liteOrm.mConfig;
        this.mTableManager = liteOrm.mTableManager;
        this.otherDatabase = liteOrm;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LiteOrm(DataBaseConfig dataBaseConfig) {
        dataBaseConfig.context = dataBaseConfig.context.getApplicationContext();
        if (dataBaseConfig.dbName == null) {
            dataBaseConfig.dbName = DataBaseConfig.DEFAULT_DB_NAME;
        }
        if (dataBaseConfig.dbVersion <= 0) {
            dataBaseConfig.dbVersion = 1;
        }
        this.mConfig = dataBaseConfig;
        setDebugged(dataBaseConfig.debugged);
        openOrCreateDatabase();
    }

    private void initDatabasePath(String str) {
        OrmLog.i(TAG, "create  database path: " + str);
        String path = this.mConfig.context.getDatabasePath(this.mConfig.dbName).getPath();
        OrmLog.i(TAG, "context database path: " + path);
        File parentFile = new File(path).getParentFile();
        if (parentFile == null || parentFile.exists()) {
            return;
        }
        boolean mkdirs = parentFile.mkdirs();
        OrmLog.i(TAG, "create database, parent file mkdirs: " + mkdirs + "  path:" + parentFile.getAbsolutePath());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <E, T> boolean keepMapping(Collection<E> collection, Collection<T> collection2) throws IllegalAccessException, InstantiationException {
        Object obj;
        Object obj2;
        Class cls = collection.iterator().next().getClass();
        Class cls2 = collection2.iterator().next().getClass();
        EntityTable table = TableManager.getTable((Class<?>) cls);
        EntityTable table2 = TableManager.getTable((Class<?>) cls2);
        if (table.mappingList == null) {
            return false;
        }
        Iterator<MapProperty> it = table.mappingList.iterator();
        while (it.hasNext()) {
            MapProperty next = it.next();
            Class type = next.field.getType();
            if (next.isToMany()) {
                if (ClassUtil.isCollection(type)) {
                    type = FieldUtil.getGenericType(next.field);
                } else {
                    if (!type.isArray()) {
                        throw new RuntimeException("OneToMany and ManyToMany Relation, Must use collection or array object");
                    }
                    type = FieldUtil.getComponentType(next.field);
                }
            }
            if (type == cls2) {
                ArrayList arrayList = new ArrayList();
                HashMap hashMap = new HashMap();
                for (E e : collection) {
                    if (e != null && (obj2 = FieldUtil.get(table.key.field, e)) != null) {
                        arrayList.add(obj2.toString());
                        hashMap.put(obj2.toString(), e);
                    }
                }
                ArrayList<RelationKey> queryRelation = queryRelation(cls, cls2, arrayList);
                if (!Checker.isEmpty(queryRelation)) {
                    HashMap hashMap2 = new HashMap();
                    for (T t : collection2) {
                        if (t != null && (obj = FieldUtil.get(table2.key.field, t)) != null) {
                            hashMap2.put(obj.toString(), t);
                        }
                    }
                    HashMap hashMap3 = new HashMap();
                    Iterator<RelationKey> it2 = queryRelation.iterator();
                    while (it2.hasNext()) {
                        RelationKey next2 = it2.next();
                        Object obj3 = hashMap.get(next2.key1);
                        Object obj4 = hashMap2.get(next2.key2);
                        if (obj3 != null && obj4 != null) {
                            if (next.isToMany()) {
                                ArrayList arrayList2 = (ArrayList) hashMap3.get(obj3);
                                ArrayList arrayList3 = arrayList2;
                                if (arrayList2 == null) {
                                    ArrayList arrayList4 = new ArrayList();
                                    hashMap3.put(obj3, arrayList4);
                                    arrayList3 = arrayList4;
                                }
                                arrayList3.add(obj4);
                            } else {
                                FieldUtil.set(next.field, obj3, obj4);
                            }
                        }
                    }
                    if (Checker.isEmpty(hashMap3)) {
                        return true;
                    }
                    for (Map.Entry entry : hashMap3.entrySet()) {
                        Object key = entry.getKey();
                        Collection<? extends E> collection3 = (Collection) entry.getValue();
                        if (ClassUtil.isCollection(type)) {
                            Collection collection4 = (Collection) FieldUtil.get(next.field, key);
                            if (collection4 == null) {
                                FieldUtil.set(next.field, key, collection3);
                            } else {
                                collection4.addAll(collection3);
                            }
                        } else if (ClassUtil.isArray(type)) {
                            Object[] objArr = (Object[]) ClassUtil.newArray(type, collection3.size());
                            collection3.toArray(objArr);
                            Object[] objArr2 = (Object[]) FieldUtil.get(next.field, key);
                            if (objArr2 == null) {
                                FieldUtil.set(next.field, key, objArr);
                            } else {
                                FieldUtil.set(next.field, key, DataUtil.concat(objArr2, objArr));
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static LiteOrm newCascadeInstance(Context context, String str) {
        return newCascadeInstance(new DataBaseConfig(context, str));
    }

    public static synchronized LiteOrm newCascadeInstance(DataBaseConfig dataBaseConfig) {
        LiteOrm newInstance;
        synchronized (LiteOrm.class) {
            newInstance = CascadeSQLiteImpl.newInstance(dataBaseConfig);
        }
        return newInstance;
    }

    public static LiteOrm newSingleInstance(Context context, String str) {
        return newSingleInstance(new DataBaseConfig(context, str));
    }

    public static synchronized LiteOrm newSingleInstance(DataBaseConfig dataBaseConfig) {
        LiteOrm newInstance;
        synchronized (LiteOrm.class) {
            newInstance = SingleSQLiteImpl.newInstance(dataBaseConfig);
        }
        return newInstance;
    }

    public static int releaseMemory() {
        return SQLiteDatabase.releaseMemory();
    }

    public abstract LiteOrm cascade();

    @Override // android.database.sqlite.SQLiteClosable, java.io.Closeable, java.lang.AutoCloseable, com.litesuits.orm.db.DataBase
    public synchronized void close() {
        releaseReference();
    }

    @Override // com.litesuits.orm.db.DataBase
    public SQLStatement createSQLStatement(String str, Object[] objArr) {
        return new SQLStatement(str, objArr);
    }

    @Override // com.litesuits.orm.db.DataBase
    public boolean deleteDatabase() {
        String path = this.mHelper.getWritableDatabase().getPath();
        justRelease();
        OrmLog.i(TAG, "data has cleared. delete Database path: " + path);
        return deleteDatabase(new File(path));
    }

    @Override // com.litesuits.orm.db.DataBase
    public boolean deleteDatabase(File file) {
        acquireReference();
        try {
            if (file == null) {
                throw new IllegalArgumentException("file must not be null");
            }
            boolean delete = file.delete() | new File(file.getPath() + "-journal").delete() | new File(file.getPath() + "-shm").delete() | new File(file.getPath() + "-wal").delete();
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                final String str = file.getName() + "-mj";
                boolean z = delete;
                for (File file2 : parentFile.listFiles(new FileFilter() { // from class: com.litesuits.orm.LiteOrm.2
                    @Override // java.io.FileFilter
                    public boolean accept(File file3) {
                        return file3.getName().startsWith(str);
                    }
                })) {
                    z |= file2.delete();
                }
                delete = z;
            }
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public boolean dropTable(Class<?> cls) {
        return dropTable(TableManager.getTable(cls, false).name);
    }

    @Override // com.litesuits.orm.db.DataBase
    @Deprecated
    public boolean dropTable(Object obj) {
        return dropTable(obj.getClass());
    }

    @Override // com.litesuits.orm.db.DataBase
    public boolean dropTable(String str) {
        acquireReference();
        try {
            try {
                return SQLBuilder.buildDropTable(str).execute(this.mHelper.getWritableDatabase());
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return false;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public boolean execute(SQLiteDatabase sQLiteDatabase, SQLStatement sQLStatement) {
        acquireReference();
        if (sQLStatement != null) {
            try {
                try {
                    return sQLStatement.execute(sQLiteDatabase);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                releaseReference();
            }
        }
        releaseReference();
        return false;
    }

    @Override // com.litesuits.orm.db.DataBase
    public DataBaseConfig getDataBaseConfig() {
        return this.mConfig;
    }

    @Override // com.litesuits.orm.db.DataBase
    public synchronized SQLiteDatabase getReadableDatabase() {
        return this.mHelper.getReadableDatabase();
    }

    @Override // com.litesuits.orm.db.DataBase
    public SQLiteHelper getSQLiteHelper() {
        return this.mHelper;
    }

    @Override // com.litesuits.orm.db.DataBase
    public TableManager getTableManager() {
        return this.mTableManager;
    }

    @Override // com.litesuits.orm.db.DataBase
    public synchronized SQLiteDatabase getWritableDatabase() {
        return this.mHelper.getWritableDatabase();
    }

    protected void justRelease() {
        if (this.mHelper != null) {
            this.mHelper.getWritableDatabase().close();
            this.mHelper.close();
            this.mHelper = null;
        }
        if (this.mTableManager != null) {
            this.mTableManager.release();
            this.mTableManager = null;
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <E, T> boolean mapping(Collection<E> collection, Collection<T> collection2) {
        if (Checker.isEmpty((Collection<?>) collection) || Checker.isEmpty((Collection<?>) collection2)) {
            return false;
        }
        acquireReference();
        try {
            return keepMapping(collection2, collection) | keepMapping(collection, collection2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            releaseReference();
        }
    }

    @Override // android.database.sqlite.SQLiteClosable
    protected void onAllReferencesReleased() {
        justRelease();
    }

    @Override // com.litesuits.orm.db.DataBase
    public SQLiteDatabase openOrCreateDatabase() {
        initDatabasePath(this.mConfig.dbName);
        if (this.mHelper != null) {
            justRelease();
        }
        this.mHelper = new SQLiteHelper(this.mConfig.context.getApplicationContext(), this.mConfig.dbName, null, this.mConfig.dbVersion, this.mConfig.onUpdateListener);
        this.mTableManager = new TableManager(this.mConfig.dbName, this.mHelper.getReadableDatabase());
        return this.mHelper.getWritableDatabase();
    }

    @Override // com.litesuits.orm.db.DataBase
    public SQLiteDatabase openOrCreateDatabase(String str, SQLiteDatabase.CursorFactory cursorFactory) {
        return SQLiteDatabase.openOrCreateDatabase(this.mConfig.context.getDatabasePath(this.mConfig.dbName).getPath(), cursorFactory);
    }

    @Override // com.litesuits.orm.db.DataBase
    public long queryCount(QueryBuilder queryBuilder) {
        acquireReference();
        try {
            try {
                if (!this.mTableManager.isSQLTableCreated(queryBuilder.getTableName())) {
                    return 0L;
                }
                return queryBuilder.createStatementForCount().queryForLong(this.mHelper.getReadableDatabase());
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1L;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> long queryCount(Class<T> cls) {
        return queryCount(new QueryBuilder(cls));
    }

    @Override // com.litesuits.orm.db.DataBase
    public ArrayList<RelationKey> queryRelation(final Class cls, final Class cls2, final List<String> list) {
        acquireReference();
        final ArrayList<RelationKey> arrayList = new ArrayList<>();
        try {
            try {
                final EntityTable table = TableManager.getTable((Class<?>) cls);
                final EntityTable table2 = TableManager.getTable((Class<?>) cls2);
                if (this.mTableManager.isSQLMapTableCreated(table.name, table2.name)) {
                    CollSpliter.split(list, 999, new CollSpliter.Spliter<String>() { // from class: com.litesuits.orm.LiteOrm.1
                        @Override // com.litesuits.orm.db.assit.CollSpliter.Spliter
                        public int oneSplit(ArrayList<String> arrayList2) throws Exception {
                            Querier.doQuery(LiteOrm.this.mHelper.getReadableDatabase(), SQLBuilder.buildQueryRelationSql(cls, cls2, (List<String>) list), new Querier.CursorParser() { // from class: com.litesuits.orm.LiteOrm.1.1
                                @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                                public void parseEachCursor(SQLiteDatabase sQLiteDatabase, Cursor cursor) throws Exception {
                                    RelationKey relationKey = new RelationKey();
                                    relationKey.key1 = cursor.getString(cursor.getColumnIndex(table.name));
                                    relationKey.key2 = cursor.getString(cursor.getColumnIndex(table2.name));
                                    arrayList.add(relationKey);
                                }
                            });
                            return 0;
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        } finally {
            releaseReference();
        }
    }

    public void setDebugged(boolean z) {
        this.mConfig.debugged = z;
        OrmLog.isPrint = z;
    }

    public abstract LiteOrm single();

    @Override // com.litesuits.orm.db.DataBase
    public int update(WhereBuilder whereBuilder, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            try {
                return SQLBuilder.buildUpdateSql(whereBuilder, columnsValue, conflictAlgorithm).execUpdate(this.mHelper.getWritableDatabase());
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1;
            }
        } finally {
            releaseReference();
        }
    }
}
