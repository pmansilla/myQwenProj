package cn.sharesdk.framework;

import android.os.Handler;
import com.mob.commons.eventrecoder.EventRecorder;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/* compiled from: ShareSDKCore.java */
/* loaded from: classes.dex */
public class e {
    public static ArrayList<Platform> a() {
        ArrayList<Platform> c = c();
        a(c);
        return c;
    }

    public static HashMap<Integer, HashMap<String, Object>> a(HashMap<String, Object> hashMap) {
        ArrayList arrayList;
        int i;
        if (hashMap == null || hashMap.size() <= 0 || (arrayList = (ArrayList) hashMap.get("fakelist")) == null) {
            return null;
        }
        HashMap<Integer, HashMap<String, Object>> hashMap2 = new HashMap<>();
        EventRecorder.addBegin("ShareSDK", "parseDevInfo");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> hashMap3 = (HashMap) it.next();
            if (hashMap3 != null) {
                try {
                    i = ResHelper.parseInt(String.valueOf(hashMap3.get("snsplat")));
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                    i = -1;
                }
                if (i != -1) {
                    hashMap2.put(Integer.valueOf(i), hashMap3);
                }
            }
        }
        EventRecorder.addEnd("ShareSDK", "parseDevInfo");
        return hashMap2;
    }

    public static void a(int i, Platform platform) {
        cn.sharesdk.framework.b.b.d dVar = new cn.sharesdk.framework.b.b.d();
        switch (i) {
            case 1:
                dVar.a = "SHARESDK_ENTER_SHAREMENU";
                break;
            case 2:
                dVar.a = "SHARESDK_CANCEL_SHAREMENU";
                break;
            case 3:
                dVar.a = "SHARESDK_EDIT_SHARE";
                break;
            case 4:
                dVar.a = "SHARESDK_FAILED_SHARE";
                break;
            case 5:
                dVar.a = "SHARESDK_CANCEL_SHARE";
                break;
        }
        if (platform != null) {
            dVar.b = platform.getPlatformId();
        }
        cn.sharesdk.framework.b.d a = cn.sharesdk.framework.b.d.a();
        if (a != null) {
            a.a(dVar);
        }
    }

    public static void a(Handler handler) {
        cn.sharesdk.framework.b.d a = cn.sharesdk.framework.b.d.a();
        if (a != null) {
            a.a(handler);
            a.b();
        }
    }

    public static void a(String str, int i) {
        cn.sharesdk.framework.b.d a = cn.sharesdk.framework.b.d.a();
        if (a == null) {
            return;
        }
        cn.sharesdk.framework.b.b.a aVar = new cn.sharesdk.framework.b.b.a();
        aVar.b = str;
        aVar.a = i;
        a.a(aVar);
    }

    public static void a(ArrayList<Platform> arrayList) {
        if (arrayList == null) {
            return;
        }
        Collections.sort(arrayList, new Comparator<Platform>() { // from class: cn.sharesdk.framework.e.1
            @Override // java.util.Comparator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public int compare(Platform platform, Platform platform2) {
                return platform.getSortId() != platform2.getSortId() ? platform.getSortId() - platform2.getSortId() : platform.getPlatformId() - platform2.getPlatformId();
            }
        });
    }

    public static void a(boolean z) {
        cn.sharesdk.framework.authorize.e c = cn.sharesdk.framework.authorize.e.c();
        if (c != null) {
            c.a(z);
        }
    }

    public static boolean b() {
        return cn.sharesdk.framework.authorize.e.c().a();
    }

    private static ArrayList<Platform> c() {
        ArrayList<Platform> arrayList = new ArrayList<>();
        for (String str : new String[]{"cn.sharesdk.douban.Douban", "cn.sharesdk.evernote.Evernote", "cn.sharesdk.facebook.Facebook", "cn.sharesdk.renren.Renren", "cn.sharesdk.sina.weibo.SinaWeibo", "cn.sharesdk.kaixin.KaiXin", "cn.sharesdk.linkedin.LinkedIn", "cn.sharesdk.system.email.Email", "cn.sharesdk.system.text.ShortMessage", "cn.sharesdk.tencent.qq.QQ", "cn.sharesdk.tencent.qzone.QZone", "cn.sharesdk.tencent.weibo.TencentWeibo", "cn.sharesdk.twitter.Twitter", "cn.sharesdk.wechat.friends.Wechat", "cn.sharesdk.wechat.moments.WechatMoments", "cn.sharesdk.wechat.favorite.WechatFavorite", "cn.sharesdk.youdao.YouDao", "cn.sharesdk.google.GooglePlus", "cn.sharesdk.foursquare.FourSquare", "cn.sharesdk.pinterest.Pinterest", "cn.sharesdk.flickr.Flickr", "cn.sharesdk.tumblr.Tumblr", "cn.sharesdk.dropbox.Dropbox", "cn.sharesdk.vkontakte.VKontakte", "cn.sharesdk.instagram.Instagram", "cn.sharesdk.yixin.friends.Yixin", "cn.sharesdk.yixin.moments.YixinMoments", "cn.sharesdk.mingdao.Mingdao", "cn.sharesdk.line.Line", "cn.sharesdk.kakao.story.KakaoStory", "cn.sharesdk.kakao.talk.KakaoTalk", "cn.sharesdk.whatsapp.WhatsApp", "cn.sharesdk.pocket.Pocket", "cn.sharesdk.instapaper.Instapaper", "cn.sharesdk.facebookmessenger.FacebookMessenger", "cn.sharesdk.alipay.friends.Alipay", "cn.sharesdk.alipay.moments.AlipayMoments", "cn.sharesdk.dingding.friends.Dingding", "cn.sharesdk.youtube.Youtube", "cn.sharesdk.meipai.Meipai", "cn.sharesdk.telegram.Telegram", "cn.sharesdk.cmcc.Cmcc", "cn.sharesdk.reddit.Reddit", "cn.sharesdk.telecom.Telecom", "cn.sharesdk.accountkit.Accountkit", "cn.sharesdk.douyin.Douyin"}) {
            try {
                arrayList.add((Platform) ReflectHelper.newInstance(ReflectHelper.importClass(str), new Object[0]));
            } catch (Throwable unused) {
            }
        }
        return arrayList;
    }
}
