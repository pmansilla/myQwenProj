package com.mob.mcl.a;

import android.text.TextUtils;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.Hashon;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ApcInternalData.java */
/* loaded from: classes.dex */
public class c implements Serializable {
    public String a;
    public String b;
    public String c;
    public String d;
    public int e;
    public int f;
    public int g;

    public static ArrayList<KVPair<String>> a(String str) {
        HashMap fromJson;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str) && (fromJson = new Hashon().fromJson(str)) != null && !fromJson.isEmpty()) {
            for (Map.Entry entry : fromJson.entrySet()) {
                if (entry != null) {
                    arrayList.add(new KVPair<>((String) entry.getKey(), entry.getValue()));
                }
            }
        }
        return arrayList;
    }
}
