package ycble.runchinaup.aider;

import org.apache.commons.lang.CharUtils;

/* loaded from: classes2.dex */
public enum MsgType {
    QQ,
    WECHAT,
    FACEBOOK,
    TWITTER,
    WHATSAPP,
    LINE,
    SKPE,
    QIANNIU,
    KaKaoTalk,
    Messenger,
    LinkedIn,
    DingDind,
    Viber,
    VK,
    SINA_WEIBO,
    Instagram,
    WECHAT_VOICE,
    WECHAT_VIDEO;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static MsgType pck2MsgType(String str) {
        char c;
        switch (str.hashCode()) {
            case -2099846372:
                if (str.equals("com.skype.raider")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1938583537:
                if (str.equals("com.vkontakte.android")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -1651733025:
                if (str.equals("com.viber.voip")) {
                    c = CharUtils.CR;
                    break;
                }
                c = 65535;
                break;
            case -1547699361:
                if (str.equals("com.whatsapp")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1521143749:
                if (str.equals("jp.naver.line.android")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -973170826:
                if (str.equals("com.tencent.mm")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -761275646:
                if (str.equals("com.taobao.qianniu")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -662003450:
                if (str.equals("com.instagram.android")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -112833610:
                if (str.equals("com.tencent.timTIM")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 10619783:
                if (str.equals("com.twitter.android")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 361910168:
                if (str.equals("com.tencent.mobileqq")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 714499313:
                if (str.equals("com.facebook.katana")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 908140028:
                if (str.equals("com.facebook.orca")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1153658444:
                if (str.equals("com.linkedin.android")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1249065348:
                if (str.equals("com.kakao.talk")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1335515207:
                if (str.equals("com.alibaba.android.rimet")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1456713281:
                if (str.equals("com.skype.rover")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1536737232:
                if (str.equals("com.sina.weibo")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return QQ;
            case 2:
                return WECHAT;
            case 3:
            case 4:
                return SKPE;
            case 5:
                return WHATSAPP;
            case 6:
                return FACEBOOK;
            case 7:
                return TWITTER;
            case '\b':
                return LINE;
            case '\t':
                return QIANNIU;
            case '\n':
                return KaKaoTalk;
            case 11:
                return LinkedIn;
            case '\f':
                return Messenger;
            case '\r':
                return Viber;
            case 14:
                return Instagram;
            case 15:
                return DingDind;
            case 16:
                return VK;
            case 17:
                return SINA_WEIBO;
            default:
                return null;
        }
    }
}
