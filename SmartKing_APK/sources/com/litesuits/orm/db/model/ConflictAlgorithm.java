package com.litesuits.orm.db.model;

import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes.dex */
public enum ConflictAlgorithm {
    None(SQLBuilder.BLANK),
    Rollback(" OR ROLLBACK "),
    Abort(" OR ABORT "),
    Fail(" OR FAIL "),
    Ignore(" OR IGNORE "),
    Replace(" OR REPLACE ");

    private String algorithm;

    ConflictAlgorithm(String str) {
        this.algorithm = str;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }
}
