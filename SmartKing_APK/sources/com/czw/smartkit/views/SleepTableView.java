package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.smartkit.bleModule.sleep.DevPartSleepBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SleepTableView extends View {
    private final SimpleDateFormat HHmmSmp;
    private float bottomMargin;
    private Context context;
    private int durationCount;
    private boolean isEmptyData;
    private boolean isInitData;
    private float leftMargin;
    private Paint paint;
    private ArrayList<RectF> rectFArrayList;
    private float rightMargin;
    private ArrayList<DevPartSleepBean> sleepTableData;
    private float textSize;
    private float topMargin;

    public SleepTableView(Context context) {
        super(context);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.isEmptyData = true;
        this.HHmmSmp = new SimpleDateFormat("HH:mm");
        this.rectFArrayList = new ArrayList<>();
        this.durationCount = 4;
        this.isInitData = true;
        this.sleepTableData = new ArrayList<>();
        init(context);
    }

    public SleepTableView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.isEmptyData = true;
        this.HHmmSmp = new SimpleDateFormat("HH:mm");
        this.rectFArrayList = new ArrayList<>();
        this.durationCount = 4;
        this.isInitData = true;
        this.sleepTableData = new ArrayList<>();
        init(context);
    }

    public SleepTableView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.isEmptyData = true;
        this.HHmmSmp = new SimpleDateFormat("HH:mm");
        this.rectFArrayList = new ArrayList<>();
        this.durationCount = 4;
        this.isInitData = true;
        this.sleepTableData = new ArrayList<>();
        init(context);
    }

    private void drawTime(Canvas canvas) {
        if (this.sleepTableData.size() < 2) {
            return;
        }
        this.rectFArrayList.clear();
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setTextAlign(Paint.Align.CENTER);
        float width = ((getWidth() - this.leftMargin) - this.rightMargin) / this.durationCount;
        int size = this.sleepTableData.size();
        float f = this.leftMargin;
        int i = 0;
        while (i < size) {
            float duration = ((this.sleepTableData.get(i).getDuration() / 5) * width) + f;
            RectF rectF = new RectF(f, this.topMargin, duration, getHeight() - this.bottomMargin);
            if (this.isEmptyData) {
                this.paint.setColor(Color.parseColor("#FFFFFF"));
            } else {
                int sleepType = this.sleepTableData.get(i).getSleepType();
                if (sleepType == 1) {
                    this.paint.setColor(Color.parseColor("#6800C3"));
                } else if (sleepType == 2) {
                    this.paint.setColor(Color.parseColor("#BE55FF"));
                } else {
                    this.paint.setColor(Color.parseColor("#FFFFFF"));
                }
            }
            canvas.drawRect(rectF, this.paint);
            this.rectFArrayList.add(rectF);
            i++;
            f = duration;
        }
        this.paint.setColor(-1);
        if (this.isInitData || this.sleepTableData.size() <= 2) {
            return;
        }
        canvas.drawText(this.HHmmSmp.format(Long.valueOf(this.sleepTableData.get(0).getDate() * 1000)), this.leftMargin, getHeight() - this.textSize, this.paint);
        canvas.drawText(this.HHmmSmp.format(Long.valueOf((this.sleepTableData.get(this.sleepTableData.size() - 1).getDate() * 1000) + (r0.getDuration() * 60 * 1000))), getWidth() - this.rightMargin, getHeight() - this.textSize, this.paint);
    }

    private void init(Context context) {
        this.context = context;
        this.textSize = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_64PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_64PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_10PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_48PX);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX) / 2.0f);
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setTextSize(this.textSize);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTime(canvas);
    }

    public void updateData(List<DevPartSleepBean> list) {
        this.durationCount = 0;
        this.isInitData = false;
        this.sleepTableData.clear();
        if (list == null || list.size() <= 2) {
            this.isEmptyData = true;
            invalidate();
            return;
        }
        this.isEmptyData = false;
        Iterator<DevPartSleepBean> it = list.iterator();
        while (it.hasNext()) {
            this.durationCount += it.next().getDuration() / 5;
        }
        this.sleepTableData.addAll(list);
        invalidate();
    }
}
