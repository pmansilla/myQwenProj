package com.czw.modes.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import me.panpf.sketch.display.ImageDisplayer;

/* loaded from: classes.dex */
public class PickerBirthdayPop {
    private static final int endYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())).intValue();
    private static PickerBirthdayPop pop = null;
    private static final int totalCut = 100;
    private Context context;
    private WheelView dayWheelView;
    private TextView leftItem;
    private WheelView monthWheelView;
    public PopupWindow popupWindow;
    private TextView rightItem;
    private WheelView yearWheelView;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public abstract void getBirthDate(int i, int i2, int i3);

        public void onDismiss() {
        }
    }

    public PickerBirthdayPop(Context context) {
        this.context = context;
    }

    public static ArrayList<String> dayDate(int i) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i2 = 1; i2 <= i; i2++) {
            arrayList.add(String.format("%02d", Integer.valueOf(i2)));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLastDay(int i, int i2) {
        return i2 != 2 ? (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31 : (i % 100 == 0 && i % ImageDisplayer.DEFAULT_ANIMATION_DURATION == 0) ? 29 : 28;
    }

    public static PickerBirthdayPop getPop(Context context) {
        synchronized (Void.class) {
            if (pop == null) {
                synchronized (Void.class) {
                    pop = new PickerBirthdayPop(context);
                }
            }
        }
        return pop;
    }

    private void initDay(ArrayList<String> arrayList) {
        this.dayWheelView.setWheelData(arrayList);
    }

    private void initWheel(View view) {
        this.yearWheelView = (WheelView) view.findViewById(R.id.year);
        this.yearWheelView.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.yearWheelView.setSkin(WheelView.Skin.Holo);
        this.yearWheelView.setWheelData(yearDate());
        this.yearWheelView.setWheelSize(5);
        this.yearWheelView.setLoop(false);
        this.yearWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() { // from class: com.czw.modes.pop.PickerBirthdayPop.1
            @Override // com.wx.wheelview.widget.WheelView.OnWheelItemSelectedListener
            public void onItemSelected(int i, Object obj) {
                PickerBirthdayPop.dayDate(PickerBirthdayPop.this.getLastDay(((PickerBirthdayPop.endYear - 100) - i) + 1, PickerBirthdayPop.this.monthWheelView.getSelection() + 1));
            }
        });
        this.monthWheelView = (WheelView) view.findViewById(R.id.month);
        this.monthWheelView.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.monthWheelView.setWheelData(monthDate());
        this.monthWheelView.setSkin(WheelView.Skin.Holo);
        this.monthWheelView.setWheelSize(5);
        this.monthWheelView.setLoop(true);
        this.monthWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() { // from class: com.czw.modes.pop.PickerBirthdayPop.2
            @Override // com.wx.wheelview.widget.WheelView.OnWheelItemSelectedListener
            public void onItemSelected(int i, Object obj) {
                PickerBirthdayPop.dayDate(PickerBirthdayPop.this.getLastDay(((PickerBirthdayPop.endYear - 100) - i) + 1, PickerBirthdayPop.this.monthWheelView.getSelection() + 1));
            }
        });
        this.dayWheelView = (WheelView) view.findViewById(R.id.day);
        this.dayWheelView.setWheelAdapter(new ArrayWheelAdapter(this.context));
        this.dayWheelView.setSkin(WheelView.Skin.Holo);
        this.dayWheelView.setWheelSize(5);
        this.dayWheelView.setLoop(true);
        initDay(dayDate(30));
    }

    public static ArrayList<String> monthDate() {
        ArrayList<String> arrayList = new ArrayList<>();
        int i = 0;
        while (i < 12) {
            i++;
            arrayList.add(String.format("%02d", Integer.valueOf(i)));
        }
        return arrayList;
    }

    public static ArrayList<String> yearDate() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = endYear - 100; i < endYear; i++) {
            arrayList.add(String.format("%02d", Integer.valueOf(i)));
        }
        return arrayList;
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

    public PickerBirthdayPop showPicker(View view, String str, final ClickCallback clickCallback) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.pop_picker_birthday, (ViewGroup) null);
        this.popupWindow = new PopupWindow(inflate, -1, -2, true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(128);
        this.popupWindow.setBackgroundDrawable(colorDrawable);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        initWheel(inflate);
        String[] split = str.split("-");
        this.yearWheelView.setSelection(yearDate().indexOf(split[0]));
        this.monthWheelView.setSelection(monthDate().indexOf(split[1]));
        ArrayList<String> dayDate = dayDate(getLastDay(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue()));
        initDay(dayDate);
        this.dayWheelView.setSelection(dayDate.indexOf(split[2]));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.modes.pop.PickerBirthdayPop.3
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (clickCallback != null) {
                    clickCallback.onDismiss();
                }
                PickerBirthdayPop.this.backgroundAlpha(1.0f);
            }
        });
        this.leftItem = (TextView) inflate.findViewById(R.id.leftItem);
        this.leftItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.modes.pop.PickerBirthdayPop.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PickerBirthdayPop.this.popupWindow.dismiss();
                PickerBirthdayPop.this.backgroundAlpha(1.0f);
                if (clickCallback != null) {
                    clickCallback.onDismiss();
                }
            }
        });
        this.rightItem = (TextView) inflate.findViewById(R.id.rightItem);
        this.rightItem.setOnClickListener(new View.OnClickListener() { // from class: com.czw.modes.pop.PickerBirthdayPop.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PickerBirthdayPop.this.dismiss();
                if (clickCallback != null) {
                    clickCallback.getBirthDate((PickerBirthdayPop.endYear - 100) + PickerBirthdayPop.this.yearWheelView.getCurrentPosition(), PickerBirthdayPop.this.monthWheelView.getCurrentPosition() + 1, PickerBirthdayPop.this.dayWheelView.getCurrentPosition() + 1);
                }
            }
        });
        backgroundAlpha(0.5f);
        return this;
    }

    public void showTxt(int i, int i2) {
        this.leftItem.setText(i);
        this.rightItem.setText(i2);
    }
}
