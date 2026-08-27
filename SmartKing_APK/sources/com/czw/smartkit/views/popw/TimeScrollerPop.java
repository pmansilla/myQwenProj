package com.czw.smartkit.views.popw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TimeScrollerPop implements View.OnClickListener {
    static ArrayList<String> loadHour = new ArrayList<>();
    static ArrayList<String> loadMinute;
    private static TimeScrollerPop pop;
    private TextView centerItem;
    private ClickCallback clickCallback;
    private View contentView;
    private Context context;
    private WheelView hourView;
    private TextView leftItem;
    private WheelView minuteView;
    public PopupWindow popupWindow;
    private TextView rightItem;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public void onDismiss() {
        }

        public abstract void onSelect(int i, int i2);
    }

    static {
        for (int i = 0; i < 24; i++) {
            loadHour.add(String.format("%02d", Integer.valueOf(i)));
        }
        loadMinute = new ArrayList<>();
        for (int i2 = 0; i2 < 60; i2++) {
            loadMinute.add(String.format("%02d", Integer.valueOf(i2)));
        }
    }

    public TimeScrollerPop(Context context) {
        this.context = context;
        initViewEvent(context);
    }

    public static TimeScrollerPop getPop(Context context) {
        synchronized (Void.class) {
            if (pop == null) {
                synchronized (Void.class) {
                    pop = new TimeScrollerPop(context);
                }
            }
        }
        return pop;
    }

    private void initViewEvent(Context context) {
        this.contentView = LayoutInflater.from(context).inflate(R.layout.pop_time_scroller, (ViewGroup) null);
        this.popupWindow = new PopupWindow(this.contentView, -1, -2, true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(128);
        this.popupWindow.setBackgroundDrawable(colorDrawable);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.smartkit.views.popw.TimeScrollerPop.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (TimeScrollerPop.this.clickCallback != null) {
                    TimeScrollerPop.this.clickCallback.onDismiss();
                }
                TimeScrollerPop.this.backgroundAlpha(1.0f);
            }
        });
        this.leftItem = (TextView) this.contentView.findViewById(R.id.leftItem);
        this.leftItem.setOnClickListener(this);
        this.rightItem = (TextView) this.contentView.findViewById(R.id.rightItem);
        this.rightItem.setOnClickListener(this);
        this.centerItem = (TextView) this.contentView.findViewById(R.id.centerItem);
        this.centerItem.setOnClickListener(this);
        this.hourView = (WheelView) this.contentView.findViewById(R.id.hourView);
        this.hourView.setWheelAdapter(new ArrayWheelAdapter(context));
        this.hourView.setSkin(WheelView.Skin.Holo);
        this.hourView.setWheelSize(5);
        this.hourView.setLoop(false);
        this.hourView.setWheelData(loadHour);
        this.minuteView = (WheelView) this.contentView.findViewById(R.id.minuteView);
        this.minuteView.setWheelAdapter(new ArrayWheelAdapter(context));
        this.minuteView.setSkin(WheelView.Skin.Holo);
        this.minuteView.setWheelSize(5);
        this.minuteView.setLoop(false);
        this.minuteView.setWheelData(loadMinute);
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = ((FragmentActivity) this.context).getWindow().getAttributes();
        attributes.alpha = f;
        ((FragmentActivity) this.context).getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        this.popupWindow.dismiss();
        backgroundAlpha(1.0f);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftItem) {
            dismiss();
            if (this.clickCallback != null) {
                this.clickCallback.onDismiss();
                return;
            }
            return;
        }
        if (id != R.id.rightItem) {
            return;
        }
        dismiss();
        if (this.clickCallback != null) {
            this.clickCallback.onSelect(this.hourView.getCurrentPosition(), this.minuteView.getCurrentPosition());
        }
    }

    public TimeScrollerPop showLeftText(int i) {
        this.leftItem.setText(i);
        return this;
    }

    public TimeScrollerPop showPicker(View view, ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        backgroundAlpha(0.5f);
        return this;
    }

    public TimeScrollerPop showRightText(int i) {
        this.rightItem.setText(i);
        return this;
    }

    public TimeScrollerPop showTexts(int i, int i2, int i3) {
        this.leftItem.setText(i);
        this.centerItem.setText(i2);
        this.rightItem.setText(i3);
        return this;
    }

    public TimeScrollerPop showTitle(int i) {
        this.centerItem.setText(i);
        return this;
    }
}
