package ycnet.runchinaup.core.abs;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import ycnet.runchinaup.core.OkHttpCore;

/* loaded from: classes2.dex */
public abstract class IRequest {
    /* JADX INFO: Access modifiers changed from: protected */
    public void get(String str, IDataParser iDataParser) {
        OkHttpCore.getRequest(str, iDataParser.getCallBack());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void post(String str, IDataParser iDataParser, FormBody formBody) {
        OkHttpCore.postRequest(str, iDataParser.getCallBack(), formBody);
    }

    protected void post(String str, IDataParser iDataParser, RequestBody requestBody) {
        OkHttpCore.postRequest(str, iDataParser.getCallBack(), requestBody);
    }
}
