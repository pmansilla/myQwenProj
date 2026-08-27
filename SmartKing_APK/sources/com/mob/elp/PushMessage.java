package com.mob.elp;

import android.text.TextUtils;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.elp.d.d;
import com.mob.tools.utils.Hashon;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PushMessage implements Serializable {
    public ClickAction clickAction;
    public String content;
    public HashMap<String, Object> extras;
    public String title;
    public int type;
    public Unfold unfold;
    public String workId;

    /* loaded from: classes.dex */
    public static class ClickAction implements Serializable {
        public int action;
        public String scheme;
        public String url;
    }

    /* loaded from: classes.dex */
    public static class Unfold implements Serializable {
        public String image;
        public ArrayList<String> images;
        public int location;
        public int showType;
    }

    private static int checkInt(HashMap<String, Object> hashMap, String str, int i) {
        if (hashMap != null && hashMap.containsKey(str)) {
            Object obj = hashMap.get(str);
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
        }
        return i;
    }

    private static ArrayList<String> checkList(HashMap<String, Object> hashMap, String str) {
        if (hashMap != null) {
            try {
                if (hashMap.containsKey(str)) {
                    Object obj = hashMap.get(str);
                    if (obj instanceof ArrayList) {
                        return (ArrayList) obj;
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return new ArrayList<>();
    }

    private static HashMap<String, Object> checkMap(HashMap<String, Object> hashMap, String str) {
        if (hashMap != null) {
            try {
                if (hashMap.containsKey(str)) {
                    Object obj = hashMap.get(str);
                    if (obj instanceof HashMap) {
                        return (HashMap) obj;
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return new HashMap<>();
    }

    private static String checkString(HashMap<String, Object> hashMap, String str) {
        if (hashMap == null || !hashMap.containsKey(str)) {
            return "";
        }
        Object obj = hashMap.get(str);
        return obj instanceof String ? (String) obj : "";
    }

    public static PushMessage createPushMessage(String str, String str2) {
        try {
            HashMap fromJson = new Hashon().fromJson(str);
            PushMessage pushMessage = new PushMessage();
            pushMessage.workId = str2;
            pushMessage.title = checkString(fromJson, "title");
            pushMessage.content = checkString(fromJson, "content");
            if (TextUtils.isEmpty(pushMessage.title)) {
                pushMessage.title = MobSDK.getContext().getPackageName();
            }
            pushMessage.type = checkInt(fromJson, "type", 0);
            pushMessage.extras = checkMap(fromJson, "extras");
            pushMessage.clickAction = new ClickAction();
            HashMap<String, Object> checkMap = checkMap(fromJson, "clickAction");
            pushMessage.clickAction.action = checkInt(checkMap, "action", 0);
            pushMessage.clickAction.url = checkString(checkMap, FileDownloadModel.URL);
            pushMessage.clickAction.scheme = checkString(checkMap, "scheme");
            pushMessage.unfold = new Unfold();
            HashMap<String, Object> checkMap2 = checkMap(fromJson, "unfold");
            pushMessage.unfold.location = checkInt(checkMap2, "location", 0);
            pushMessage.unfold.showType = checkInt(checkMap2, "showType", 0);
            if (pushMessage.unfold.showType == 1) {
                HashMap<String, Object> checkMap3 = checkMap(checkMap2, "window");
                pushMessage.unfold.images = checkList(checkMap3, "images");
            } else if (pushMessage.unfold.showType == 2) {
                HashMap<String, Object> checkMap4 = checkMap(checkMap2, "card");
                pushMessage.unfold.image = checkString(checkMap4, PictureConfig.IMAGE);
            } else if (pushMessage.unfold.showType == 3) {
                HashMap<String, Object> checkMap5 = checkMap(checkMap2, "original");
                pushMessage.unfold.image = checkString(checkMap5, PictureConfig.IMAGE);
            } else if (pushMessage.unfold.showType == 4) {
                HashMap<String, Object> checkMap6 = checkMap(checkMap2, "banner");
                pushMessage.unfold.image = checkString(checkMap6, PictureConfig.IMAGE);
            }
            return pushMessage;
        } catch (Throwable th) {
            d.a().a(th);
            return null;
        }
    }
}
