package com.czw.smartkit.dialog.pop;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.modes.widget.MyViewPager;
import com.czw.smartkit.R;
import com.czw.utils.LogUtil;
import java.util.ArrayList;
import me.panpf.sketch.SketchImageView;

/* loaded from: classes.dex */
public class ImagePoper extends RootPop {
    private static ImagePoper picker;
    protected ViewPagerAdapter adapter;
    private TextView imageIndex;
    private MyViewPager viewPager;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public abstract void onClick(int i);

        public void onDismiss() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<SketchImageView> pages;

        public ViewPagerAdapter(ArrayList<SketchImageView> arrayList) {
            this.pages = null;
            this.pages = arrayList;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView(this.pages.get(i));
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.pages.size();
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            viewGroup.addView(this.pages.get(i));
            return this.pages.get(i);
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    public ImagePoper(Context context) {
        super(context);
    }

    public static ImagePoper create(Context context) {
        synchronized (Void.class) {
            if (picker == null) {
                synchronized (Void.class) {
                    picker = new ImagePoper(context);
                }
            }
        }
        return picker;
    }

    private void initPage(ArrayList<String> arrayList, int i) {
        ArrayList arrayList2 = new ArrayList();
        arrayList2.clear();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            SketchImageView sketchImageView = new SketchImageView(this.context);
            sketchImageView.displayImage(arrayList.get(i2));
            sketchImageView.setZoomEnabled(true);
            sketchImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            sketchImageView.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.ImagePoper.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ImagePoper.this.dismiss();
                }
            });
            arrayList2.add(sketchImageView);
        }
        this.adapter = new ViewPagerAdapter(arrayList2);
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setCurrentItem(i);
        this.adapter.notifyDataSetChanged();
        this.imageIndex.setText(String.format("%d/%d", Integer.valueOf(i + 1), Integer.valueOf(this.adapter.getCount())));
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected void initSelf() {
        this.popupWindow.setHeight(-1);
        this.imageIndex = (TextView) $View(R.id.imageIndex);
        this.viewPager = (MyViewPager) $View(R.id.viewPager);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.czw.smartkit.dialog.pop.ImagePoper.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                ImagePoper.this.imageIndex.setText(String.format("%d/%d", Integer.valueOf(i + 1), Integer.valueOf(ImagePoper.this.adapter.getCount())));
            }
        });
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected int loadLayout() {
        return R.layout.pop_image;
    }

    public ImagePoper setItems(ArrayList<String> arrayList, int i) {
        initPage(arrayList, i);
        return this;
    }

    public ImagePoper showPicker(View view, ClickCallback clickCallback) {
        LogUtil.e("debug_refLayout" + view.toString());
        this.popupWindow.showAtLocation(view, 17, 0, 0);
        return this;
    }
}
