package com.litesuits.orm.db.model;

import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.SQLStatement;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class MapInfo {
    public ArrayList<SQLStatement> delOldRelationSQL;
    public ArrayList<SQLStatement> mapNewRelationSQL;
    public ArrayList<MapTable> tableList;

    /* loaded from: classes.dex */
    public static class MapTable {
        public String column1;
        public String column2;
        public String name;

        public MapTable(String str, String str2, String str3) {
            this.name = str;
            this.column1 = str2;
            this.column2 = str3;
        }
    }

    public boolean addDelOldRelationSQL(SQLStatement sQLStatement) {
        if (this.delOldRelationSQL == null) {
            this.delOldRelationSQL = new ArrayList<>();
        }
        return this.delOldRelationSQL.add(sQLStatement);
    }

    public boolean addNewRelationSQL(SQLStatement sQLStatement) {
        if (this.mapNewRelationSQL == null) {
            this.mapNewRelationSQL = new ArrayList<>();
        }
        return this.mapNewRelationSQL.add(sQLStatement);
    }

    public boolean addNewRelationSQL(ArrayList<SQLStatement> arrayList) {
        if (this.mapNewRelationSQL == null) {
            this.mapNewRelationSQL = new ArrayList<>();
        }
        return this.mapNewRelationSQL.addAll(arrayList);
    }

    public boolean addTable(MapTable mapTable) {
        if (mapTable.name == null) {
            return false;
        }
        if (this.tableList == null) {
            this.tableList = new ArrayList<>();
        }
        return this.tableList.add(mapTable);
    }

    public boolean isEmpty() {
        return Checker.isEmpty(this.tableList) || (Checker.isEmpty(this.mapNewRelationSQL) && Checker.isEmpty(this.delOldRelationSQL));
    }
}
