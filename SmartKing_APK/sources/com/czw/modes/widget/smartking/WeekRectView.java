package com.czw.modes.widget.smartking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.R;
import com.czw.utils.LogUtil;
import com.czw.utils.MathUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class WeekRectView extends View {
    private static final SimpleDateFormat smp = new SimpleDateFormat("MM.dd");
    private float Px1;
    private float bottomMargin;
    private Context context;
    private long[] dateArr;
    private FormatInvoke formatInvoke;
    private float leftMargin;
    private Paint paint;
    private int rectColor;
    private ArrayList<RectF> rectFS;
    private float rectWidth;
    private float rightMargin;
    private float textSize;
    private float topMargin;
    private int[] weekValue;

    /* loaded from: classes.dex */
    public interface FormatInvoke {
        String format(int i);
    }

    public WeekRectView(Context context) {
        super(context);
        this.context = null;
        this.paint = null;
        this.textSize = 20.0f;
        this.Px1 = 1.0f;
        this.dateArr = new long[7];
        this.weekValue = new int[7];
        this.rectFS = new ArrayList<>();
        this.rectColor = -6040320;
        init(context);
    }

    public WeekRectView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = null;
        this.paint = null;
        this.textSize = 20.0f;
        this.Px1 = 1.0f;
        this.dateArr = new long[7];
        this.weekValue = new int[7];
        this.rectFS = new ArrayList<>();
        this.rectColor = -6040320;
        init(context);
    }

    public WeekRectView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = null;
        this.paint = null;
        this.textSize = 20.0f;
        this.Px1 = 1.0f;
        this.dateArr = new long[7];
        this.weekValue = new int[7];
        this.rectFS = new ArrayList<>();
        this.rectColor = -6040320;
        init(context);
    }

    private void drawRects(Canvas canvas) {
        int length = this.dateArr.length;
        if (this.dateArr == null || length < 1) {
            return;
        }
        int max = MathUtils.getMax(this.weekValue);
        this.rectFS.clear();
        float width = (getWidth() - this.leftMargin) - this.rightMargin;
        float height = ((getHeight() - this.topMargin) - this.bottomMargin) - (this.textSize * 2.0f);
        float f = (width - (this.rectWidth * 7.0f)) / 6.0f;
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < 7; i++) {
            float f2 = max > 0 ? 1.0f - ((this.weekValue[i] * 1.0f) / max) : 1.0f;
            RectF rectF = new RectF();
            rectF.left = this.leftMargin + ((this.rectWidth + f) * i);
            rectF.top = (f2 * height) + this.topMargin + (this.textSize * 2.0f);
            rectF.right = rectF.left + this.rectWidth;
            rectF.bottom = (getHeight() - this.bottomMargin) - this.Px1;
            this.rectFS.add(rectF);
            this.paint.setColor(this.rectColor);
            canvas.drawRect(rectF, this.paint);
            this.paint.setColor(-1);
            if (this.formatInvoke != null) {
                canvas.drawText(this.formatInvoke.format(this.weekValue[i]), rectF.centerX(), rectF.top - (this.Px1 * 2.0f), this.paint);
            } else {
                canvas.drawText(this.weekValue[i] + "", rectF.centerX(), rectF.top - (this.Px1 * 2.0f), this.paint);
            }
        }
    }

    private void drawTexts(Canvas canvas) {
        int length;
        if (this.rectFS.size() >= 7 && (length = this.dateArr.length) > 0) {
            this.paint.setTextAlign(Paint.Align.CENTER);
            this.paint.setColor(-1);
            if (this.dateArr == null || length < 1) {
                return;
            }
            for (int i = 0; i < length; i++) {
                RectF rectF = this.rectFS.get(i);
                canvas.drawText(smp.format(Long.valueOf(this.dateArr[i])), rectF.centerX(), rectF.bottom + this.textSize, this.paint);
            }
        }
    }

    private void drawTopAndBottomLine(Canvas canvas) {
        LogUtil.e("debug==绘制上下线");
        this.paint.setColor(-1);
        float f = this.topMargin;
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX));
        canvas.drawLine(this.leftMargin / 2.0f, f, getWidth() - (this.rightMargin / 2.0f), f, this.paint);
        float height = getHeight() - this.bottomMargin;
        canvas.drawLine(this.leftMargin / 2.0f, height, getWidth() - (this.rightMargin / 2.0f), height, this.paint);
    }

    private void init(Context context) {
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_12PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_80PX);
        this.rectWidth = getResources().getDimension(R.dimen.DIMEN_30PX);
        this.textSize = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.Px1 = getResources().getDimension(R.dimen.DIMEN_1PX);
        this.paint.setTextSize(this.textSize);
    }

    public void initCfg(int i) {
        this.rectColor = i;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTopAndBottomLine(canvas);
        drawRects(canvas);
        drawTexts(canvas);
    }

    public void updateShow(long[] jArr, int[] iArr, FormatInvoke formatInvoke) {
        this.formatInvoke = formatInvoke;
        this.dateArr = jArr;
        this.weekValue = iArr;
        for (long j : jArr) {
            LogUtil.e("debug-start   " + j);
        }
        requestLayout();
        invalidate();
    }
}
