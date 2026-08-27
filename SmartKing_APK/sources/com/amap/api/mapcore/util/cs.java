package com.amap.api.mapcore.util;

/* compiled from: AbstractCityStateImp.java */
/* loaded from: classes.dex */
public abstract class cs implements cw {
    protected int a;
    protected bo b;

    public cs(int i, bo boVar) {
        this.a = i;
        this.b = boVar;
    }

    public void a() {
        cm.a("Wrong call delete()  State: " + b() + "  " + getClass());
    }

    public void a(int i) {
        cm.a("Wrong call fail()  State: " + b() + "  " + getClass());
    }

    public boolean a(cs csVar) {
        return csVar.b() == b();
    }

    public int b() {
        return this.a;
    }

    public void b(cs csVar) {
        cm.a(b() + " ==> " + csVar.b() + "   " + getClass() + "==>" + csVar.getClass());
    }

    public void c() {
        cm.a("Wrong call start()  State: " + b() + "  " + getClass());
    }

    public void d() {
        cm.a("Wrong call continueDownload()  State: " + b() + "  " + getClass());
    }

    public void e() {
        cm.a("Wrong call pause()  State: " + b() + "  " + getClass());
    }

    public void f() {
        cm.a("Wrong call hasNew()  State: " + b() + "  " + getClass());
    }

    public void g() {
        cm.a("Wrong call complete()  State: " + b() + "  " + getClass());
    }
}
