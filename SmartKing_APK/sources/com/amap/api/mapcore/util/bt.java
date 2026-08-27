package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: OfflineMapDownloadList.java */
/* loaded from: classes.dex */
public class bt {
    public ArrayList<OfflineMapProvince> a = new ArrayList<>();
    private ce b;
    private Context c;

    public bt(Context context, Handler handler) {
        this.c = context;
        this.b = ce.a(context);
    }

    private void a(bo boVar, OfflineMapCity offlineMapCity) {
        int b = boVar.c().b();
        if (boVar.c().equals(boVar.a)) {
            b(boVar.x());
        } else {
            if (boVar.c().equals(boVar.f)) {
                cm.a("saveJSONObjectToFile  CITY " + boVar.getCity());
                b(boVar);
                boVar.x().c();
            }
            if (a(boVar.getcompleteCode(), boVar.c().b())) {
                a(boVar.x());
            }
        }
        offlineMapCity.setState(b);
        offlineMapCity.setCompleteCode(boVar.getcompleteCode());
    }

    private void a(bo boVar, OfflineMapProvince offlineMapProvince) {
        bz bzVar;
        int b = boVar.c().b();
        if (b == 6) {
            offlineMapProvince.setState(b);
            offlineMapProvince.setCompleteCode(0);
            b(new bz(offlineMapProvince, this.c));
            try {
                cm.b(offlineMapProvince.getProvinceCode(), this.c);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (b(b) && a(offlineMapProvince)) {
            if (boVar.getPinyin().equals(offlineMapProvince.getPinyin())) {
                offlineMapProvince.setState(b);
                offlineMapProvince.setCompleteCode(boVar.getcompleteCode());
                offlineMapProvince.setVersion(boVar.getVersion());
                offlineMapProvince.setUrl(boVar.getUrl());
                bzVar = new bz(offlineMapProvince, this.c);
                bzVar.a(boVar.a());
                bzVar.d(boVar.getCode());
            } else {
                offlineMapProvince.setState(b);
                offlineMapProvince.setCompleteCode(100);
                bzVar = new bz(offlineMapProvince, this.c);
            }
            bzVar.c();
            a(bzVar);
            cm.a("saveJSONObjectToFile  province " + bzVar.d());
        }
    }

    private void a(bz bzVar) {
        if (this.b == null || bzVar == null) {
            return;
        }
        this.b.a(bzVar);
    }

    private void a(OfflineMapCity offlineMapCity, OfflineMapCity offlineMapCity2) {
        offlineMapCity.setUrl(offlineMapCity2.getUrl());
        offlineMapCity.setVersion(offlineMapCity2.getVersion());
        offlineMapCity.setSize(offlineMapCity2.getSize());
        offlineMapCity.setCode(offlineMapCity2.getCode());
        offlineMapCity.setPinyin(offlineMapCity2.getPinyin());
        offlineMapCity.setJianpin(offlineMapCity2.getJianpin());
    }

    private void a(OfflineMapProvince offlineMapProvince, OfflineMapProvince offlineMapProvince2) {
        offlineMapProvince.setUrl(offlineMapProvince2.getUrl());
        offlineMapProvince.setVersion(offlineMapProvince2.getVersion());
        offlineMapProvince.setSize(offlineMapProvince2.getSize());
        offlineMapProvince.setPinyin(offlineMapProvince2.getPinyin());
        offlineMapProvince.setJianpin(offlineMapProvince2.getJianpin());
    }

    private boolean a(int i, int i2) {
        return i2 != 1 || i <= 2 || i >= 98;
    }

    private boolean a(OfflineMapProvince offlineMapProvince) {
        if (offlineMapProvince == null) {
            return false;
        }
        Iterator<OfflineMapCity> it = offlineMapProvince.getCityList().iterator();
        while (it.hasNext()) {
            if (it.next().getState() != 4) {
                return false;
            }
        }
        return true;
    }

    private void b(bo boVar) {
        File[] listFiles = new File(fr.c(this.c)).listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            if (file.isFile() && file.exists() && file.getName().contains(boVar.getAdcode()) && file.getName().endsWith(".zip.tmp.dt")) {
                file.delete();
            }
        }
    }

    private void b(bz bzVar) {
        if (this.b != null) {
            this.b.b(bzVar);
        }
    }

    private boolean b(int i) {
        return i == 4;
    }

    public OfflineMapCity a(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                while (it2.hasNext()) {
                    OfflineMapCity next = it2.next();
                    if (next.getCode().equals(str)) {
                        return next;
                    }
                }
            }
            return null;
        }
    }

    public ArrayList<OfflineMapProvince> a() {
        ArrayList<OfflineMapProvince> arrayList = new ArrayList<>();
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
        }
        return arrayList;
    }

    public void a(bo boVar) {
        String pinyin = boVar.getPinyin();
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            loop0: while (true) {
                if (!it.hasNext()) {
                    break;
                }
                OfflineMapProvince next = it.next();
                if (next != null) {
                    for (OfflineMapCity offlineMapCity : next.getCityList()) {
                        if (offlineMapCity.getPinyin().trim().equals(pinyin.trim())) {
                            a(boVar, offlineMapCity);
                            a(boVar, next);
                            break loop0;
                        }
                    }
                }
            }
        }
    }

    public void a(List<OfflineMapProvince> list) {
        OfflineMapProvince offlineMapProvince;
        OfflineMapCity offlineMapCity;
        synchronized (this.a) {
            if (this.a.size() > 0) {
                for (int i = 0; i < this.a.size(); i++) {
                    OfflineMapProvince offlineMapProvince2 = this.a.get(i);
                    Iterator<OfflineMapProvince> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            offlineMapProvince = null;
                            break;
                        }
                        offlineMapProvince = it.next();
                        if (offlineMapProvince2.getPinyin().equals(offlineMapProvince.getPinyin())) {
                            break;
                        }
                        if (offlineMapProvince2.getPinyin().equals("quanguogaiyaotu") || offlineMapProvince2.getProvinceCode().equals("000001") || offlineMapProvince2.getProvinceCode().equals("100000")) {
                            if (offlineMapProvince.getPinyin().equals("quanguogaiyaotu")) {
                                break;
                            }
                        }
                    }
                    if (offlineMapProvince != null) {
                        a(offlineMapProvince2, offlineMapProvince);
                        ArrayList<OfflineMapCity> cityList = offlineMapProvince2.getCityList();
                        ArrayList<OfflineMapCity> cityList2 = offlineMapProvince.getCityList();
                        for (int i2 = 0; i2 < cityList.size(); i2++) {
                            OfflineMapCity offlineMapCity2 = cityList.get(i2);
                            Iterator<OfflineMapCity> it2 = cityList2.iterator();
                            while (true) {
                                if (it2.hasNext()) {
                                    offlineMapCity = it2.next();
                                    if (offlineMapCity2.getPinyin().equals(offlineMapCity.getPinyin())) {
                                        break;
                                    }
                                } else {
                                    offlineMapCity = null;
                                    break;
                                }
                            }
                            if (offlineMapCity != null) {
                                a(offlineMapCity2, offlineMapCity);
                            }
                        }
                    }
                }
            } else {
                Iterator<OfflineMapProvince> it3 = list.iterator();
                while (it3.hasNext()) {
                    this.a.add(it3.next());
                }
            }
        }
    }

    public boolean a(int i) {
        return i == 0 || i == 2 || i == 3 || i == 1 || i == 102 || i == 101 || i == 103 || i == -1;
    }

    public OfflineMapCity b(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                while (it2.hasNext()) {
                    OfflineMapCity next = it2.next();
                    if (next.getCity().trim().equalsIgnoreCase(str.trim())) {
                        return next;
                    }
                }
            }
            return null;
        }
    }

    public ArrayList<OfflineMapCity> b() {
        ArrayList<OfflineMapCity> arrayList = new ArrayList<>();
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                while (it2.hasNext()) {
                    arrayList.add(it2.next());
                }
            }
        }
        return arrayList;
    }

    public OfflineMapProvince c(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                OfflineMapProvince next = it.next();
                if (next.getProvinceName().trim().equalsIgnoreCase(str.trim())) {
                    return next;
                }
            }
            return null;
        }
    }

    public ArrayList<OfflineMapCity> c() {
        ArrayList<OfflineMapCity> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                OfflineMapProvince next = it.next();
                if (next != null) {
                    for (OfflineMapCity offlineMapCity : next.getCityList()) {
                        if (offlineMapCity.getState() == 4 || offlineMapCity.getState() == 7) {
                            arrayList.add(offlineMapCity);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapProvince> d() {
        ArrayList<OfflineMapProvince> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                OfflineMapProvince next = it.next();
                if (next != null && (next.getState() == 4 || next.getState() == 7)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapCity> e() {
        ArrayList<OfflineMapCity> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                OfflineMapProvince next = it.next();
                if (next != null) {
                    for (OfflineMapCity offlineMapCity : next.getCityList()) {
                        if (a(offlineMapCity.getState())) {
                            arrayList.add(offlineMapCity);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapProvince> f() {
        ArrayList<OfflineMapProvince> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                OfflineMapProvince next = it.next();
                if (next != null && a(next.getState())) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public void g() {
        h();
        this.b = null;
        this.c = null;
    }

    public void h() {
        if (this.a != null) {
            synchronized (this.a) {
                this.a.clear();
            }
        }
    }
}
