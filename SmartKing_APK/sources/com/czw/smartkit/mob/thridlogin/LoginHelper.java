package com.czw.smartkit.mob.thridlogin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import com.amap.location.common.model.AmapLoc;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.util.HashMap;

/* loaded from: classes.dex */
public class LoginHelper implements Handler.Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_COMPLETE = 3;
    private static final int MSG_AUTH_ERROR = 2;
    private Context context;
    private OnLoginListener loginListener;
    private String platform;
    private Handler handler = new Handler();
    private ThridLoginCallback thridLoginCallback = null;

    /* loaded from: classes.dex */
    public interface ThridLoginCallback {
        void onCancel();

        void onError();

        void onLogin();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resolveUserInfoFromThrid(Platform platform, HashMap<String, Object> hashMap) {
        LogUtil.e("debug==>平台" + platform.getName());
        LogUtil.e("debug==>信息" + hashMap.toString());
        ThridUserInfo thridUserInfo = new ThridUserInfo();
        if (platform.getName() == QQ.NAME) {
            thridUserInfo.setUserName(hashMap.get("nickname").toString());
            thridUserInfo.setUserIcon(hashMap.get("figureurl_qq_1").toString());
            thridUserInfo.setSex(hashMap.get("gender").toString().equals("男") ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_FUSED);
        } else if (platform.getName() == Wechat.NAME) {
            thridUserInfo.setUserName(hashMap.get("nickname").toString());
            thridUserInfo.setUserIcon(hashMap.get("headimgurl").toString());
            thridUserInfo.setSex(hashMap.get("sex").toString());
            thridUserInfo.setOpenId(hashMap.get("openid").toString());
        } else {
            platform.getName();
            String str = SinaWeibo.NAME;
        }
        LogUtil.e(new Gson().toJson(thridUserInfo));
        LogUtil.e("debug_userInfo-->" + thridUserInfo.toString());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x006b, code lost:
    
        return false;
     */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean handleMessage(android.os.Message r4) {
        /*
            r3 = this;
            android.content.Context r0 = r3.context
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r1.append(r2)
            int r2 = r4.what
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            android.widget.Toast r0 = android.widget.Toast.makeText(r0, r1, r2)
            r0.show()
            int r0 = r4.what
            switch(r0) {
                case 1: goto L60;
                case 2: goto L3a;
                case 3: goto L23;
                default: goto L22;
            }
        L22:
            goto L6b
        L23:
            java.lang.Object r4 = r4.obj
            java.lang.Object[] r4 = (java.lang.Object[]) r4
            r0 = r4[r2]
            java.lang.String r0 = (java.lang.String) r0
            r1 = 1
            r4 = r4[r1]
            java.util.HashMap r4 = (java.util.HashMap) r4
            com.czw.smartkit.mob.thridlogin.OnLoginListener r1 = r3.loginListener
            if (r1 == 0) goto L6b
            com.czw.smartkit.mob.thridlogin.OnLoginListener r1 = r3.loginListener
            r1.onLogin(r0, r4)
            goto L6b
        L3a:
            java.lang.Object r4 = r4.obj
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "caught error: "
            r0.append(r1)
            java.lang.String r1 = r4.getMessage()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.content.Context r1 = r3.context
            android.widget.Toast r0 = android.widget.Toast.makeText(r1, r0, r2)
            r0.show()
            r4.printStackTrace()
            goto L6b
        L60:
            android.content.Context r4 = r3.context
            java.lang.String r0 = "canceled"
            android.widget.Toast r4 = android.widget.Toast.makeText(r4, r0, r2)
            r4.show()
        L6b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.mob.thridlogin.LoginHelper.handleMessage(android.os.Message):boolean");
    }

    public LoginHelper login(Context context, String str) {
        Platform platform = ShareSDK.getPlatform(str);
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() { // from class: com.czw.smartkit.mob.thridlogin.LoginHelper.1
            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onCancel(Platform platform2, int i) {
                LogUtil.e("debug onCancel==>=" + i + "=" + platform2.toString());
                if (i == 8) {
                    Message message = new Message();
                    message.what = 1;
                    message.arg2 = i;
                    message.obj = platform2;
                    LoginHelper.this.handler.sendMessage(message);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onComplete(Platform platform2, int i, HashMap<String, Object> hashMap) {
                LogUtil.e("debug onComplete==>" + hashMap.toString() + "=" + i + "=");
                if (i == 8) {
                    LoginHelper.this.resolveUserInfoFromThrid(platform2, hashMap);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onError(Platform platform2, int i, Throwable th) {
                LogUtil.e("debug onError==>" + th.toString() + "=" + i + "=" + platform2.toString());
                if (i == 8) {
                    Message message = new Message();
                    message.what = 2;
                    message.arg2 = i;
                    message.obj = th;
                    LoginHelper.this.handler.sendMessage(message);
                }
                th.printStackTrace();
            }
        });
        platform.showUser(null);
        return this;
    }

    public LoginHelper setOnLoginListener(OnLoginListener onLoginListener) {
        this.loginListener = onLoginListener;
        return this;
    }

    public void setThridLoginCallback(ThridLoginCallback thridLoginCallback) {
        this.thridLoginCallback = thridLoginCallback;
    }
}
