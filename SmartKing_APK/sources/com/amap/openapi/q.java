package com.amap.openapi;

import com.amap.location.common.model.CellStatus;
import java.util.ArrayList;
import java.util.List;

/* compiled from: Cell.java */
/* loaded from: classes.dex */
public class q {
    public byte a;
    public String b;
    public ArrayList<r> c = new ArrayList<>();
    public List<CellStatus.HistoryCell> d = new ArrayList();

    public void a(byte b, String str) {
        this.a = b;
        this.b = str;
        this.c.clear();
        this.d.clear();
    }

    public String toString() {
        return "Cell{mRadioType=" + ((int) this.a) + ", mOperator='" + this.b + "', mCellPart=" + this.c + ", mHistoryCellList=" + this.d + '}';
    }
}
