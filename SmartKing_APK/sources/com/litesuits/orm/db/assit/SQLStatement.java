package com.litesuits.orm.db.assit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import com.litesuits.orm.db.TableManager;
import com.litesuits.orm.db.assit.Querier;
import com.litesuits.orm.db.assit.Transaction;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.EntityTable;
import com.litesuits.orm.db.model.MapInfo;
import com.litesuits.orm.db.model.Property;
import com.litesuits.orm.db.utils.ClassUtil;
import com.litesuits.orm.db.utils.DataUtil;
import com.litesuits.orm.db.utils.FieldUtil;
import com.litesuits.orm.log.OrmLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SQLStatement implements Serializable {
    public static final int IN_TOP_LIMIT = 999;
    public static final short NONE = -1;
    public static final short NORMAL = 0;
    private static final String TAG = "SQLStatement";
    private static final long serialVersionUID = -3790876762607683712L;
    public Object[] bindArgs;
    private SQLiteStatement mStatement;
    public String sql;

    public SQLStatement() {
    }

    public SQLStatement(String str, Object[] objArr) {
        this.sql = str;
        this.bindArgs = objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mapRelationToDb(Object obj, final boolean z, final boolean z2, SQLiteDatabase sQLiteDatabase, final TableManager tableManager) {
        final MapInfo buildMappingInfo = SQLBuilder.buildMappingInfo(obj, z, tableManager);
        if (buildMappingInfo == null || buildMappingInfo.isEmpty()) {
            return;
        }
        Transaction.execute(sQLiteDatabase, new Transaction.Worker<Boolean>() { // from class: com.litesuits.orm.db.assit.SQLStatement.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.litesuits.orm.db.assit.Transaction.Worker
            public Boolean doTransaction(SQLiteDatabase sQLiteDatabase2) throws Exception {
                if (z && z2) {
                    Iterator<MapInfo.MapTable> it = buildMappingInfo.tableList.iterator();
                    while (it.hasNext()) {
                        MapInfo.MapTable next = it.next();
                        tableManager.checkOrCreateMappingTable(sQLiteDatabase2, next.name, next.column1, next.column2);
                    }
                }
                if (buildMappingInfo.delOldRelationSQL != null) {
                    Iterator<SQLStatement> it2 = buildMappingInfo.delOldRelationSQL.iterator();
                    while (it2.hasNext()) {
                        long execDelete = it2.next().execDelete(sQLiteDatabase2);
                        if (OrmLog.isPrint) {
                            OrmLog.v(SQLStatement.TAG, "Exec delete mapping success, nums: " + execDelete);
                        }
                    }
                }
                if (z && buildMappingInfo.mapNewRelationSQL != null) {
                    Iterator<SQLStatement> it3 = buildMappingInfo.mapNewRelationSQL.iterator();
                    while (it3.hasNext()) {
                        long execInsert = it3.next().execInsert(sQLiteDatabase2);
                        if (OrmLog.isPrint) {
                            OrmLog.v(SQLStatement.TAG, "Exec save mapping success, nums: " + execInsert);
                        }
                    }
                }
                return true;
            }
        });
    }

    private void printSQL() {
        if (OrmLog.isPrint) {
            OrmLog.d(TAG, "SQL Execute: [" + this.sql + "] ARGS--> " + Arrays.toString(this.bindArgs));
        }
    }

    private void realease() {
        if (this.mStatement != null) {
            this.mStatement.close();
        }
        this.bindArgs = null;
        this.mStatement = null;
    }

    protected void bind(int i, Object obj) throws IOException {
        if (obj == null) {
            this.mStatement.bindNull(i);
            return;
        }
        if ((obj instanceof CharSequence) || (obj instanceof Boolean) || (obj instanceof Character)) {
            this.mStatement.bindString(i, String.valueOf(obj));
            return;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            this.mStatement.bindDouble(i, ((Number) obj).doubleValue());
            return;
        }
        if (obj instanceof Number) {
            this.mStatement.bindLong(i, ((Number) obj).longValue());
            return;
        }
        if (obj instanceof Date) {
            this.mStatement.bindLong(i, ((Date) obj).getTime());
            return;
        }
        if (obj instanceof byte[]) {
            this.mStatement.bindBlob(i, (byte[]) obj);
        } else if (obj instanceof Serializable) {
            this.mStatement.bindBlob(i, DataUtil.objectToByte(obj));
        } else {
            this.mStatement.bindNull(i);
        }
    }

    public int execDelete(SQLiteDatabase sQLiteDatabase) throws IOException {
        return execDeleteWithMapping(sQLiteDatabase, null, null);
    }

    public int execDeleteCollection(SQLiteDatabase sQLiteDatabase, Collection<?> collection) throws IOException {
        return execDeleteCollectionWithMapping(sQLiteDatabase, collection, null);
    }

    public int execDeleteCollectionWithMapping(SQLiteDatabase sQLiteDatabase, final Collection<?> collection, final TableManager tableManager) throws IOException {
        int executeUpdateDelete;
        printSQL();
        this.mStatement = sQLiteDatabase.compileStatement(this.sql);
        if (this.bindArgs != null) {
            int i = 0;
            while (i < this.bindArgs.length) {
                int i2 = i + 1;
                bind(i2, this.bindArgs[i]);
                i = i2;
            }
        }
        if (Build.VERSION.SDK_INT < 11) {
            this.mStatement.execute();
            executeUpdateDelete = collection.size();
        } else {
            executeUpdateDelete = this.mStatement.executeUpdateDelete();
        }
        if (OrmLog.isPrint) {
            OrmLog.v(TAG, "SQL execute delete, changed rows --> " + executeUpdateDelete);
        }
        realease();
        if (tableManager != null) {
            Boolean bool = (Boolean) Transaction.execute(sQLiteDatabase, new Transaction.Worker<Boolean>() { // from class: com.litesuits.orm.db.assit.SQLStatement.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.litesuits.orm.db.assit.Transaction.Worker
                public Boolean doTransaction(SQLiteDatabase sQLiteDatabase2) throws Exception {
                    Iterator it = collection.iterator();
                    boolean z = true;
                    while (it.hasNext()) {
                        SQLStatement.this.mapRelationToDb(it.next(), false, z, sQLiteDatabase2, tableManager);
                        z = false;
                    }
                    return true;
                }
            });
            if (OrmLog.isPrint) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Exec delete collection mapping: ");
                sb.append((bool == null || !bool.booleanValue()) ? "失败" : "成功");
                OrmLog.i(str, sb.toString());
            }
        }
        return executeUpdateDelete;
    }

    public int execDeleteWithMapping(SQLiteDatabase sQLiteDatabase, Object obj, TableManager tableManager) throws IOException {
        printSQL();
        this.mStatement = sQLiteDatabase.compileStatement(this.sql);
        int i = 0;
        if (this.bindArgs != null) {
            int i2 = 0;
            while (i2 < this.bindArgs.length) {
                int i3 = i2 + 1;
                bind(i3, this.bindArgs[i2]);
                i2 = i3;
            }
        }
        if (Build.VERSION.SDK_INT < 11) {
            this.mStatement.execute();
        } else {
            i = this.mStatement.executeUpdateDelete();
        }
        if (OrmLog.isPrint) {
            OrmLog.v(TAG, "SQL execute delete, changed rows--> " + i);
        }
        realease();
        if (tableManager != null && obj != null) {
            mapRelationToDb(obj, false, false, sQLiteDatabase, tableManager);
        }
        return i;
    }

    public long execInsert(SQLiteDatabase sQLiteDatabase) throws IOException, IllegalAccessException {
        return execInsertWithMapping(sQLiteDatabase, null, null);
    }

    public long execInsert(SQLiteDatabase sQLiteDatabase, Object obj) throws IOException, IllegalAccessException {
        return execInsertWithMapping(sQLiteDatabase, obj, null);
    }

    public int execInsertCollection(SQLiteDatabase sQLiteDatabase, Collection<?> collection) {
        return execInsertCollectionWithMapping(sQLiteDatabase, collection, null);
    }

    public int execInsertCollectionWithMapping(SQLiteDatabase sQLiteDatabase, Collection<?> collection, TableManager tableManager) {
        Object obj;
        int i;
        printSQL();
        sQLiteDatabase.beginTransaction();
        if (OrmLog.isPrint) {
            OrmLog.i(TAG, "----> BeginTransaction[insert col]");
        }
        try {
            try {
                this.mStatement = sQLiteDatabase.compileStatement(this.sql);
                EntityTable entityTable = null;
                boolean z = true;
                for (Object obj2 : collection) {
                    this.mStatement.clearBindings();
                    if (entityTable == null) {
                        entityTable = TableManager.getTable(obj2);
                    }
                    if (entityTable.key != null) {
                        obj = FieldUtil.getAssignedKeyObject(entityTable.key, obj2);
                        i = 2;
                        bind(1, obj);
                    } else {
                        obj = null;
                        i = 1;
                    }
                    if (!Checker.isEmpty(entityTable.pmap)) {
                        Iterator<Property> it = entityTable.pmap.values().iterator();
                        while (it.hasNext()) {
                            bind(i, FieldUtil.get(it.next().field, obj2));
                            i++;
                        }
                    }
                    FieldUtil.setKeyValueIfneed(obj2, entityTable.key, obj, this.mStatement.executeInsert());
                    if (tableManager != null) {
                        mapRelationToDb(obj2, true, z, sQLiteDatabase, tableManager);
                        z = false;
                    }
                }
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "Exec insert [" + collection.size() + "] rows , SQL: " + this.sql);
                }
                sQLiteDatabase.setTransactionSuccessful();
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "----> BeginTransaction[insert col] Successful");
                }
                return collection.size();
            } catch (Exception e) {
                if (OrmLog.isPrint) {
                    OrmLog.e(TAG, "----> BeginTransaction[insert col] Failling");
                }
                e.printStackTrace();
                realease();
                sQLiteDatabase.endTransaction();
                return -1;
            }
        } finally {
            realease();
            sQLiteDatabase.endTransaction();
        }
    }

    public long execInsertWithMapping(SQLiteDatabase sQLiteDatabase, Object obj, TableManager tableManager) throws IllegalAccessException, IOException {
        Object obj2;
        printSQL();
        this.mStatement = sQLiteDatabase.compileStatement(this.sql);
        if (Checker.isEmpty(this.bindArgs)) {
            obj2 = null;
        } else {
            int i = 0;
            obj2 = this.bindArgs[0];
            while (i < this.bindArgs.length) {
                int i2 = i + 1;
                bind(i2, this.bindArgs[i]);
                i = i2;
            }
        }
        try {
            long executeInsert = this.mStatement.executeInsert();
            realease();
            if (OrmLog.isPrint) {
                OrmLog.i(TAG, "SQL Execute Insert RowID --> " + executeInsert + "    sql: " + this.sql);
            }
            if (obj != null) {
                FieldUtil.setKeyValueIfneed(obj, TableManager.getTable(obj).key, obj2, executeInsert);
            }
            if (tableManager != null) {
                mapRelationToDb(obj, true, true, sQLiteDatabase, tableManager);
            }
            return executeInsert;
        } catch (Throwable th) {
            realease();
            throw th;
        }
    }

    public int execUpdate(SQLiteDatabase sQLiteDatabase) throws IOException {
        return execUpdateWithMapping(sQLiteDatabase, null, null);
    }

    public int execUpdateCollection(SQLiteDatabase sQLiteDatabase, Collection<?> collection, ColumnsValue columnsValue) {
        return execUpdateCollectionWithMapping(sQLiteDatabase, collection, columnsValue, null);
    }

    public int execUpdateCollectionWithMapping(SQLiteDatabase sQLiteDatabase, Collection<?> collection, ColumnsValue columnsValue, TableManager tableManager) {
        printSQL();
        sQLiteDatabase.beginTransaction();
        if (OrmLog.isPrint) {
            OrmLog.d(TAG, "----> BeginTransaction[update col]");
        }
        try {
            try {
                this.mStatement = sQLiteDatabase.compileStatement(this.sql);
                EntityTable entityTable = null;
                boolean z = true;
                for (Object obj : collection) {
                    this.mStatement.clearBindings();
                    if (entityTable == null) {
                        entityTable = TableManager.getTable(obj);
                    }
                    this.bindArgs = SQLBuilder.buildUpdateSqlArgsOnly(obj, columnsValue);
                    if (!Checker.isEmpty(this.bindArgs)) {
                        int i = 0;
                        while (i < this.bindArgs.length) {
                            int i2 = i + 1;
                            bind(i2, this.bindArgs[i]);
                            i = i2;
                        }
                    }
                    this.mStatement.execute();
                    if (tableManager != null) {
                        mapRelationToDb(obj, true, z, sQLiteDatabase, tableManager);
                        z = false;
                    }
                }
                if (OrmLog.isPrint) {
                    OrmLog.i(TAG, "Exec update [" + collection.size() + "] rows , SQL: " + this.sql);
                }
                sQLiteDatabase.setTransactionSuccessful();
                if (OrmLog.isPrint) {
                    OrmLog.d(TAG, "----> BeginTransaction[update col] Successful");
                }
                return collection.size();
            } catch (Exception e) {
                if (OrmLog.isPrint) {
                    OrmLog.e(TAG, "----> BeginTransaction[update col] Failling");
                }
                e.printStackTrace();
                realease();
                sQLiteDatabase.endTransaction();
                return -1;
            }
        } finally {
            realease();
            sQLiteDatabase.endTransaction();
        }
    }

    public int execUpdateWithMapping(SQLiteDatabase sQLiteDatabase, Object obj, TableManager tableManager) throws IOException {
        printSQL();
        this.mStatement = sQLiteDatabase.compileStatement(this.sql);
        int i = 0;
        if (!Checker.isEmpty(this.bindArgs)) {
            int i2 = 0;
            while (i2 < this.bindArgs.length) {
                int i3 = i2 + 1;
                bind(i3, this.bindArgs[i2]);
                i2 = i3;
            }
        }
        if (Build.VERSION.SDK_INT < 11) {
            this.mStatement.execute();
        } else {
            i = this.mStatement.executeUpdateDelete();
        }
        realease();
        if (OrmLog.isPrint) {
            OrmLog.i(TAG, "SQL Execute update, changed rows --> " + i);
        }
        if (tableManager != null && obj != null) {
            mapRelationToDb(obj, true, true, sQLiteDatabase, tableManager);
        }
        return i;
    }

    public boolean execute(SQLiteDatabase sQLiteDatabase) {
        printSQL();
        try {
            try {
                this.mStatement = sQLiteDatabase.compileStatement(this.sql);
                if (this.bindArgs != null) {
                    int i = 0;
                    while (i < this.bindArgs.length) {
                        int i2 = i + 1;
                        bind(i2, this.bindArgs[i]);
                        i = i2;
                    }
                }
                this.mStatement.execute();
                realease();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                realease();
                return false;
            }
        } catch (Throwable th) {
            realease();
            throw th;
        }
    }

    public <T> ArrayList<T> query(SQLiteDatabase sQLiteDatabase, final Class<T> cls) {
        printSQL();
        final ArrayList<T> arrayList = new ArrayList<>();
        try {
            final EntityTable table = TableManager.getTable(cls, false);
            Querier.doQuery(sQLiteDatabase, this, new Querier.CursorParser() { // from class: com.litesuits.orm.db.assit.SQLStatement.2
                @Override // com.litesuits.orm.db.assit.Querier.CursorParser
                public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                    Object newInstance = ClassUtil.newInstance(cls);
                    DataUtil.injectDataToObject(cursor, newInstance, table);
                    arrayList.add(newInstance);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public long queryForLong(SQLiteDatabase sQLiteDatabase) {
        long j;
        printSQL();
        try {
            try {
                this.mStatement = sQLiteDatabase.compileStatement(this.sql);
                if (this.bindArgs != null) {
                    int i = 0;
                    while (i < this.bindArgs.length) {
                        int i2 = i + 1;
                        bind(i2, this.bindArgs[i]);
                        i = i2;
                    }
                }
                j = this.mStatement.simpleQueryForLong();
            } finally {
                realease();
            }
        } catch (Exception e) {
            e = e;
            j = 0;
        }
        try {
            if (OrmLog.isPrint) {
                OrmLog.i(TAG, "SQL execute query for count --> " + j);
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return j;
        }
        return j;
    }

    public <T> T queryOneEntity(SQLiteDatabase sQLiteDatabase, final Class<T> cls) {
        printSQL();
        final EntityTable table = TableManager.getTable(cls, false);
        return (T) Querier.doQuery(sQLiteDatabase, this, new Querier.CursorParser<T>() { // from class: com.litesuits.orm.db.assit.SQLStatement.3
            T t;

            @Override // com.litesuits.orm.db.assit.Querier.CursorParser
            public void parseEachCursor(SQLiteDatabase sQLiteDatabase2, Cursor cursor) throws Exception {
                this.t = (T) ClassUtil.newInstance(cls);
                DataUtil.injectDataToObject(cursor, this.t, table);
                stopParse();
            }

            @Override // com.litesuits.orm.db.assit.Querier.CursorParser
            public T returnResult() {
                return this.t;
            }
        });
    }

    public String toString() {
        return "SQLStatement [sql=" + this.sql + ", bindArgs=" + Arrays.toString(this.bindArgs) + ", mStatement=" + this.mStatement + "]";
    }
}
