package com.amap.api.mapcore.util;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.mob.guard.MobGuard;
import com.tencent.bugly.BuglyStrategy;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: CoreUtil.java */
/* loaded from: classes.dex */
public class gx {
    private static String[] a = {"com.amap.api.trace", "com.amap.api.trace.core"};

    public static int a(List<LatLng> list) {
        int i = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }
        int i2 = 0;
        while (i < list.size() - 1) {
            LatLng latLng = list.get(i);
            i++;
            LatLng latLng2 = list.get(i);
            if (latLng == null || latLng2 == null) {
                return i2;
            }
            i2 = (int) (i2 + AMapUtils.calculateLineDistance(latLng, latLng2));
        }
        return i2;
    }

    protected static void a(int i, String str, String str2) throws gu {
        if (i != 0) {
            switch (i) {
                case 10000:
                    return;
                case GLMapStaticValue.AM_CALLBACK_CHANGEMAPLOGO /* 10001 */:
                    throw new gu("用户key不正确或过期");
                case GLMapStaticValue.AM_CALLBACK_NEED_NEXTFRAME /* 10002 */:
                    throw new gu("请求服务不存在");
                case GLMapStaticValue.AM_CALLBACK_INDOOR_NETWORK_ERR /* 10003 */:
                    throw new gu("访问已超出日访问量");
                case 10004:
                    throw new gu("用户访问过于频繁");
                case 10005:
                    throw new gu("用户IP无效");
                case 10006:
                    throw new gu("用户域名无效");
                case 10007:
                    throw new gu("用户签名未通过");
                case 10008:
                    throw new gu("用户MD5安全码未通过");
                case 10009:
                    throw new gu("请求key与绑定平台不符");
                case 10010:
                    throw new gu("IP访问超限");
                case 10011:
                    throw new gu("服务不支持https请求");
                case 10012:
                    throw new gu("权限不足，服务请求被拒绝");
                case 10013:
                    throw new gu("开发者删除了key，key被删除后无法正常使用");
                default:
                    switch (i) {
                        case 20000:
                            throw new gu("请求参数非法");
                        case 20001:
                            throw new gu("缺少必填参数");
                        case MobGuard.SDK_VERSION_CODE /* 20002 */:
                            throw new gu("请求协议非法");
                        case 20003:
                            throw new gu("其他未知错误");
                        default:
                            switch (i) {
                                case BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH /* 30000 */:
                                    throw new gu("请求服务响应错误");
                                case 30001:
                                    throw new gu("引擎返回数据异常");
                                case 30002:
                                    throw new gu("服务端请求链接超时");
                                case 30003:
                                    throw new gu("读取服务结果超时");
                                default:
                                    throw new gu(str);
                            }
                    }
            }
        }
    }

    public static void a(String str, String str2) throws gu {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("errcode")) {
                a(jSONObject.getInt("errcode"), jSONObject.getString("errmsg"), str2);
                return;
            }
            if (jSONObject.has("status") && jSONObject.has("infocode")) {
                String string = jSONObject.getString("status");
                int i = jSONObject.getInt("infocode");
                if (AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(string)) {
                    return;
                }
                String string2 = jSONObject.getString("info");
                if (AmapLoc.RESULT_TYPE_GPS.equals(string)) {
                    a(i, string2, str2);
                }
            }
        } catch (JSONException unused) {
            throw new gu(AMapException.ERROR_PROTOCOL);
        }
    }
}
