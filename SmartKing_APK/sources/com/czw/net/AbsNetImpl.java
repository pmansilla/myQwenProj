package com.czw.net;

import android.support.v4.app.FragmentActivity;
import com.autonavi.amap.mapcore.AeUtil;
import com.czw.modes.net.OKHttpUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ikidou.reflect.TypeBuilder;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public abstract class AbsNetImpl {
    private static final Gson gson = new Gson();

    public static <D> Callback packCallback(final NetRespListener<NetResult<D>> netRespListener, final Class<D> cls, final FragmentActivity fragmentActivity, final QMUITipDialog qMUITipDialog) {
        return new Callback() { // from class: com.czw.net.AbsNetImpl.1
            public <D> NetResult<List<D>> fromJsonArray(String str, Class<D> cls2) {
                return (NetResult) AbsNetImpl.gson.fromJson(str, TypeBuilder.newInstance(NetResult.class).beginSubType(List.class).addTypeParam((Class) cls2).endSubType().build());
            }

            public <D> NetResult<D> fromJsonObject(String str, Class<D> cls2) {
                return (NetResult) AbsNetImpl.gson.fromJson(str, TypeBuilder.newInstance(NetResult.class).addTypeParam((Class) cls2).build());
            }

            @Override // com.squareup.okhttp.Callback
            public void onFailure(Request request, IOException iOException) {
                NetRespListener.this.onFailure(request, iOException);
                if (fragmentActivity != null) {
                    fragmentActivity.runOnUiThread(new Runnable() { // from class: com.czw.net.AbsNetImpl.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (qMUITipDialog != null) {
                                qMUITipDialog.cancel();
                            }
                        }
                    });
                }
            }

            @Override // com.squareup.okhttp.Callback
            public void onResponse(Response response) throws IOException {
                if (fragmentActivity != null) {
                    fragmentActivity.runOnUiThread(new Runnable() { // from class: com.czw.net.AbsNetImpl.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (qMUITipDialog != null) {
                                qMUITipDialog.cancel();
                            }
                        }
                    });
                }
                int code = response.code();
                if (code != 200) {
                    LogUtil.e("==========================================");
                    LogUtil.e("服务器状态码 " + code + "====================================");
                    LogUtil.e("==========================================");
                    return;
                }
                String string = response.body().string();
                LogUtil.e("debug_response:" + string);
                JsonObject asJsonObject = new JsonParser().parse(string).getAsJsonObject();
                if (cls == null || !asJsonObject.has(AeUtil.ROOT_DATA_PATH_OLD_NAME) || (asJsonObject.has(AeUtil.ROOT_DATA_PATH_OLD_NAME) && asJsonObject.get(AeUtil.ROOT_DATA_PATH_OLD_NAME).isJsonNull())) {
                    LogUtil.d("debug_response 数据为空");
                    NetResult netResult = (NetResult) AbsNetImpl.gson.fromJson(string, NetResult.class);
                    netResult.setJsonString(string);
                    NetRespListener.this.onResponse(netResult);
                    return;
                }
                if (asJsonObject.has(AeUtil.ROOT_DATA_PATH_OLD_NAME) && asJsonObject.get(AeUtil.ROOT_DATA_PATH_OLD_NAME).isJsonObject()) {
                    LogUtil.d("debug_response 单一对象数据");
                    NetResult fromJsonObject = fromJsonObject(string, cls);
                    fromJsonObject.setJsonString(string);
                    NetRespListener.this.onResponse(fromJsonObject);
                    return;
                }
                if (asJsonObject.has(AeUtil.ROOT_DATA_PATH_OLD_NAME) && asJsonObject.get(AeUtil.ROOT_DATA_PATH_OLD_NAME).isJsonArray()) {
                    LogUtil.d("debug_response 列表对象数据");
                    NetResult fromJsonArray = fromJsonArray(string, cls);
                    fromJsonArray.setJsonString(string);
                    NetRespListener.this.onResponse(fromJsonArray);
                }
            }
        };
    }

    protected abstract String loadUrlDomain();

    protected abstract <D> void post(String str, NetRespListener netRespListener, Class<D> cls, OKHttpUtil.Param... paramArr);
}
