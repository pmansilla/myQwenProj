package com.amap.location.common.model;

import android.os.SystemClock;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class CellStatus {
    public static final int MASK_CDMA_CELL_TYPE = 2;
    public static final int MASK_GSM_CELL_TYPE = 1;
    public static final int MASK_NEW_VERSION_INDICATOR = 4;
    public static final int MASK_OPERATOR_INDICATOR = 8;
    private static final int MAX_HISTORY_CELLS = 3;
    public CellState mainCell;
    public CellState mainCell2;
    public String networkOperator;
    public long updateTime;
    public int cellType = 0;
    public List<CellState> neighbors = Collections.emptyList();
    public List<CellState> cellStateList2 = Collections.emptyList();
    private final List<HistoryCell> mHistoryCells = new ArrayList(3);

    /* loaded from: classes.dex */
    public static class HistoryCell {
        public int bid;
        public int cid;
        public int lac;
        public long lastUpdateTimeMills;
        public int nid;
        public int rssi;
        public int sid;
        public int type;

        public HistoryCell() {
            this.type = 0;
            this.rssi = 0;
            this.lac = 0;
            this.cid = 0;
            this.sid = 0;
            this.nid = 0;
            this.bid = 0;
            this.lastUpdateTimeMills = 0L;
        }

        public HistoryCell(CellState cellState) {
            this.type = 0;
            this.rssi = 0;
            this.lac = 0;
            this.cid = 0;
            this.sid = 0;
            this.nid = 0;
            this.bid = 0;
            this.lastUpdateTimeMills = 0L;
            this.type = cellState.type;
            this.rssi = cellState.signalStrength;
            this.lac = cellState.lac;
            this.cid = cellState.cid;
            this.sid = cellState.sid;
            this.nid = cellState.nid;
            this.bid = cellState.bid;
            this.lastUpdateTimeMills = cellState.lastUpdateTimeMills <= 0 ? SystemClock.elapsedRealtime() : cellState.lastUpdateTimeMills;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public HistoryCell m16clone() {
            HistoryCell historyCell = new HistoryCell();
            historyCell.type = this.type;
            historyCell.rssi = this.rssi;
            historyCell.lac = this.lac;
            historyCell.cid = this.cid;
            historyCell.sid = this.sid;
            historyCell.nid = this.nid;
            historyCell.bid = this.bid;
            historyCell.lastUpdateTimeMills = this.lastUpdateTimeMills;
            return historyCell;
        }

        public boolean equals(Object obj) {
            if (obj != null && (obj instanceof HistoryCell)) {
                HistoryCell historyCell = (HistoryCell) obj;
                if (this.type == historyCell.type && this.lac == historyCell.lac && this.cid == historyCell.cid && this.nid == historyCell.nid && this.bid == historyCell.bid && this.sid == historyCell.sid) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(Locale.CHINA, "[type=%d,rssi=%d,lac=%d, cid=%d,sid=%d,nid=%d, bid=%d, time=%d]", Integer.valueOf(this.type), Integer.valueOf(this.rssi), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.sid), Integer.valueOf(this.nid), Integer.valueOf(this.bid), Long.valueOf(this.lastUpdateTimeMills));
        }
    }

    private String toStr(boolean z) {
        String str;
        String str2;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("CellStatus:[");
        sb3.append("updateTime=" + this.updateTime + ",");
        sb3.append("cellType=" + this.cellType + ",");
        sb3.append("networkOperator=" + this.networkOperator + ",");
        sb3.append(this.mainCell != null ? "mainCell=" + this.mainCell.toString() + "," : "mainCell=null ,");
        sb3.append(this.mainCell2 != null ? "mainCell2=" + this.mainCell2.toString() + "," : "mainCell2=null ,");
        if (this.neighbors == null || this.neighbors.size() <= 0) {
            str = "neighbors=null";
        } else {
            ArrayList arrayList = new ArrayList();
            if (this.neighbors.size() <= 5) {
                arrayList.addAll(this.neighbors);
                sb2 = new StringBuilder("neighbors=");
            } else if (z) {
                arrayList.addAll(this.neighbors.subList(0, 5));
                sb2 = new StringBuilder("neighbors=");
            } else {
                arrayList.addAll(this.neighbors);
                sb2 = new StringBuilder("neighbors=");
            }
            sb2.append(arrayList.toString());
            str = sb2.toString();
        }
        sb3.append(str);
        sb3.append(";");
        if (this.cellStateList2 == null || this.cellStateList2.size() <= 0) {
            str2 = "cellStateList2=null";
        } else {
            ArrayList arrayList2 = new ArrayList();
            if (this.cellStateList2.size() <= 5) {
                arrayList2.addAll(this.cellStateList2);
                sb = new StringBuilder("cellStateList2=");
            } else if (z) {
                arrayList2.addAll(this.cellStateList2.subList(0, 5));
                sb = new StringBuilder("cellStateList2=");
            } else {
                arrayList2.addAll(this.cellStateList2);
                sb = new StringBuilder("cellStateList2=");
            }
            sb.append(arrayList2.toString());
            str2 = sb.toString();
        }
        sb3.append(str2);
        sb3.append("]");
        StringBuilder sb4 = new StringBuilder(" [HistoryCell:");
        int size = this.mHistoryCells.size();
        for (int i = 0; i < size; i++) {
            sb4.append(i);
            sb4.append(":");
            sb4.append(this.mHistoryCells.get(i).toString());
            sb4.append(SQLBuilder.BLANK);
        }
        sb4.append("]");
        return sb3.toString() + sb4.toString();
    }

    public void addHistoryCell(List<HistoryCell> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mHistoryCells.clear();
        this.mHistoryCells.addAll(list);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CellStatus m15clone() {
        CellStatus cellStatus = new CellStatus();
        cellStatus.updateTime = this.updateTime;
        cellStatus.cellType = this.cellType;
        cellStatus.networkOperator = this.networkOperator;
        if (this.mainCell != null) {
            cellStatus.mainCell = this.mainCell.m14clone();
        }
        if (this.mainCell2 != null) {
            cellStatus.mainCell2 = this.mainCell2.m14clone();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.neighbors);
        cellStatus.neighbors = arrayList;
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(this.cellStateList2);
        cellStatus.cellStateList2 = arrayList2;
        Iterator<HistoryCell> it = this.mHistoryCells.iterator();
        while (it.hasNext()) {
            cellStatus.mHistoryCells.add(it.next().m16clone());
        }
        return cellStatus;
    }

    public List<HistoryCell> getHistoryCells() {
        return this.mHistoryCells;
    }

    public String toString() {
        return toStr(false);
    }

    public String toStringSimple() {
        return toStr(true);
    }
}
