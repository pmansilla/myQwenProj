package cn.smssdk.c;

import android.R;
import android.widget.LinearLayout;
import cn.smssdk.OnDialogListener;
import cn.smssdk.entity.UiSettings;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: AlertPage.java */
/* loaded from: classes.dex */
public class a extends FakeActivity {
    private static a e;
    private ArrayList<Runnable> a;
    private ArrayList<Runnable> b;
    private HashMap<String, Object> c;
    private String d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AlertPage.java */
    /* renamed from: cn.smssdk.c.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0010a implements OnDialogListener {
        C0010a() {
        }

        @Override // cn.smssdk.OnDialogListener
        public void onAgree() {
            a.this.c.put("res", true);
            a.e.finish();
        }

        @Override // cn.smssdk.OnDialogListener
        public void onDisagree() {
            a.this.c.put("res", false);
            a.e.finish();
        }
    }

    public a() {
        e = this;
        this.a = new ArrayList<>();
        this.b = new ArrayList<>();
        this.c = new HashMap<>();
        this.c.put("okActions", this.a);
        this.c.put("cancelActions", this.b);
        setResult(this.c);
    }

    public static void a(Runnable runnable, Runnable runnable2) {
        e.a.add(runnable);
        e.b.add(runnable2);
    }

    public static void a(String str) {
        e.d = str;
    }

    private LinearLayout b() {
        LinearLayout linearLayout = new LinearLayout(this.activity);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-1);
        return linearLayout;
    }

    public static boolean c() {
        return e != null;
    }

    private void d() {
        new b(getContext(), new UiSettings.Builder().setMsgText(this.d).build(), new C0010a()).show();
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        int styleRes = ResHelper.getStyleRes(this.activity, "smssdk_DialogStyle");
        if (styleRes > 0) {
            this.activity.setTheme(styleRes);
        } else {
            this.activity.setTheme(R.style.Theme.Dialog);
        }
        this.activity.setContentView(b());
        d();
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        e = null;
        super.onDestroy();
    }
}
