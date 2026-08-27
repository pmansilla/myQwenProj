package com.liulishuo.filedownloader.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.SparseArray;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SqliteDatabaseImpl implements FileDownloadDatabase {
    public static final String CONNECTION_TABLE_NAME = "filedownloaderConnection";
    public static final String TABLE_NAME = "filedownloader";
    private final SQLiteDatabase db = new SqliteDatabaseOpenHelper(FileDownloadHelper.getAppContext()).getWritableDatabase();

    /* loaded from: classes.dex */
    public class Maintainer implements FileDownloadDatabase.Maintainer {
        private final SparseArray<List<ConnectionModel>> connectionModelListMap;
        private MaintainerIterator currentIterator;
        private final SparseArray<FileDownloadModel> downloaderModelMap;
        private final SparseArray<FileDownloadModel> needChangeIdList;

        Maintainer(SqliteDatabaseImpl sqliteDatabaseImpl) {
            this(null, null);
        }

        Maintainer(SparseArray<FileDownloadModel> sparseArray, SparseArray<List<ConnectionModel>> sparseArray2) {
            this.needChangeIdList = new SparseArray<>();
            this.downloaderModelMap = sparseArray;
            this.connectionModelListMap = sparseArray2;
        }

        @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase.Maintainer
        public void changeFileDownloadModelId(int i, FileDownloadModel fileDownloadModel) {
            this.needChangeIdList.put(i, fileDownloadModel);
        }

        @Override // java.lang.Iterable
        public Iterator<FileDownloadModel> iterator() {
            MaintainerIterator maintainerIterator = new MaintainerIterator();
            this.currentIterator = maintainerIterator;
            return maintainerIterator;
        }

        @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase.Maintainer
        public void onFinishMaintain() {
            if (this.currentIterator != null) {
                this.currentIterator.onFinishMaintain();
            }
            int size = this.needChangeIdList.size();
            if (size < 0) {
                return;
            }
            SqliteDatabaseImpl.this.db.beginTransaction();
            for (int i = 0; i < size; i++) {
                try {
                    int keyAt = this.needChangeIdList.keyAt(i);
                    FileDownloadModel fileDownloadModel = this.needChangeIdList.get(keyAt);
                    SqliteDatabaseImpl.this.db.delete(SqliteDatabaseImpl.TABLE_NAME, "_id = ?", new String[]{String.valueOf(keyAt)});
                    SqliteDatabaseImpl.this.db.insert(SqliteDatabaseImpl.TABLE_NAME, null, fileDownloadModel.toContentValues());
                    if (fileDownloadModel.getConnectionCount() > 1) {
                        List<ConnectionModel> findConnectionModel = SqliteDatabaseImpl.this.findConnectionModel(keyAt);
                        if (findConnectionModel.size() > 0) {
                            SqliteDatabaseImpl.this.db.delete(SqliteDatabaseImpl.CONNECTION_TABLE_NAME, "id = ?", new String[]{String.valueOf(keyAt)});
                            for (ConnectionModel connectionModel : findConnectionModel) {
                                connectionModel.setId(fileDownloadModel.getId());
                                SqliteDatabaseImpl.this.db.insert(SqliteDatabaseImpl.CONNECTION_TABLE_NAME, null, connectionModel.toContentValues());
                            }
                        }
                    }
                } catch (Throwable th) {
                    SqliteDatabaseImpl.this.db.endTransaction();
                    throw th;
                }
            }
            if (this.downloaderModelMap != null && this.connectionModelListMap != null) {
                int size2 = this.downloaderModelMap.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    int id = this.downloaderModelMap.valueAt(i2).getId();
                    List<ConnectionModel> findConnectionModel2 = SqliteDatabaseImpl.this.findConnectionModel(id);
                    if (findConnectionModel2 != null && findConnectionModel2.size() > 0) {
                        this.connectionModelListMap.put(id, findConnectionModel2);
                    }
                }
            }
            SqliteDatabaseImpl.this.db.setTransactionSuccessful();
            SqliteDatabaseImpl.this.db.endTransaction();
        }

        @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase.Maintainer
        public void onRefreshedValidData(FileDownloadModel fileDownloadModel) {
            if (this.downloaderModelMap != null) {
                this.downloaderModelMap.put(fileDownloadModel.getId(), fileDownloadModel);
            }
        }

        @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase.Maintainer
        public void onRemovedInvalidData(FileDownloadModel fileDownloadModel) {
        }
    }

    /* loaded from: classes.dex */
    class MaintainerIterator implements Iterator<FileDownloadModel> {
        private final Cursor c;
        private int currentId;
        private final List<Integer> needRemoveId = new ArrayList();

        MaintainerIterator() {
            this.c = SqliteDatabaseImpl.this.db.rawQuery("SELECT * FROM filedownloader", null);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.c.moveToNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public FileDownloadModel next() {
            FileDownloadModel createFromCursor = SqliteDatabaseImpl.createFromCursor(this.c);
            this.currentId = createFromCursor.getId();
            return createFromCursor;
        }

        void onFinishMaintain() {
            this.c.close();
            if (this.needRemoveId.isEmpty()) {
                return;
            }
            String join = TextUtils.join(", ", this.needRemoveId);
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "delete %s", join);
            }
            SqliteDatabaseImpl.this.db.execSQL(FileDownloadUtils.formatString("DELETE FROM %s WHERE %s IN (%s);", SqliteDatabaseImpl.TABLE_NAME, FileDownloadModel.ID, join));
            SqliteDatabaseImpl.this.db.execSQL(FileDownloadUtils.formatString("DELETE FROM %s WHERE %s IN (%s);", SqliteDatabaseImpl.CONNECTION_TABLE_NAME, ConnectionModel.ID, join));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.needRemoveId.add(Integer.valueOf(this.currentId));
        }
    }

    /* loaded from: classes.dex */
    public static class Maker implements FileDownloadHelper.DatabaseCustomMaker {
        @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.DatabaseCustomMaker
        public FileDownloadDatabase customMake() {
            return new SqliteDatabaseImpl();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FileDownloadModel createFromCursor(Cursor cursor) {
        FileDownloadModel fileDownloadModel = new FileDownloadModel();
        fileDownloadModel.setId(cursor.getInt(cursor.getColumnIndex(FileDownloadModel.ID)));
        fileDownloadModel.setUrl(cursor.getString(cursor.getColumnIndex(FileDownloadModel.URL)));
        fileDownloadModel.setPath(cursor.getString(cursor.getColumnIndex(FileDownloadModel.PATH)), cursor.getShort(cursor.getColumnIndex(FileDownloadModel.PATH_AS_DIRECTORY)) == 1);
        fileDownloadModel.setStatus((byte) cursor.getShort(cursor.getColumnIndex("status")));
        fileDownloadModel.setSoFar(cursor.getLong(cursor.getColumnIndex(FileDownloadModel.SOFAR)));
        fileDownloadModel.setTotal(cursor.getLong(cursor.getColumnIndex(FileDownloadModel.TOTAL)));
        fileDownloadModel.setErrMsg(cursor.getString(cursor.getColumnIndex(FileDownloadModel.ERR_MSG)));
        fileDownloadModel.setETag(cursor.getString(cursor.getColumnIndex(FileDownloadModel.ETAG)));
        fileDownloadModel.setFilename(cursor.getString(cursor.getColumnIndex(FileDownloadModel.FILENAME)));
        fileDownloadModel.setConnectionCount(cursor.getInt(cursor.getColumnIndex(FileDownloadModel.CONNECTION_COUNT)));
        return fileDownloadModel;
    }

    public static Maker createMaker() {
        return new Maker();
    }

    private void update(int i, ContentValues contentValues) {
        this.db.update(TABLE_NAME, contentValues, "_id = ? ", new String[]{String.valueOf(i)});
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void clear() {
        this.db.delete(TABLE_NAME, null, null);
        this.db.delete(CONNECTION_TABLE_NAME, null, null);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public FileDownloadModel find(int i) {
        Cursor cursor = null;
        try {
            Cursor rawQuery = this.db.rawQuery(FileDownloadUtils.formatString("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, FileDownloadModel.ID), new String[]{Integer.toString(i)});
            try {
                if (!rawQuery.moveToNext()) {
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return null;
                }
                FileDownloadModel createFromCursor = createFromCursor(rawQuery);
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return createFromCursor;
            } catch (Throwable th) {
                cursor = rawQuery;
                th = th;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public List<ConnectionModel> findConnectionModel(int i) {
        Cursor cursor;
        ArrayList arrayList = new ArrayList();
        try {
            cursor = this.db.rawQuery(FileDownloadUtils.formatString("SELECT * FROM %s WHERE %s = ?", CONNECTION_TABLE_NAME, ConnectionModel.ID), new String[]{Integer.toString(i)});
            while (cursor.moveToNext()) {
                try {
                    ConnectionModel connectionModel = new ConnectionModel();
                    connectionModel.setId(i);
                    connectionModel.setIndex(cursor.getInt(cursor.getColumnIndex(ConnectionModel.INDEX)));
                    connectionModel.setStartOffset(cursor.getLong(cursor.getColumnIndex(ConnectionModel.START_OFFSET)));
                    connectionModel.setCurrentOffset(cursor.getLong(cursor.getColumnIndex(ConnectionModel.CURRENT_OFFSET)));
                    connectionModel.setEndOffset(cursor.getLong(cursor.getColumnIndex(ConnectionModel.END_OFFSET)));
                    arrayList.add(connectionModel);
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
        }
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void insert(FileDownloadModel fileDownloadModel) {
        this.db.insert(TABLE_NAME, null, fileDownloadModel.toContentValues());
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void insertConnectionModel(ConnectionModel connectionModel) {
        this.db.insert(CONNECTION_TABLE_NAME, null, connectionModel.toContentValues());
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public FileDownloadDatabase.Maintainer maintainer() {
        return new Maintainer(this);
    }

    public FileDownloadDatabase.Maintainer maintainer(SparseArray<FileDownloadModel> sparseArray, SparseArray<List<ConnectionModel>> sparseArray2) {
        return new Maintainer(sparseArray, sparseArray2);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void onTaskStart(int i) {
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public boolean remove(int i) {
        return this.db.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(i)}) != 0;
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void removeConnections(int i) {
        this.db.execSQL("DELETE FROM filedownloaderConnection WHERE id = " + i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void update(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel == null) {
            FileDownloadLog.w(this, "update but model == null!", new Object[0]);
        } else if (find(fileDownloadModel.getId()) == null) {
            insert(fileDownloadModel);
        } else {
            this.db.update(TABLE_NAME, fileDownloadModel.toContentValues(), "_id = ? ", new String[]{String.valueOf(fileDownloadModel.getId())});
        }
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateCompleted(int i, long j) {
        remove(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnected(int i, long j, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", (Byte) (byte) 2);
        contentValues.put(FileDownloadModel.TOTAL, Long.valueOf(j));
        contentValues.put(FileDownloadModel.ETAG, str);
        contentValues.put(FileDownloadModel.FILENAME, str2);
        update(i, contentValues);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnectionCount(int i, int i2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileDownloadModel.CONNECTION_COUNT, Integer.valueOf(i2));
        this.db.update(TABLE_NAME, contentValues, "_id = ? ", new String[]{Integer.toString(i)});
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnectionModel(int i, int i2, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConnectionModel.CURRENT_OFFSET, Long.valueOf(j));
        this.db.update(CONNECTION_TABLE_NAME, contentValues, "id = ? AND connectionIndex = ?", new String[]{Integer.toString(i), Integer.toString(i2)});
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateError(int i, Throwable th, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileDownloadModel.ERR_MSG, th.toString());
        contentValues.put("status", (Byte) (byte) -1);
        contentValues.put(FileDownloadModel.SOFAR, Long.valueOf(j));
        update(i, contentValues);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateOldEtagOverdue(int i, String str, long j, long j2, int i2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileDownloadModel.SOFAR, Long.valueOf(j));
        contentValues.put(FileDownloadModel.TOTAL, Long.valueOf(j2));
        contentValues.put(FileDownloadModel.ETAG, str);
        contentValues.put(FileDownloadModel.CONNECTION_COUNT, Integer.valueOf(i2));
        update(i, contentValues);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updatePause(int i, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", (Byte) (byte) -2);
        contentValues.put(FileDownloadModel.SOFAR, Long.valueOf(j));
        update(i, contentValues);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updatePending(int i) {
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateProgress(int i, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", (Byte) (byte) 3);
        contentValues.put(FileDownloadModel.SOFAR, Long.valueOf(j));
        update(i, contentValues);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateRetry(int i, Throwable th) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileDownloadModel.ERR_MSG, th.toString());
        contentValues.put("status", (Byte) (byte) 5);
        update(i, contentValues);
    }
}
