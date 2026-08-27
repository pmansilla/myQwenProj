package com.czw.modes.pop;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ScrollPicker extends RootPop {
    private static ScrollPicker pop;
    private TextView centerItem;
    private ClickCallback clickCallback;
    private ArrayList<String> datas;
    private TextView leftItem;
    private TextView rightItem;
    private WheelView wheelview;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public void onDismiss() {
        }

        public abstract void onSelect(int i);
    }

    public ScrollPicker(Context context) {
        super(context);
        this.datas = new ArrayList<>();
    }

    public static ScrollPicker create(Context context) {
        synchronized (Void.class) {
            if (pop == null) {
                synchronized (Void.class) {
                    pop = new ScrollPicker(context);
                }
            }
        }
        return pop;
    }

    @Override // com.czw.modes.pop.RootPop
    protected void initSelf() {
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.modes.pop.ScrollPicker.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (ScrollPicker.this.clickCallback != null) {
                    ScrollPicker.this.clickCallback.onDismiss();
                }
                ScrollPicker.this.backgroundAlpha(1.0f);
            }
        });
        this.leftItem = (TextView) $View(R.id.leftItem);
        this.centerItem = (TextView) $View(R.id.centerItem);
        this.rightItem = (TextView) $View(R.id.rightItem);
        this.wheelview = (WheelView) $View(R.id.wheelview);
        this.wheelview.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.wheelview.setSkin(WheelView.Skin.Holo);
        this.wheelview.setWheelSize(5);
        this.wheelview.setLoop(false);
    }

    @Override // com.czw.modes.pop.RootPop
    protected int loadLayout() {
        return R.layout.pop_single_scroller;
    }

    public ScrollPicker setData(String str, ArrayList<String> arrayList) {
        this.datas.clear();
        this.datas.addAll(arrayList);
        this.wheelview.setWheelData(arrayList);
        int size = arrayList.size();
        int i = size / 4;
        for (int i2 = 0; i2 < size; i2++) {
            if (arrayList.get(i2).equals(str)) {
                i = i2;
            }
        }
        this.wheelview.setSelection(i);
        return this;
    }

    public ScrollPicker setTitle(int i) {
        this.centerItem.setText(i);
        return this;
    }

    public ScrollPicker setTitle(int i, int i2, int i3) {
        this.leftItem.setText(i);
        this.centerItem.setText(i2);
        this.rightItem.setText(i3);
        return this;
    }

    public ScrollPicker showPicker(View view, final ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        backgroundAlpha(0.5f);
        this.leftItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.modes.pop.ScrollPicker.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ScrollPicker.this.dismiss();
                if (clickCallback != null) {
                    clickCallback.onDismiss();
                }
            }
        });
        this.rightItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.modes.pop.ScrollPicker.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ScrollPicker.this.dismiss();
                if (clickCallback != null) {
                    clickCallback.onSelect(ScrollPicker.this.wheelview.getCurrentPosition());
                }
            }
        });
        return this;
    }
}
