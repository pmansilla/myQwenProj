package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.CustomerLogo;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class PlatformPageAdapter extends ViewPagerAdapter implements View.OnClickListener {
    public static final int DESIGN_BOTTOM_HEIGHT = 52;
    protected static final int MIN_CLICK_INTERVAL = 1000;
    protected int bottomHeight;
    protected int cellHeight;
    protected Object[][] cells;
    private long lastClickTime;
    protected int lineSize;
    protected int logoHeight;
    protected int paddingTop;
    private PlatformPage page;
    protected int panelHeight;
    protected int sepLineWidth;
    private IndicatorView vInd;

    public PlatformPageAdapter(PlatformPage platformPage, ArrayList<Object> arrayList) {
        this.page = platformPage;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        calculateSize(platformPage.getContext(), arrayList);
        collectCells(arrayList);
    }

    private View createPanel(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-855310);
        int i = this.panelHeight / this.cellHeight;
        LinearLayout[] linearLayoutArr = new LinearLayout[this.lineSize * i];
        linearLayout.setTag(linearLayoutArr);
        int bitmapRes = ResHelper.getBitmapRes(context, "ssdk_oks_classic_platform_cell_back");
        for (int i2 = 0; i2 < i; i2++) {
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, this.cellHeight));
            for (int i3 = 0; i3 < this.lineSize; i3++) {
                linearLayoutArr[(this.lineSize * i2) + i3] = new LinearLayout(context);
                linearLayoutArr[(this.lineSize * i2) + i3].setBackgroundResource(bitmapRes);
                linearLayoutArr[(this.lineSize * i2) + i3].setOrientation(1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, this.cellHeight);
                layoutParams.weight = 1.0f;
                linearLayout2.addView(linearLayoutArr[(this.lineSize * i2) + i3], layoutParams);
                if (i3 < this.lineSize - 1) {
                    linearLayout2.addView(new View(context), new LinearLayout.LayoutParams(this.sepLineWidth, -1));
                }
            }
            linearLayout.addView(new View(context), new LinearLayout.LayoutParams(-1, this.sepLineWidth));
        }
        for (LinearLayout linearLayout3 : linearLayoutArr) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, this.logoHeight);
            layoutParams2.topMargin = this.paddingTop;
            linearLayout3.addView(imageView, layoutParams2);
            TextView textView = new TextView(context);
            textView.setTextColor(-10197916);
            textView.setTextSize(2, 14.0f);
            textView.setGravity(17);
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams3.weight = 1.0f;
            linearLayout3.addView(textView, layoutParams3);
        }
        return linearLayout;
    }

    private void refreshPanel(LinearLayout[] linearLayoutArr, Object[] objArr) {
        int bitmapRes = ResHelper.getBitmapRes(this.page.getContext(), "ssdk_oks_classic_platform_cell_back");
        int bitmapRes2 = ResHelper.getBitmapRes(this.page.getContext(), "ssdk_oks_classic_platfrom_cell_back_nor");
        for (int i = 0; i < objArr.length; i++) {
            ImageView imageView = (ImageView) ResHelper.forceCast(linearLayoutArr[i].getChildAt(0));
            TextView textView = (TextView) ResHelper.forceCast(linearLayoutArr[i].getChildAt(1));
            if (objArr[i] == null) {
                imageView.setVisibility(4);
                textView.setVisibility(4);
                linearLayoutArr[i].setBackgroundResource(bitmapRes2);
                linearLayoutArr[i].setOnClickListener(null);
            } else {
                imageView.setVisibility(0);
                textView.setVisibility(0);
                imageView.requestLayout();
                textView.requestLayout();
                linearLayoutArr[i].setBackgroundResource(bitmapRes);
                linearLayoutArr[i].setOnClickListener(this);
                linearLayoutArr[i].setTag(objArr[i]);
                if (objArr[i] instanceof CustomerLogo) {
                    CustomerLogo customerLogo = (CustomerLogo) ResHelper.forceCast(objArr[i]);
                    if (customerLogo.logo != null) {
                        imageView.setImageBitmap(customerLogo.logo);
                    } else {
                        imageView.setImageBitmap(null);
                    }
                    if (customerLogo.label != null) {
                        textView.setText(customerLogo.label);
                    } else {
                        textView.setText("");
                    }
                } else {
                    String lowerCase = ((Platform) ResHelper.forceCast(objArr[i])).getName().toLowerCase();
                    int bitmapRes3 = ResHelper.getBitmapRes(imageView.getContext(), "ssdk_oks_classic_" + lowerCase);
                    if (bitmapRes3 > 0) {
                        imageView.setImageResource(bitmapRes3);
                    } else {
                        imageView.setImageBitmap(null);
                    }
                    int stringRes = ResHelper.getStringRes(textView.getContext(), "ssdk_" + lowerCase);
                    if (stringRes > 0) {
                        textView.setText(stringRes);
                    } else {
                        textView.setText("");
                    }
                }
            }
        }
    }

    protected abstract void calculateSize(Context context, ArrayList<Object> arrayList);

    protected abstract void collectCells(ArrayList<Object> arrayList);

    public int getBottomHeight() {
        return this.bottomHeight;
    }

    @Override // com.mob.tools.gui.ViewPagerAdapter
    public int getCount() {
        if (this.cells == null) {
            return 0;
        }
        return this.cells.length;
    }

    public int getPanelHeight() {
        return this.panelHeight;
    }

    @Override // com.mob.tools.gui.ViewPagerAdapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = createPanel(viewGroup.getContext());
        }
        refreshPanel((LinearLayout[]) ResHelper.forceCast(((LinearLayout) ResHelper.forceCast(view)).getTag()), this.cells[i]);
        return view;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastClickTime < 1000) {
            return;
        }
        this.lastClickTime = currentTimeMillis;
        if (view.getTag() instanceof CustomerLogo) {
            this.page.performCustomLogoClick(view, (CustomerLogo) ResHelper.forceCast(view.getTag()));
        } else {
            this.page.showEditPage((Platform) ResHelper.forceCast(view.getTag()));
        }
    }

    @Override // com.mob.tools.gui.ViewPagerAdapter
    public void onScreenChange(int i, int i2) {
        if (this.vInd != null) {
            this.vInd.setScreenCount(getCount());
            this.vInd.onScreenChange(i, i2);
        }
    }

    public void setIndicator(IndicatorView indicatorView) {
        this.vInd = indicatorView;
    }
}
