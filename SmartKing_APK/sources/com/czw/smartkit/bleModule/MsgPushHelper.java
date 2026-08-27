package com.czw.smartkit.bleModule;

import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.utils.LogUtil;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.aider.MsgType;
import ycble.runchinaup.aider.callback.MsgCallback;

/* loaded from: classes.dex */
public class MsgPushHelper extends MsgCallback {
    private static MsgPushHelper msgPushHelper;
    private BleManager bleManager = BleManager.getBleManager();

    private MsgPushHelper() {
    }

    public static MsgPushHelper getMsgPushHelper() {
        synchronized (Void.class) {
            if (msgPushHelper == null) {
                synchronized (Void.class) {
                    msgPushHelper = new MsgPushHelper();
                }
            }
        }
        return msgPushHelper;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // ycble.runchinaup.aider.callback.MsgCallback
    public void onAppMsgReceive(String str, MsgType msgType, String str2, String str3) {
        int i;
        LogUtil.e(msgType + FileUriModel.SCHEME + str2 + FileUriModel.SCHEME + str3);
        RemindSetting read = SharePreferenceRemind.read();
        StringBuilder sb = new StringBuilder();
        sb.append("debug==>");
        sb.append(read.toString());
        LogUtil.e(sb.toString());
        if (msgType == null) {
            return;
        }
        switch (msgType) {
            case QQ:
                if (read.qqEnable) {
                    i = 3;
                    break;
                }
                i = -1;
                break;
            case WECHAT:
                if (read.wechatEnable) {
                    i = 2;
                    break;
                }
                i = -1;
                break;
            case WHATSAPP:
                if (read.whatappEnable) {
                    i = 7;
                    break;
                }
                i = -1;
                break;
            case FACEBOOK:
                if (read.facebookEnable) {
                    i = 5;
                    break;
                }
                i = -1;
                break;
            case TWITTER:
                if (read.twitterEnable) {
                    i = 6;
                    break;
                }
                i = -1;
                break;
            case LinkedIn:
                if (read.linkedEnable) {
                    i = 8;
                    break;
                }
                i = -1;
                break;
            default:
                i = -1;
                break;
        }
        if (i == -1) {
            return;
        }
        this.bleManager.writeMuliteData(DataStruct.createPushMsg(str2 + str3, i));
    }

    @Override // ycble.runchinaup.aider.callback.MsgCallback
    public void onMessageReceive(String str, String str2, String str3) {
        LogUtil.e("debug===接收到短信::" + str2 + " messageContent:" + str3);
        BleManager.getBleManager().writeMuliteData(DataStruct.createPushMsg(str2 + ":" + str3, 1));
    }

    @Override // ycble.runchinaup.aider.callback.MsgCallback
    public void onPhoneInComing(String str, String str2, int i) {
        if (i == 0) {
            BleManager.getBleManager().writeMuliteData(DataStruct.createPushMsg(str2, 0));
            LogUtil.e("debug===来电铃声响了:" + str2);
        }
    }
}
