package com.litesuits.orm.db.impl;

import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.CollSpliter;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.litesuits.orm.db.assit.SQLStatement;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.db.model.EntityTable;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes.dex */
public final class SingleSQLiteImpl extends LiteOrm {
    public static final String TAG = "SingleSQLiteImpl";

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleSQLiteImpl(LiteOrm liteOrm) {
        super(liteOrm);
    }

    private SingleSQLiteImpl(DataBaseConfig dataBaseConfig) {
        super(dataBaseConfig);
    }

    public static synchronized LiteOrm newInstance(DataBaseConfig dataBaseConfig) {
        SingleSQLiteImpl singleSQLiteImpl;
        synchronized (SingleSQLiteImpl.class) {
            singleSQLiteImpl = new SingleSQLiteImpl(dataBaseConfig);
        }
        return singleSQLiteImpl;
    }

    @Override // com.litesuits.orm.LiteOrm
    public LiteOrm cascade() {
        if (this.otherDatabase == null) {
            this.otherDatabase = new CascadeSQLiteImpl(this);
        }
        return this.otherDatabase;
    }

    @Override // com.litesuits.orm.db.DataBase
    public int delete(WhereBuilder whereBuilder) {
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(whereBuilder.getTableClass(), false).name)) {
            return -1;
        }
        acquireReference();
        try {
            return whereBuilder.createStatementDelete().execDelete(this.mHelper.getWritableDatabase());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Class<T> cls) {
        return deleteAll(cls);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int delete(Class<T> cls, long j, long j2, String str) {
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(cls, false).name)) {
            return -1;
        }
        acquireReference();
        try {
            if (j < 0 || j2 < j) {
                throw new RuntimeException("start must >=0 and smaller than end");
            }
            if (j != 0) {
                j--;
            }
            long j3 = j;
            return SQLBuilder.buildDeleteSql(cls, j3, j2 == 2147483647L ? -1L : j2 - j3, str).execDelete(this.mHelper.getWritableDatabase());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    @Deprecated
    public <T> int delete(Class<T> cls, WhereBuilder whereBuilder) {
        return delete(whereBuilder);
    }

    @Override // com.litesuits.orm.db.DataBase
    public int delete(Object obj) {
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(obj).name)) {
            return -1;
        }
        acquireReference();
        try {
            return SQLBuilder.buildDeleteSql(obj).execDelete(this.mHelper.getWritableDatabase());
        } catch (Exception e) {
            e.printStackTrace();
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
                if (!Checker.isEmpty((Collection<?>) collection)) {
                    if (this.mTableManager.isSQLTableCreated(TableManager.getTable(collection.iterator().next()).name)) {
                        final SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                        writableDatabase.beginTransaction();
                        try {
                            int split = CollSpliter.split(collection, 999, new CollSpliter.Spliter<T>() { // from class: com.litesuits.orm.db.impl.SingleSQLiteImpl.1
                                @Override // com.litesuits.orm.db.assit.CollSpliter.Spliter
                                public int oneSplit(ArrayList<T> arrayList) throws Exception {
                                    return SQLBuilder.buildDeleteSql((Collection<?>) arrayList).execDeleteCollection(writableDatabase, arrayList);
                                }
                            });
                            writableDatabase.setTransactionSuccessful();
                            return split;
                        } finally {
                            writableDatabase.endTransaction();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int deleteAll(Class<T> cls) {
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(cls, false).name)) {
            return -1;
        }
        acquireReference();
        try {
            return SQLBuilder.buildDeleteAllSql(cls).execDelete(this.mHelper.getWritableDatabase());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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
                if (!Checker.isEmpty((Collection<?>) collection)) {
                    SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                    T next = collection.iterator().next();
                    SQLStatement buildInsertAllSql = SQLBuilder.buildInsertAllSql(next, conflictAlgorithm);
                    this.mTableManager.checkOrCreateTable(writableDatabase, next);
                    return buildInsertAllSql.execInsertCollection(writableDatabase, collection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public long insert(Object obj) {
        return insert(obj, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public long insert(Object obj, ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            try {
                SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                this.mTableManager.checkOrCreateTable(writableDatabase, obj);
                return SQLBuilder.buildInsertSql(obj, conflictAlgorithm).execInsert(writableDatabase, obj);
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
    public <T> ArrayList<T> query(QueryBuilder<T> queryBuilder) {
        if (!this.mTableManager.isSQLTableCreated(TableManager.getTable(queryBuilder.getQueryClass(), false).name)) {
            return new ArrayList<>();
        }
        acquireReference();
        try {
            return queryBuilder.createStatement().query(this.mHelper.getReadableDatabase(), queryBuilder.getQueryClass());
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> ArrayList<T> query(Class<T> cls) {
        return query(new QueryBuilder<>(cls));
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> T queryById(long j, Class<T> cls) {
        return (T) queryById(String.valueOf(j), cls);
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> T queryById(String str, Class<T> cls) {
        EntityTable table = TableManager.getTable(cls, false);
        if (!this.mTableManager.isSQLTableCreated(table.name)) {
            return null;
        }
        acquireReference();
        try {
            ArrayList<T> query = new QueryBuilder(cls).where(table.key.column + "=?", str).createStatement().query(this.mHelper.getReadableDatabase(), cls);
            if (Checker.isEmpty(query)) {
                return null;
            }
            return query.get(0);
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int save(Collection<T> collection) {
        acquireReference();
        try {
            try {
                if (!Checker.isEmpty((Collection<?>) collection)) {
                    SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                    T next = collection.iterator().next();
                    this.mTableManager.checkOrCreateTable(writableDatabase, next);
                    return SQLBuilder.buildReplaceAllSql(next).execInsertCollection(writableDatabase, collection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public long save(Object obj) {
        acquireReference();
        try {
            try {
                SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                this.mTableManager.checkOrCreateTable(writableDatabase, obj);
                return SQLBuilder.buildReplaceSql(obj).execInsert(writableDatabase, obj);
            } catch (Exception e) {
                e.printStackTrace();
                releaseReference();
                return -1L;
            }
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.LiteOrm
    public LiteOrm single() {
        return this;
    }

    @Override // com.litesuits.orm.db.DataBase
    public int update(Object obj) {
        return update(obj, (ColumnsValue) null, (ConflictAlgorithm) null);
    }

    @Override // com.litesuits.orm.db.DataBase
    public int update(Object obj, ColumnsValue columnsValue, ConflictAlgorithm conflictAlgorithm) {
        acquireReference();
        try {
            try {
                SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                this.mTableManager.checkOrCreateTable(writableDatabase, obj);
                return SQLBuilder.buildUpdateSql(obj, columnsValue, conflictAlgorithm).execUpdate(writableDatabase);
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
                if (!Checker.isEmpty((Collection<?>) collection)) {
                    SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
                    T next = collection.iterator().next();
                    this.mTableManager.checkOrCreateTable(writableDatabase, next);
                    return SQLBuilder.buildUpdateAllSql(next, columnsValue, conflictAlgorithm).execUpdateCollection(writableDatabase, collection, columnsValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        } finally {
            releaseReference();
        }
    }

    @Override // com.litesuits.orm.db.DataBase
    public <T> int update(Collection<T> collection, ConflictAlgorithm conflictAlgorithm) {
        return update((Collection) collection, (ColumnsValue) null, conflictAlgorithm);
    }
}
