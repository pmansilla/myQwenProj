package com.liulishuo.filedownloader.model;

import android.content.ContentValues;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.List;

/* loaded from: classes.dex */
public class ConnectionModel {
    public static final String CURRENT_OFFSET = "currentOffset";
    public static final String END_OFFSET = "endOffset";
    public static final String ID = "id";
    public static final String INDEX = "connectionIndex";
    public static final String START_OFFSET = "startOffset";
    private long currentOffset;
    private long endOffset;
    private int id;
    private int index;
    private long startOffset;

    public static long getTotalOffset(List<ConnectionModel> list) {
        long j = 0;
        for (ConnectionModel connectionModel : list) {
            j += connectionModel.getCurrentOffset() - connectionModel.getStartOffset();
        }
        return j;
    }

    public long getCurrentOffset() {
        return this.currentOffset;
    }

    public long getEndOffset() {
        return this.endOffset;
    }

    public int getId() {
        return this.id;
    }

    public int getIndex() {
        return this.index;
    }

    public long getStartOffset() {
        return this.startOffset;
    }

    public void setCurrentOffset(long j) {
        this.currentOffset = j;
    }

    public void setEndOffset(long j) {
        this.endOffset = j;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public void setStartOffset(long j) {
        this.startOffset = j;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, Integer.valueOf(this.id));
        contentValues.put(INDEX, Integer.valueOf(this.index));
        contentValues.put(START_OFFSET, Long.valueOf(this.startOffset));
        contentValues.put(CURRENT_OFFSET, Long.valueOf(this.currentOffset));
        contentValues.put(END_OFFSET, Long.valueOf(this.endOffset));
        return contentValues;
    }

    public String toString() {
        return FileDownloadUtils.formatString("id[%d] index[%d] range[%d, %d) current offset(%d)", Integer.valueOf(this.id), Integer.valueOf(this.index), Long.valueOf(this.startOffset), Long.valueOf(this.endOffset), Long.valueOf(this.currentOffset));
    }
}
