package ycnet.runchinaup.core.ycimpl.parser;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.amap.api.maps.model.MyLocationStyle;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import okhttp3.Call;
import okhttp3.Response;
import ycnet.runchinaup.core.abs.IDataParser;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;
import ycnet.runchinaup.log.ycNetLog;

/* loaded from: classes2.dex */
public class YCResponseParser extends IDataParser {
    private Class<?>[] clazz;
    private YCResponseListener ycResponseListener;

    public YCResponseParser(YCResponseListener yCResponseListener, Class<?>... clsArr) {
        this.ycResponseListener = null;
        this.clazz = clsArr;
        this.ycResponseListener = yCResponseListener;
    }

    private synchronized void handResponse(Call call, Response response) throws IOException {
        int code = response.code();
        String string = response.body().string();
        ycNetLog.e("                                            ");
        ycNetLog.e("=======NET网络响应=======START================");
        ycNetLog.e("     " + call.request().toString());
        ycNetLog.e("     " + response.toString());
        ycNetLog.e("     " + string);
        if (code == 200) {
            if (this.ycResponseListener != null) {
                this.ycResponseListener.onSuccessWithJson(string);
            }
            if (this.clazz != null && this.clazz.length > 0) {
                withResponse(call, string);
            }
        } else if (code >= 500 || code < 400) {
            if (code < 600 && code >= 500 && this.ycResponseListener != null) {
                this.ycResponseListener.onError(code, "remote service inner error");
            }
        } else if (this.ycResponseListener != null) {
            this.ycResponseListener.onError(code, "app request error");
        }
        ycNetLog.e("=======NET网络响应=======END================\n");
    }

    private void withResponse(Call call, String str) {
        Type parameterizedTypeImpl;
        try {
            JSONObject parseObject = JSONObject.parseObject(str);
            if (!parseObject.containsKey(MyLocationStyle.ERROR_CODE) || (!parseObject.containsKey("message") && !parseObject.containsKey(NotificationCompat.CATEGORY_MESSAGE))) {
                ycNetLog.e(call.request().toString() + "非 yc的标准数据json格式，请从onSuccessWithJson()方法里面获取回调数据！！！");
                return;
            }
            int intValue = parseObject.getInteger(MyLocationStyle.ERROR_CODE).intValue();
            String string = parseObject.containsKey("message") ? parseObject.getString("message") : parseObject.getString(NotificationCompat.CATEGORY_MESSAGE);
            if (intValue != 0) {
                if (this.ycResponseListener != null) {
                    this.ycResponseListener.onError(intValue, string);
                    return;
                }
                return;
            }
            if (this.clazz.length > 1) {
                int length = this.clazz.length - 1;
                parameterizedTypeImpl = null;
                while (length > 0) {
                    Type[] typeArr = new Type[1];
                    if (parameterizedTypeImpl == null) {
                        parameterizedTypeImpl = this.clazz[length];
                    }
                    typeArr[0] = parameterizedTypeImpl;
                    ParameterizedTypeImpl parameterizedTypeImpl2 = new ParameterizedTypeImpl(typeArr, null, this.clazz[length - 1]);
                    length--;
                    parameterizedTypeImpl = parameterizedTypeImpl2;
                }
            } else {
                parameterizedTypeImpl = new ParameterizedTypeImpl(new Type[1], null, this.clazz[0]);
            }
            if (this.ycResponseListener != null) {
                this.ycResponseListener.onSuccess(JSON.parseObject(str, parameterizedTypeImpl, new Feature[0]));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (this.ycResponseListener != null) {
                ycNetLog.e("json解析异常");
                this.ycResponseListener.onError(IDataParser.CODE_JSON_RESOVE_EXCEPTION, "json解析异常");
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
            if (this.ycResponseListener == null || TextUtils.isEmpty(e3.getMessage())) {
                return;
            }
            this.ycResponseListener.onError(IDataParser.CODE_NET_UNKNOWN_EXCEPTION, "\n" + e3.getMessage());
        }
    }

    @Override // ycnet.runchinaup.core.abs.IDataParser
    public void onFailure(Call call, IOException iOException) {
        if (iOException instanceof UnknownHostException) {
            ycNetLog.e("网络不可用的状态吧");
            if (this.ycResponseListener != null) {
                this.ycResponseListener.onError(IDataParser.CODE_NET_UNAVAILABLE, " 网络连接不可用");
            }
        }
        iOException.printStackTrace();
    }

    @Override // ycnet.runchinaup.core.abs.IDataParser
    public void parser(Call call, Response response) throws IOException {
        handResponse(call, response);
    }
}
