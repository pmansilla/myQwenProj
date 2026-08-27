package com.czw.smartkit.util;

import android.os.Handler;
import android.widget.TextView;

/* loaded from: classes.dex */
public class CounterUtils {
    private int count;
    private Handler handler;
    private String oldText;
    private TextView textView;
    private int tmp;

    public CounterUtils(int i, TextView textView, String str) {
        this.handler = new Handler();
        this.count = 60;
        this.textView = null;
        this.oldText = "";
        this.count = i;
        this.textView = textView;
        this.oldText = str;
        this.handler = new Handler();
    }

    static /* synthetic */ int access$010(CounterUtils counterUtils) {
        int i = counterUtils.tmp;
        counterUtils.tmp = i - 1;
        return i;
    }

    public void startCounter() {
        this.tmp = this.count;
        this.handler.post(new Runnable() { // from class: com.czw.smartkit.util.CounterUtils.1
            @Override // java.lang.Runnable
            public void run() {
                CounterUtils.access$010(CounterUtils.this);
                if (CounterUtils.this.tmp < 0) {
                    CounterUtils.this.handler.removeCallbacks(this);
                    CounterUtils.this.textView.setText(CounterUtils.this.oldText);
                    CounterUtils.this.textView.setClickable(true);
                    return;
                }
                CounterUtils.this.handler.postDelayed(this, 1000L);
                CounterUtils.this.textView.setText(CounterUtils.this.tmp + "s");
                CounterUtils.this.textView.setClickable(false);
            }
        });
    }
}
