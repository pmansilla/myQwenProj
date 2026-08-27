package com.czw.smartkit.net;

import com.czw.modes.net.OKHttpUtil;
import com.squareup.okhttp.Callback;

/* loaded from: classes.dex */
public class NetExtraImpl implements Cfg {
    public void requestByGet(String str, Callback callback) {
        OKHttpUtil.getInstance().asyncGetRequest(str, callback);
    }

    public void requestByPost(String str, Callback callback, OKHttpUtil.Param... paramArr) {
        OKHttpUtil.getInstance().asyncPostRequest(str, callback, paramArr);
    }
}
