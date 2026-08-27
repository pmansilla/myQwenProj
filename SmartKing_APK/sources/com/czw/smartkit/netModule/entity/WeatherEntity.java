package com.czw.smartkit.netModule.entity;

import android.text.TextUtils;
import com.alibaba.fastjson.parser.JSONLexer;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import java.io.Serializable;
import org.apache.commons.lang.CharUtils;

/* loaded from: classes.dex */
public class WeatherEntity implements Serializable {
    private String cityName;
    private String maxTemperature;
    private String minTemperature;
    private String temperature;
    private String weather;
    private String weatherCode;
    private String wind;
    private String windDirection;

    public String getCityName() {
        return this.cityName;
    }

    public String getMaxTemperature() {
        return this.maxTemperature;
    }

    public String getMinTemperature() {
        return this.minTemperature;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public String getWeather() {
        boolean isEmpty = TextUtils.isEmpty(this.weather);
        int i = R.string.weather_DuoYun;
        if (!isEmpty) {
            String str = this.weather;
            char c = 65535;
            switch (str.hashCode()) {
                case -1236115480:
                    if (str.equals("雷阵雨伴有冰雹")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1229291873:
                    if (str.equals("暴雨到大暴雨")) {
                        c = 25;
                        break;
                    }
                    break;
                case 26228:
                    if (str.equals("晴")) {
                        c = 0;
                        break;
                    }
                    break;
                case 38452:
                    if (str.equals("阴")) {
                        c = 3;
                        break;
                    }
                    break;
                case 38654:
                    if (str.equals("雾")) {
                        c = 19;
                        break;
                    }
                    break;
                case 38718:
                    if (str.equals("霾")) {
                        c = '!';
                        break;
                    }
                    break;
                case 659035:
                    if (str.equals("中雨")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 659037:
                    if (str.equals("中雪")) {
                        c = 16;
                        break;
                    }
                    break;
                case 687245:
                    if (str.equals("冻雨")) {
                        c = 20;
                        break;
                    }
                    break;
                case 727223:
                    if (str.equals("多云")) {
                        c = 1;
                        break;
                    }
                    break;
                case 746145:
                    if (str.equals("大雨")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 746147:
                    if (str.equals("大雪")) {
                        c = 17;
                        break;
                    }
                    break;
                case 769209:
                    if (str.equals("小雨")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 769211:
                    if (str.equals("小雪")) {
                        c = 15;
                        break;
                    }
                    break;
                case 808877:
                    if (str.equals("扬沙")) {
                        c = 31;
                        break;
                    }
                    break;
                case 853684:
                    if (str.equals("暴雨")) {
                        c = 11;
                        break;
                    }
                    break;
                case 853686:
                    if (str.equals("暴雪")) {
                        c = 18;
                        break;
                    }
                    break;
                case 892010:
                    if (str.equals("浮尘")) {
                        c = 30;
                        break;
                    }
                    break;
                case 1230675:
                    if (str.equals("阵雨")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1230677:
                    if (str.equals("阵雪")) {
                        c = 14;
                        break;
                    }
                    break;
                case 22786587:
                    if (str.equals("大暴雨")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 27473909:
                    if (str.equals("沙尘暴")) {
                        c = 21;
                        break;
                    }
                    break;
                case 37872057:
                    if (str.equals("雨夹雪")) {
                        c = 7;
                        break;
                    }
                    break;
                case 38370442:
                    if (str.equals("雷阵雨")) {
                        c = 5;
                        break;
                    }
                    break;
                case 617172868:
                    if (str.equals("中到大雨")) {
                        c = 23;
                        break;
                    }
                    break;
                case 617172870:
                    if (str.equals("中到大雪")) {
                        c = 28;
                        break;
                    }
                    break;
                case 700993117:
                    if (str.equals("大到暴雨")) {
                        c = 24;
                        break;
                    }
                    break;
                case 700993119:
                    if (str.equals("大到暴雪")) {
                        c = 29;
                        break;
                    }
                    break;
                case 722962972:
                    if (str.equals("小到中雨")) {
                        c = 22;
                        break;
                    }
                    break;
                case 722962974:
                    if (str.equals("小到中雪")) {
                        c = 27;
                        break;
                    }
                    break;
                case 753718907:
                    if (str.equals("强沙尘暴")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 895811842:
                    if (str.equals("特大暴雨")) {
                        c = CharUtils.CR;
                        break;
                    }
                    break;
                case 1204232695:
                    if (str.equals("大暴雨到特大暴雨")) {
                        c = JSONLexer.EOI;
                        break;
                    }
                    break;
            }
            if (c != 0) {
                switch (c) {
                    case 3:
                        i = R.string.weather_Yin;
                        break;
                    case 4:
                        i = R.string.weather_ZhenYu;
                        break;
                    case 5:
                        i = R.string.weather_LeiZhenYu;
                        break;
                    case 6:
                        i = R.string.weather_LeiZhenYuWithBingBao;
                        break;
                    case 7:
                        i = R.string.weather_YuJiaXue;
                        break;
                    case '\b':
                        i = R.string.weather_XiaoYu;
                        break;
                    case '\t':
                        i = R.string.weather_ZhongYu;
                        break;
                    case '\n':
                        i = R.string.weather_DaYu;
                        break;
                    case 11:
                        i = R.string.weather_BaoYu;
                        break;
                    case '\f':
                        i = R.string.weather_DaBaoYu;
                        break;
                    case '\r':
                        i = R.string.weather_TeDaBaoYu;
                        break;
                    case 14:
                        i = R.string.weather_ZhenXue;
                        break;
                    case 15:
                        i = R.string.weather_XiaoXue;
                        break;
                    case 16:
                        i = R.string.weather_ZhongXue;
                        break;
                    case 17:
                        i = R.string.weather_DaXue;
                        break;
                    case 18:
                        i = R.string.weather_BaoXue;
                        break;
                    case 19:
                        i = R.string.weather_Wu;
                        break;
                    case 20:
                        i = R.string.weather_BingYu;
                        break;
                    case 21:
                        i = R.string.weather_ShaChenBao;
                        break;
                    case 22:
                        i = R.string.weather_XioaDaoZhongYu;
                        break;
                    case 23:
                        i = R.string.weather_ZhongDaoDaYu;
                        break;
                    case 24:
                        i = R.string.weather_DaDaoBaoYu;
                        break;
                    case 25:
                        i = R.string.weather_DaoYuDaoDaBaoYu;
                        break;
                    case 26:
                        i = R.string.weather_DaDaoYuDaoTeDaBaoYu;
                        break;
                    case 27:
                        i = R.string.weather_XiaoDaoZhongXue;
                        break;
                    case 28:
                        i = R.string.weather_ZhongDaoDaXue;
                        break;
                    case 29:
                        i = R.string.weather_DaDaoBaoXue;
                        break;
                    case 30:
                        i = R.string.weather_FuChen;
                        break;
                    case 31:
                        i = R.string.weather_YangSha;
                        break;
                    case ' ':
                        i = R.string.weather_QiangShaChenBao;
                        break;
                    case '!':
                        i = R.string.weather_Mai;
                        break;
                }
            } else {
                i = R.string.weather_qing;
            }
        }
        return MainApplication.getApp().getResources().getString(i);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0025. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x001f. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0022. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002f A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0032 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0035 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0038 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003b A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003e A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getWeatherCode() {
        /*
            r3 = this;
            java.lang.String r0 = r3.weatherCode
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto Lb
            java.lang.String r0 = "0"
            goto Ld
        Lb:
            java.lang.String r0 = r3.weatherCode
        Ld:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            int r1 = r1.intValue()
            r2 = 53
            if (r1 == r2) goto L4d
            switch(r1) {
                case 0: goto L4a;
                case 1: goto L47;
                case 2: goto L44;
                default: goto L1c;
            }
        L1c:
            switch(r1) {
                case 4: goto L41;
                case 5: goto L41;
                default: goto L1f;
            }
        L1f:
            switch(r1) {
                case 7: goto L3e;
                case 8: goto L3b;
                case 9: goto L38;
                default: goto L22;
            }
        L22:
            switch(r1) {
                case 14: goto L35;
                case 15: goto L32;
                case 16: goto L2f;
                case 17: goto L2f;
                case 18: goto L2c;
                case 19: goto L3e;
                default: goto L25;
            }
        L25:
            switch(r1) {
                case 21: goto L3e;
                case 22: goto L3b;
                case 23: goto L38;
                case 24: goto L38;
                case 25: goto L38;
                case 26: goto L35;
                case 27: goto L32;
                case 28: goto L2f;
                default: goto L28;
            }
        L28:
            switch(r1) {
                case 301: goto L3e;
                case 302: goto L35;
                default: goto L2b;
            }
        L2b:
            goto L4f
        L2c:
            java.lang.String r0 = "12"
            goto L4f
        L2f:
            java.lang.String r0 = "9"
            goto L4f
        L32:
            java.lang.String r0 = "10"
            goto L4f
        L35:
            java.lang.String r0 = "11"
            goto L4f
        L38:
            java.lang.String r0 = "6"
            goto L4f
        L3b:
            java.lang.String r0 = "7"
            goto L4f
        L3e:
            java.lang.String r0 = "8"
            goto L4f
        L41:
            java.lang.String r0 = "4"
            goto L4f
        L44:
            java.lang.String r0 = "0"
            goto L4f
        L47:
            java.lang.String r0 = "2"
            goto L4f
        L4a:
            java.lang.String r0 = "1"
            goto L4f
        L4d:
            java.lang.String r0 = "13"
        L4f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.netModule.entity.WeatherEntity.getWeatherCode():java.lang.String");
    }

    public String getWind() {
        return this.wind;
    }

    public String getWindDirection() {
        return this.windDirection;
    }

    public void setCityName(String str) {
        this.cityName = str;
    }

    public void setMaxTemperature(String str) {
        this.maxTemperature = str;
    }

    public void setMinTemperature(String str) {
        this.minTemperature = str;
    }

    public void setTemperature(String str) {
        this.temperature = str;
    }

    public void setWeather(String str) {
        this.weather = str;
    }

    public void setWeatherCode(String str) {
        this.weatherCode = str;
    }

    public void setWind(String str) {
        this.wind = str;
    }

    public void setWindDirection(String str) {
        this.windDirection = str;
    }

    public String toString() {
        return "WeatherEntity{cityName='" + this.cityName + "', maxTemperature='" + this.maxTemperature + "', minTemperature='" + this.minTemperature + "', temperature='" + this.temperature + "', wind='" + this.wind + "', windDirection='" + this.windDirection + "', weather='" + this.weather + "', weatherCode='" + this.weatherCode + "'}";
    }
}
