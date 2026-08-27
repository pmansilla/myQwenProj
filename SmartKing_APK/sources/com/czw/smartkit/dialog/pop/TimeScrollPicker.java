package com.czw.smartkit.dialog.pop;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TimeScrollPicker extends RootPop {
    static ArrayList<String> loadHour = new ArrayList<>();
    static ArrayList<String> loadMinute;
    private static TimeScrollPicker pop;
    private TextView centerItem;
    private WheelView hourView;
    private TextView leftItem;
    private WheelView minuteView;
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

    public TimeScrollPicker(Context context) {
        super(context);
    }

    public static TimeScrollPicker create(Context context) {
        synchronized (Void.class) {
            if (pop == null) {
                synchronized (Void.class) {
                    pop = new TimeScrollPicker(context);
                }
            }
        }
        return pop;
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected void initSelf() {
        this.leftItem = (TextView) $View(R.id.leftItem);
        this.rightItem = (TextView) $View(R.id.rightItem);
        this.hourView = (WheelView) $View(R.id.hourView);
        this.centerItem = (TextView) $View(R.id.centerItem);
        this.minuteView = (WheelView) $View(R.id.minuteView);
        this.hourView.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.hourView.setSkin(WheelView.Skin.Holo);
        this.hourView.setWheelSize(5);
        this.hourView.setLoop(false);
        this.hourView.setWheelData(loadHour);
        this.minuteView.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.minuteView.setSkin(WheelView.Skin.Holo);
        this.minuteView.setWheelSize(5);
        this.minuteView.setLoop(false);
        this.minuteView.setWheelData(loadMinute);
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected int loadLayout() {
        return R.layout.pop_time_scroller;
    }

    public TimeScrollPicker showLeftText(int i) {
        this.leftItem.setText(i);
        return this;
    }

    public TimeScrollPicker showPicker(View view, final ClickCallback clickCallback) {
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        backgroundAlpha(0.5f);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.smartkit.dialog.pop.TimeScrollPicker.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (clickCallback != null) {
                    clickCallback.onDismiss();
                }
                TimeScrollPicker.this.backgroundAlpha(1.0f);
            }
        });
        this.leftItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.TimeScrollPicker.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TimeScrollPicker.this.popupWindow.dismiss();
                TimeScrollPicker.this.backgroundAlpha(1.0f);
                if (clickCallback != null) {
                    clickCallback.onDismiss();
                }
            }
        });
        this.rightItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.TimeScrollPicker.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TimeScrollPicker.this.dismiss();
                if (clickCallback != null) {
                    clickCallback.onSelect(TimeScrollPicker.this.hourView.getCurrentPosition(), TimeScrollPicker.this.minuteView.getCurrentPosition());
                }
            }
        });
        backgroundAlpha(0.5f);
        return this;
    }

    public TimeScrollPicker showRightText(int i) {
        this.rightItem.setText(i);
        return this;
    }

    public TimeScrollPicker showTexts(int i, int i2, int i3) {
        this.leftItem.setText(i);
        this.centerItem.setText(i2);
        this.rightItem.setText(i3);
        return this;
    }

    public TimeScrollPicker showTitle(int i) {
        this.centerItem.setText(i);
        return this;
    }
}
