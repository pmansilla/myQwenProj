package com.amap.api.mapcore.util;

import android.content.Context;
import android.opengl.GLES20;
import com.amap.api.maps.model.GL3DModel;
import com.amap.api.maps.model.GL3DModelOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: Gl3dModelManager.java */
/* loaded from: classes.dex */
public class y {
    private Context b;
    private ad c;
    private long a = 0;
    private List<dg> d = new ArrayList();
    private List<Integer> e = new ArrayList();

    public y(Context context, ad adVar) {
        this.b = context;
        this.c = adVar;
    }

    public GL3DModel a(GL3DModelOptions gL3DModelOptions) {
        GL3DModel gL3DModel;
        if (gL3DModelOptions == null) {
            return null;
        }
        dg dgVar = new dg(this, gL3DModelOptions, this.c);
        StringBuilder sb = new StringBuilder();
        sb.append("model_");
        long j = this.a;
        this.a = 1 + j;
        sb.append(j);
        dgVar.a(sb.toString());
        synchronized (this.d) {
            this.d.add(dgVar);
            gL3DModel = new GL3DModel(dgVar);
        }
        return gL3DModel;
    }

    public void a() {
        for (dg dgVar : this.d) {
            if (dgVar.isVisible()) {
                dgVar.j();
            }
        }
    }

    public void a(int i) {
        this.e.add(Integer.valueOf(i));
    }

    public void a(String str) {
        try {
            if (this.d == null || this.d.size() <= 0) {
                return;
            }
            dg dgVar = null;
            for (int i = 0; i < this.d.size(); i++) {
                dgVar = this.d.get(i);
                if (str.equals(dgVar.getId())) {
                    break;
                }
            }
            if (dgVar != null) {
                this.d.remove(dgVar);
                dgVar.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void b() {
        if (this.d != null) {
            this.d.clear();
        }
    }

    public void c() {
        if (this.d != null) {
            Iterator<dg> it = this.d.iterator();
            while (it.hasNext()) {
                it.next().destroy();
            }
            this.d.clear();
        }
    }

    public void d() {
        if (this.e != null) {
            Iterator<Integer> it = this.e.iterator();
            while (it.hasNext()) {
                GLES20.glDeleteTextures(1, new int[]{it.next().intValue()}, 0);
            }
        }
    }
}
