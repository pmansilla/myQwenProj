package com.czw.smartkit.databaseModule.htxOta;

import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class HtxOtaFailureEntityDatabaseUtil {
    private static final HtxOtaFailureEntityDatabaseUtil ourInstance = new HtxOtaFailureEntityDatabaseUtil();
    private LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();

    private HtxOtaFailureEntityDatabaseUtil() {
    }

    public static HtxOtaFailureEntityDatabaseUtil getInstance() {
        return ourInstance;
    }

    public void deleteData(String str) {
        this.liteOrm.delete(WhereBuilder.create(HtxOtaFailureEntity.class).where("uid=?", str));
    }

    public HtxOtaFailureEntity getUserDevice(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(HtxOtaFailureEntity.class).where("uid=?", str));
        if (query == null || query.size() <= 0) {
            return null;
        }
        return (HtxOtaFailureEntity) query.get(0);
    }

    public void saveData(HtxOtaFailureEntity htxOtaFailureEntity) {
        this.liteOrm.save(htxOtaFailureEntity);
    }
}
