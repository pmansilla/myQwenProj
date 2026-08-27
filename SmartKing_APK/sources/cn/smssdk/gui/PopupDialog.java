package cn.smssdk.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.smssdk.utils.SMSLog;
import com.mob.tools.utils.ResHelper;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class PopupDialog extends AlertDialog {
    private static final String TAG = "PopupDialog";
    private LinearLayout bottomLl;
    private TextView cancel;
    private ImageView close;
    private TextView confirm;
    private Context context;
    private TextView msg;
    private TextView title;
    private RelativeLayout topRl;
    private View verticalLine;
    private View view;
    private int width;

    protected PopupDialog(Context context, boolean z, boolean z2) {
        super(context, ResHelper.getStyleRes(context, "Dialog_Common"));
        this.context = context;
        double screenWidth = getScreenWidth(this.context);
        Double.isNaN(screenWidth);
        this.width = (int) (screenWidth * 0.7d);
        setCancelable(z);
        setCanceledOnTouchOutside(z2);
        this.view = LayoutInflater.from(this.context).inflate(ResHelper.getLayoutRes(context, "smssdk_popup_dialog"), (ViewGroup) null);
        initView();
    }

    public static PopupDialog create(Context context, int i, int i2, int i3, View.OnClickListener onClickListener, int i4, View.OnClickListener onClickListener2, boolean z, boolean z2, boolean z3) {
        return create(context, i, i2, i3, onClickListener, i4, onClickListener2, z, z2, z3, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x009a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static cn.smssdk.gui.PopupDialog create(android.content.Context r17, int r18, int r19, int r20, android.view.View.OnClickListener r21, int r22, android.view.View.OnClickListener r23, boolean r24, boolean r25, boolean r26, android.content.DialogInterface.OnDismissListener r27) {
        /*
            Method dump skipped, instructions count: 221
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.gui.PopupDialog.create(android.content.Context, int, int, int, android.view.View$OnClickListener, int, android.view.View$OnClickListener, boolean, boolean, boolean, android.content.DialogInterface$OnDismissListener):cn.smssdk.gui.PopupDialog");
    }

    public static PopupDialog create(Context context, String str, String str2, String str3, View.OnClickListener onClickListener, String str4, View.OnClickListener onClickListener2, boolean z, boolean z2, boolean z3) {
        PopupDialog popupDialog = new PopupDialog(context, z, z2);
        popupDialog.setDialogTitle(str, z3);
        popupDialog.setDialogMessage(str2);
        popupDialog.setDialogButton(str3, onClickListener, str4, onClickListener2);
        return popupDialog;
    }

    private int getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    private int[] getScreenSize(Context context) {
        WindowManager windowManager;
        try {
            windowManager = (WindowManager) context.getSystemService("window");
        } catch (Throwable th) {
            SMSLog.getInstance().w(SMSLog.FORMAT, TAG, "getScreenSize", th);
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
            SMSLog.getInstance().w(SMSLog.FORMAT, TAG, "getScreenSize", th2);
            return new int[]{0, 0};
        }
    }

    private int getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    private void initView() {
        this.title = (TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_title_tv"));
        this.close = (ImageView) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_close_iv"));
        this.msg = (TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_message_tv"));
        this.msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.confirm = (TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_confirm_tv"));
        this.cancel = (TextView) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_cancel_tv"));
        this.bottomLl = (LinearLayout) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_bottom_ll"));
        this.topRl = (RelativeLayout) this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_top_rl"));
        this.verticalLine = this.view.findViewById(ResHelper.getIdRes(this.context, "common_dialog_vertical_line"));
        if (this.close != null) {
            this.close.setOnClickListener(new View.OnClickListener() { // from class: cn.smssdk.gui.PopupDialog.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    PopupDialog.this.dismiss();
                }
            });
        }
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(this.view, new LinearLayout.LayoutParams(this.width, -2, 0.0f));
    }

    public void setCancel(int i) {
        this.cancel.setText(this.context.getResources().getString(i));
    }

    protected void setDialogButton(int i, CharSequence charSequence, final View.OnClickListener onClickListener) {
        if (charSequence == null || "".equals(charSequence)) {
            switch (i) {
                case -2:
                    if (this.cancel != null) {
                        this.cancel.setVisibility(8);
                        return;
                    }
                    return;
                case -1:
                    if (this.confirm != null) {
                        this.confirm.setVisibility(8);
                        return;
                    }
                    return;
                default:
                    SMSLog.getInstance().e(SMSLog.FORMAT, TAG, "setDialogButton", "Button can not be found. whichButton=" + i);
                    return;
            }
        }
        switch (i) {
            case -2:
                if (this.cancel != null) {
                    this.cancel.setText(charSequence);
                    this.cancel.setOnClickListener(new View.OnClickListener() { // from class: cn.smssdk.gui.PopupDialog.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            if (onClickListener != null) {
                                onClickListener.onClick(view);
                            }
                            PopupDialog.this.dismiss();
                        }
                    });
                    return;
                }
                return;
            case -1:
                if (this.confirm != null) {
                    this.confirm.setText(charSequence);
                    this.confirm.setOnClickListener(new View.OnClickListener() { // from class: cn.smssdk.gui.PopupDialog.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            if (onClickListener != null) {
                                onClickListener.onClick(view);
                            }
                            PopupDialog.this.dismiss();
                        }
                    });
                    return;
                }
                return;
            default:
                SMSLog.getInstance().e(SMSLog.FORMAT, TAG, "setDialogButton", "Button can not be found. whichButton=" + i);
                return;
        }
    }

    public void setDialogButton(String str, View.OnClickListener onClickListener, String str2, View.OnClickListener onClickListener2) {
        if ((str == null || "".equals(str)) && (str2 == null || "".equals(str2))) {
            if (this.bottomLl != null) {
                this.bottomLl.setVisibility(8);
            }
        } else {
            if (str != null && !"".equals(str) && str2 != null && !"".equals(str2)) {
                setDialogButton(-1, str, onClickListener);
                setDialogButton(-2, str2, onClickListener2);
                return;
            }
            this.verticalLine.setVisibility(8);
            setDialogButton(-1, null, null);
            if (str == null || "".equals(str)) {
                setDialogButton(-2, str2, onClickListener2);
            } else {
                setDialogButton(-2, str, onClickListener);
            }
        }
    }

    public void setDialogMessage(CharSequence charSequence) {
        if (charSequence == null || "".equals(charSequence)) {
            if (this.msg != null) {
                this.msg.setVisibility(8);
            }
        } else if (this.msg != null) {
            this.msg.setText(charSequence);
        }
    }

    public void setDialogTitle(CharSequence charSequence, boolean z) {
        if (charSequence == null || "".equals(charSequence)) {
            if (this.title != null) {
                this.title.setVisibility(8);
            }
        } else if (this.title != null) {
            this.title.setText(charSequence);
        }
        if (!z || this.close == null) {
            return;
        }
        this.close.setVisibility(0);
    }
}
