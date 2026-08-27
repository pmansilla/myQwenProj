package com.mob.elp.b;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mob.elp.PushMessage;
import com.mob.elp.a.f;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;

/* compiled from: MessageDialog.java */
/* loaded from: classes.dex */
public class b extends Dialog implements View.OnClickListener {
    private PushMessage a;
    private TextView b;
    private TextView c;
    private ImageView d;
    private ImageView e;
    private ImageView f;
    private ImageView g;
    private ArrayList<Bitmap> h;

    private b(Context context, PushMessage pushMessage, ArrayList<Bitmap> arrayList) {
        super(context, ResHelper.getStyleRes(context, "ELPDialogStyle"));
        this.a = pushMessage;
        this.h = arrayList;
        a(context);
    }

    public static b a(Context context, PushMessage pushMessage, ArrayList<Bitmap> arrayList) {
        return new b(context, pushMessage, arrayList);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x005e A[Catch: all -> 0x0136, TryCatch #0 {all -> 0x0136, blocks: (B:2:0x0000, B:4:0x0004, B:6:0x0015, B:11:0x005e, B:13:0x009b, B:14:0x00ec, B:16:0x0105, B:17:0x010e, B:19:0x0112, B:20:0x011b, B:23:0x0021, B:25:0x0029, B:26:0x0034, B:28:0x003c, B:29:0x0047, B:31:0x0050), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x005d A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(android.content.Context r9) {
        /*
            Method dump skipped, instructions count: 319
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.elp.b.b.a(android.content.Context):void");
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        com.mob.elp.a.a.b().a();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == ResHelper.getIdRes(getContext(), "ivClose")) {
            dismiss();
        } else if (view.getId() == ResHelper.getIdRes(getContext(), "llView")) {
            dismiss();
            f.a().a(getContext(), this.a);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        f.a().a(getContext(), "show", this.a.workId);
    }
}
