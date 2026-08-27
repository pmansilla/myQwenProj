package com.czw.smartkit.entity;

import cn.smssdk.SMSSDK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class CountryEntity implements Serializable {
    public String code;
    public String name;

    public CountryEntity(String str) {
        this.name = this.name;
        this.code = str;
    }

    public CountryEntity(String str, String str2) {
        this.name = str;
        this.code = str2;
    }

    public static ArrayList<CountryEntity> getCountry() {
        ArrayList<CountryEntity> arrayList = new ArrayList<>();
        Iterator<Map.Entry<Character, ArrayList<String[]>>> it = SMSSDK.getGroupedCountryList().entrySet().iterator();
        while (it.hasNext()) {
            Iterator<String[]> it2 = it.next().getValue().iterator();
            while (it2.hasNext()) {
                String[] next = it2.next();
                arrayList.add(new CountryEntity(next[0], next[1]));
            }
        }
        return arrayList;
    }
}
