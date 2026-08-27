package cn.smssdk.c;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.OnDialogListener;
import cn.smssdk.entity.UiSettings;
import cn.smssdk.ui.companent.CircleImageView;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.e;
import com.mob.tools.utils.ResHelper;
import java.lang.reflect.Method;

/* compiled from: AuthorizeDialog.java */
/* loaded from: classes.dex */
public class b extends AlertDialog {
    private View a;
    private Context b;
    private int c;
    private TextView d;
    private TextView e;
    private CircleImageView f;
    private TextView g;
    private TextView h;
    private OnDialogListener i;
    private UiSettings j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthorizeDialog.java */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (b.this.isShowing()) {
                b.this.dismiss();
            }
            if (b.this.i != null) {
                b.this.i.onAgree();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthorizeDialog.java */
    /* renamed from: cn.smssdk.c.b$b, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class ViewOnClickListenerC0011b implements View.OnClickListener {
        ViewOnClickListenerC0011b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (b.this.isShowing()) {
                b.this.dismiss();
            }
            if (b.this.i != null) {
                b.this.i.onDisagree();
            }
        }
    }

    public b(Context context, OnDialogListener onDialogListener) {
        this(context, new UiSettings.Builder().buildDefault(), onDialogListener);
    }

    public b(Context context, UiSettings uiSettings, OnDialogListener onDialogListener) {
        super(context, ResHelper.getStyleRes(context, "smssdk_DialogStyle"));
        this.b = context;
        this.j = uiSettings;
        this.i = onDialogListener;
        if (this.b.getResources().getConfiguration().orientation == 2) {
            double a2 = a(this.b);
            Double.isNaN(a2);
            this.c = (int) (a2 * 0.7d);
        } else {
            double c = c(this.b);
            Double.isNaN(c);
            this.c = (int) (c * 0.7d);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.a = LayoutInflater.from(this.b).inflate(ResHelper.getLayoutRes(context, "smssdk_authorize_dialog"), (ViewGroup) null);
    }

    private int a(Context context) {
        return b(context)[1];
    }

    private void a() {
        int d = e.d(-1);
        if (d == -1) {
            this.f.setVisibility(8);
        } else {
            this.f.setImageResource(d);
        }
        this.h.setOnClickListener(new a());
        this.g.setOnClickListener(new ViewOnClickListenerC0011b());
    }

    private void b() {
        this.d = (TextView) this.a.findViewById(ResHelper.getIdRes(this.b, "smssdk_authorize_dialog_title_tv"));
        this.e = (TextView) this.a.findViewById(ResHelper.getIdRes(this.b, "smssdk_authorize_dialog_msg"));
        this.f = (CircleImageView) this.a.findViewById(ResHelper.getIdRes(this.b, "smssdk_authorize_dialog_logo_iv"));
        this.h = (TextView) this.a.findViewById(ResHelper.getIdRes(this.b, "smssdk_authorize_dialog_accept_tv"));
        this.g = (TextView) this.a.findViewById(ResHelper.getIdRes(this.b, "smssdk_authorize_dialog_reject_tv"));
        UiSettings uiSettings = this.j;
        if (uiSettings != null) {
            this.d.setText(e.b(uiSettings.getTitleTextId(), UiSettings.DEFAULT_TITLE_TEXT_ID));
            this.d.setTextColor(e.a(this.j.getTitleTextColorId(), UiSettings.DEFAULT_TITLE_TEXT_COLOR_ID));
            int titleTextSizeDp = this.j.getTitleTextSizeDp();
            if (titleTextSizeDp <= 0) {
                titleTextSizeDp = UiSettings.DEFAULT_TITLE_TEXT_SIZE_DP;
            }
            this.d.setTextSize(titleTextSizeDp);
            this.e.setText(this.j.getMsgText());
        }
    }

    private int[] b(Context context) {
        WindowManager windowManager;
        try {
            windowManager = (WindowManager) context.getSystemService("window");
        } catch (Throwable th) {
            SMSLog.getInstance().w(th, SMSLog.FORMAT, "AuthorizeDialog", "getScreenSize", "get SCreenSize Exception");
            windowManager = null;
        }
        if (windowManager == null) {
            return new int[]{0, 0};
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < 13) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
        }
        try {
            Point point = new Point();
            Method method = defaultDisplay.getClass().getMethod("getRealSize", Point.class);
            method.setAccessible(true);
            method.invoke(defaultDisplay, point);
            return new int[]{point.x, point.y};
        } catch (Throwable th2) {
            SMSLog.getInstance().w(th2, SMSLog.FORMAT, "AuthorizeDialog", "getScreenSize", "get SCreenSize Exception");
            return new int[]{0, 0};
        }
    }

    private int c(Context context) {
        return b(context)[0];
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(this.a, new LinearLayout.LayoutParams(this.c, -2, 0.0f));
        b();
        a();
    }
}
