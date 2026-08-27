package com.czw.smartkit.databaseModule.userDevice;

import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class UserDeviceDatabaseUtil {
    private static final UserDeviceDatabaseUtil ourInstance = new UserDeviceDatabaseUtil();
    private LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();

    private UserDeviceDatabaseUtil() {
    }

    public static UserDeviceDatabaseUtil getInstance() {
        return ourInstance;
    }

    public void deleteData(String str) {
        this.liteOrm.delete(WhereBuilder.create(UserDeviceEntity.class).where("uid=?", str));
    }

    public UserDeviceEntity getUserDevice(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(UserDeviceEntity.class).where("uid=?", str));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (UserDeviceEntity) query.get(0);
    }

    public void saveData(UserDeviceEntity userDeviceEntity) {
        this.liteOrm.save(userDeviceEntity);
    }
}
