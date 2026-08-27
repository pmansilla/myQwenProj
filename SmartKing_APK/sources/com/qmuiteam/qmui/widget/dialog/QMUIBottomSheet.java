package com.qmuiteam.qmui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class QMUIBottomSheet extends Dialog {
    private static final String TAG = "QMUIBottomSheet";
    private static final int mAnimationDuration = 200;
    private View mContentView;
    private boolean mIsAnimating;
    private OnBottomSheetShowListener mOnBottomSheetShowListener;

    /* loaded from: classes2.dex */
    public static class BottomGridSheetBuilder implements View.OnClickListener {
        public static final int FIRST_LINE = 0;
        public static final int SECOND_LINE = 1;
        private TextView mBottomButton;
        private ViewGroup mBottomButtonContainer;
        private Context mContext;
        private QMUIBottomSheet mDialog;
        private OnSheetItemClickListener mOnSheetItemClickListener;
        private int mMiniItemWidth = -1;
        private Typeface mItemTextTypeFace = null;
        private Typeface mBottomButtonTypeFace = null;
        private boolean mIsShowButton = true;
        private CharSequence mButtonText = null;
        private View.OnClickListener mButtonClickListener = null;
        private SparseArray<View> mFirstLineViews = new SparseArray<>();
        private SparseArray<View> mSecondLineViews = new SparseArray<>();

        /* loaded from: classes2.dex */
        public interface OnSheetItemClickListener {
            void onClick(QMUIBottomSheet qMUIBottomSheet, View view);
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface Style {
        }

        public BottomGridSheetBuilder(Context context) {
            this.mContext = context;
        }

        private void addViewsInSection(SparseArray<View> sparseArray, LinearLayout linearLayout, int i) {
            for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                View view = sparseArray.get(i2);
                setItemWidth(view, i);
                linearLayout.addView(view);
            }
        }

        private View buildViews() {
            LinearLayout linearLayout = (LinearLayout) View.inflate(this.mContext, getContentViewLayoutId(), null);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.bottom_sheet_first_linear_layout);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout.findViewById(R.id.bottom_sheet_second_linear_layout);
            this.mBottomButtonContainer = (ViewGroup) linearLayout.findViewById(R.id.bottom_sheet_button_container);
            this.mBottomButton = (TextView) linearLayout.findViewById(R.id.bottom_sheet_close_button);
            int max = Math.max(this.mFirstLineViews.size(), this.mSecondLineViews.size());
            int screenWidth = QMUIDisplayHelper.getScreenWidth(this.mContext);
            int screenHeight = QMUIDisplayHelper.getScreenHeight(this.mContext);
            if (screenWidth >= screenHeight) {
                screenWidth = screenHeight;
            }
            int calculateItemWidth = calculateItemWidth(screenWidth, max, linearLayout2.getPaddingLeft(), linearLayout2.getPaddingRight());
            addViewsInSection(this.mFirstLineViews, linearLayout2, calculateItemWidth);
            addViewsInSection(this.mSecondLineViews, linearLayout3, calculateItemWidth);
            boolean z = this.mFirstLineViews.size() > 0;
            boolean z2 = this.mSecondLineViews.size() > 0;
            if (!z) {
                linearLayout2.setVisibility(8);
            }
            if (!z2) {
                if (z) {
                    linearLayout2.setPadding(linearLayout2.getPaddingLeft(), linearLayout2.getPaddingTop(), linearLayout2.getPaddingRight(), 0);
                }
                linearLayout3.setVisibility(8);
            }
            if (this.mBottomButtonContainer != null) {
                if (this.mIsShowButton) {
                    this.mBottomButtonContainer.setVisibility(0);
                    linearLayout.setPadding(0, QMUIResHelper.getAttrDimen(this.mContext, R.attr.qmui_bottom_sheet_grid_padding_vertical), 0, 0);
                } else {
                    this.mBottomButtonContainer.setVisibility(8);
                }
                if (this.mBottomButtonTypeFace != null) {
                    this.mBottomButton.setTypeface(this.mBottomButtonTypeFace);
                }
                if (this.mButtonText != null) {
                    this.mBottomButton.setText(this.mButtonText);
                }
                if (this.mButtonClickListener != null) {
                    this.mBottomButton.setOnClickListener(this.mButtonClickListener);
                } else {
                    this.mBottomButton.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomGridSheetBuilder.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            BottomGridSheetBuilder.this.mDialog.dismiss();
                        }
                    });
                }
            }
            return linearLayout;
        }

        private int calculateItemWidth(int i, int i2, int i3, int i4) {
            int i5;
            if (this.mMiniItemWidth == -1) {
                this.mMiniItemWidth = QMUIResHelper.getAttrDimen(this.mContext, R.attr.qmui_bottom_sheet_grid_item_mini_width);
            }
            int i6 = i - i3;
            int i7 = i6 - i4;
            int i8 = this.mMiniItemWidth;
            if (i2 >= 3 && (i5 = i7 - (i2 * i8)) > 0 && i5 < i8) {
                i8 = i7 / (i7 / i8);
            }
            return i2 * i8 > i7 ? (int) (i6 / ((i6 / i8) + 0.5f)) : i8;
        }

        private void setItemWidth(View view, int i) {
            LinearLayout.LayoutParams layoutParams;
            if (view.getLayoutParams() != null) {
                layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = i;
            } else {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i, -2);
                view.setLayoutParams(layoutParams2);
                layoutParams = layoutParams2;
            }
            layoutParams.gravity = 48;
        }

        public BottomGridSheetBuilder addItem(int i, CharSequence charSequence, int i2) {
            return addItem(i, charSequence, charSequence, i2, 0);
        }

        public BottomGridSheetBuilder addItem(int i, CharSequence charSequence, Object obj, int i2) {
            return addItem(i, charSequence, obj, i2, 0);
        }

        public BottomGridSheetBuilder addItem(int i, CharSequence charSequence, Object obj, int i2, int i3) {
            return addItem(createItemView(AppCompatResources.getDrawable(this.mContext, i), charSequence, obj, i3), i2);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:4:0x001b, code lost:
        
            return r1;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomGridSheetBuilder addItem(android.view.View r2, int r3) {
            /*
                r1 = this;
                switch(r3) {
                    case 0: goto L10;
                    case 1: goto L4;
                    default: goto L3;
                }
            L3:
                goto L1b
            L4:
                android.util.SparseArray<android.view.View> r3 = r1.mSecondLineViews
                android.util.SparseArray<android.view.View> r0 = r1.mSecondLineViews
                int r0 = r0.size()
                r3.append(r0, r2)
                goto L1b
            L10:
                android.util.SparseArray<android.view.View> r3 = r1.mFirstLineViews
                android.util.SparseArray<android.view.View> r0 = r1.mFirstLineViews
                int r0 = r0.size()
                r3.append(r0, r2)
            L1b:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomGridSheetBuilder.addItem(android.view.View, int):com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet$BottomGridSheetBuilder");
        }

        public QMUIBottomSheet build() {
            this.mDialog = new QMUIBottomSheet(this.mContext);
            this.mDialog.setContentView(buildViews(), new ViewGroup.LayoutParams(-1, -2));
            return this.mDialog;
        }

        public QMUIBottomSheetItemView createItemView(Drawable drawable, CharSequence charSequence, Object obj, int i) {
            QMUIBottomSheetItemView qMUIBottomSheetItemView = (QMUIBottomSheetItemView) LayoutInflater.from(this.mContext).inflate(getItemViewLayoutId(), (ViewGroup) null, false);
            TextView textView = (TextView) qMUIBottomSheetItemView.findViewById(R.id.grid_item_title);
            if (this.mItemTextTypeFace != null) {
                textView.setTypeface(this.mItemTextTypeFace);
            }
            textView.setText(charSequence);
            qMUIBottomSheetItemView.setTag(obj);
            qMUIBottomSheetItemView.setOnClickListener(this);
            ((AppCompatImageView) qMUIBottomSheetItemView.findViewById(R.id.grid_item_image)).setImageDrawable(drawable);
            if (i != 0) {
                ((ImageView) ((ViewStub) qMUIBottomSheetItemView.findViewById(R.id.grid_item_subscript)).inflate()).setImageResource(i);
            }
            return qMUIBottomSheetItemView;
        }

        protected int getContentViewLayoutId() {
            return R.layout.qmui_bottom_sheet_grid;
        }

        protected int getItemViewLayoutId() {
            return R.layout.qmui_bottom_sheet_grid_item;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.mOnSheetItemClickListener != null) {
                this.mOnSheetItemClickListener.onClick(this.mDialog, view);
            }
        }

        public BottomGridSheetBuilder setBottomButtonTypeFace(Typeface typeface) {
            this.mBottomButtonTypeFace = typeface;
            return this;
        }

        public BottomGridSheetBuilder setButtonClickListener(View.OnClickListener onClickListener) {
            this.mButtonClickListener = onClickListener;
            return this;
        }

        public BottomGridSheetBuilder setButtonText(CharSequence charSequence) {
            this.mButtonText = charSequence;
            return this;
        }

        public BottomGridSheetBuilder setIsShowButton(boolean z) {
            this.mIsShowButton = z;
            return this;
        }

        public BottomGridSheetBuilder setItemTextTypeFace(Typeface typeface) {
            this.mItemTextTypeFace = typeface;
            return this;
        }

        public void setItemVisibility(Object obj, int i) {
            View view = null;
            for (int i2 = 0; i2 < this.mFirstLineViews.size(); i2++) {
                View view2 = this.mFirstLineViews.get(i2);
                if (view2 != null && view2.getTag().equals(obj)) {
                    view = view2;
                }
            }
            for (int i3 = 0; i3 < this.mSecondLineViews.size(); i3++) {
                View view3 = this.mSecondLineViews.get(i3);
                if (view3 != null && view3.getTag().equals(obj)) {
                    view = view3;
                }
            }
            if (view != null) {
                view.setVisibility(i);
            }
        }

        public BottomGridSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
            this.mOnSheetItemClickListener = onSheetItemClickListener;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class BottomListSheetBuilder {
        private BaseAdapter mAdapter;
        private int mCheckedIndex;
        private ListView mContainerView;
        private Context mContext;
        private QMUIBottomSheet mDialog;
        private List<View> mHeaderViews;
        private List<BottomSheetListItemData> mItems;
        private boolean mNeedRightMark;
        private DialogInterface.OnDismissListener mOnBottomDialogDismissListener;
        private OnSheetItemClickListener mOnSheetItemClickListener;
        private String mTitle;
        private TextView mTitleTv;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class BottomSheetListItemData {
            boolean hasRedPoint;
            Drawable image;
            boolean isDisabled;
            String tag;
            String text;

            public BottomSheetListItemData(Drawable drawable, String str, String str2) {
                this.image = null;
                this.tag = "";
                this.hasRedPoint = false;
                this.isDisabled = false;
                this.image = drawable;
                this.text = str;
                this.tag = str2;
            }

            public BottomSheetListItemData(Drawable drawable, String str, String str2, boolean z) {
                this.image = null;
                this.tag = "";
                this.hasRedPoint = false;
                this.isDisabled = false;
                this.image = drawable;
                this.text = str;
                this.tag = str2;
                this.hasRedPoint = z;
            }

            public BottomSheetListItemData(Drawable drawable, String str, String str2, boolean z, boolean z2) {
                this.image = null;
                this.tag = "";
                this.hasRedPoint = false;
                this.isDisabled = false;
                this.image = drawable;
                this.text = str;
                this.tag = str2;
                this.hasRedPoint = z;
                this.isDisabled = z2;
            }

            public BottomSheetListItemData(String str, String str2) {
                this.image = null;
                this.tag = "";
                this.hasRedPoint = false;
                this.isDisabled = false;
                this.text = str;
                this.tag = str2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class ListAdapter extends BaseAdapter {
            private ListAdapter() {
            }

            @Override // android.widget.Adapter
            public int getCount() {
                return BottomListSheetBuilder.this.mItems.size();
            }

            @Override // android.widget.Adapter
            public BottomSheetListItemData getItem(int i) {
                return (BottomSheetListItemData) BottomListSheetBuilder.this.mItems.get(i);
            }

            @Override // android.widget.Adapter
            public long getItemId(int i) {
                return 0L;
            }

            @Override // android.widget.Adapter
            public View getView(final int i, View view, ViewGroup viewGroup) {
                final ViewHolder viewHolder;
                final BottomSheetListItemData item = getItem(i);
                if (view == null) {
                    view = LayoutInflater.from(BottomListSheetBuilder.this.mContext).inflate(R.layout.qmui_bottom_sheet_list_item, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.bottom_dialog_list_item_img);
                    viewHolder.textView = (TextView) view.findViewById(R.id.bottom_dialog_list_item_title);
                    viewHolder.markView = view.findViewById(R.id.bottom_dialog_list_item_mark_view_stub);
                    viewHolder.redPoint = view.findViewById(R.id.bottom_dialog_list_item_point);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                if (item.image != null) {
                    viewHolder.imageView.setVisibility(0);
                    viewHolder.imageView.setImageDrawable(item.image);
                } else {
                    viewHolder.imageView.setVisibility(8);
                }
                viewHolder.textView.setText(item.text);
                if (item.hasRedPoint) {
                    viewHolder.redPoint.setVisibility(0);
                } else {
                    viewHolder.redPoint.setVisibility(8);
                }
                if (item.isDisabled) {
                    viewHolder.textView.setEnabled(false);
                    view.setEnabled(false);
                } else {
                    viewHolder.textView.setEnabled(true);
                    view.setEnabled(true);
                }
                if (BottomListSheetBuilder.this.mNeedRightMark) {
                    if (viewHolder.markView instanceof ViewStub) {
                        viewHolder.markView = ((ViewStub) viewHolder.markView).inflate();
                    }
                    if (BottomListSheetBuilder.this.mCheckedIndex == i) {
                        viewHolder.markView.setVisibility(0);
                    } else {
                        viewHolder.markView.setVisibility(8);
                    }
                } else {
                    viewHolder.markView.setVisibility(8);
                }
                view.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder.ListAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (item.hasRedPoint) {
                            item.hasRedPoint = false;
                            viewHolder.redPoint.setVisibility(8);
                        }
                        if (BottomListSheetBuilder.this.mNeedRightMark) {
                            BottomListSheetBuilder.this.setCheckedIndex(i);
                            ListAdapter.this.notifyDataSetChanged();
                        }
                        if (BottomListSheetBuilder.this.mOnSheetItemClickListener != null) {
                            BottomListSheetBuilder.this.mOnSheetItemClickListener.onClick(BottomListSheetBuilder.this.mDialog, view2, i, item.tag);
                        }
                    }
                });
                return view;
            }
        }

        /* loaded from: classes2.dex */
        public interface OnSheetItemClickListener {
            void onClick(QMUIBottomSheet qMUIBottomSheet, View view, int i, String str);
        }

        /* loaded from: classes2.dex */
        private static class ViewHolder {
            ImageView imageView;
            View markView;
            View redPoint;
            TextView textView;

            private ViewHolder() {
            }
        }

        public BottomListSheetBuilder(Context context) {
            this(context, false);
        }

        public BottomListSheetBuilder(Context context, boolean z) {
            this.mContext = context;
            this.mItems = new ArrayList();
            this.mHeaderViews = new ArrayList();
            this.mNeedRightMark = z;
        }

        private View buildViews() {
            View inflate = View.inflate(this.mContext, getContentViewLayoutId(), null);
            this.mTitleTv = (TextView) inflate.findViewById(R.id.title);
            this.mContainerView = (ListView) inflate.findViewById(R.id.listview);
            if (this.mTitle == null || this.mTitle.length() == 0) {
                this.mTitleTv.setVisibility(8);
            } else {
                this.mTitleTv.setVisibility(0);
                this.mTitleTv.setText(this.mTitle);
            }
            if (this.mHeaderViews.size() > 0) {
                Iterator<View> it = this.mHeaderViews.iterator();
                while (it.hasNext()) {
                    this.mContainerView.addHeaderView(it.next());
                }
            }
            if (needToScroll()) {
                this.mContainerView.getLayoutParams().height = getListMaxHeight();
                this.mDialog.setOnBottomSheetShowListener(new OnBottomSheetShowListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder.1
                    @Override // com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.OnBottomSheetShowListener
                    public void onShow() {
                        BottomListSheetBuilder.this.mContainerView.setSelection(BottomListSheetBuilder.this.mCheckedIndex);
                    }
                });
            }
            this.mAdapter = new ListAdapter();
            this.mContainerView.setAdapter((android.widget.ListAdapter) this.mAdapter);
            return inflate;
        }

        private boolean needToScroll() {
            int size = this.mItems.size() * QMUIResHelper.getAttrDimen(this.mContext, R.attr.qmui_bottom_sheet_list_item_height);
            if (this.mHeaderViews.size() > 0) {
                for (View view : this.mHeaderViews) {
                    if (view.getMeasuredHeight() == 0) {
                        view.measure(0, 0);
                    }
                    size += view.getMeasuredHeight();
                }
            }
            if (this.mTitleTv != null && !QMUILangHelper.isNullOrEmpty(this.mTitle)) {
                size += QMUIResHelper.getAttrDimen(this.mContext, R.attr.qmui_bottom_sheet_title_height);
            }
            return size > getListMaxHeight();
        }

        public BottomListSheetBuilder addHeaderView(View view) {
            if (view != null) {
                this.mHeaderViews.add(view);
            }
            return this;
        }

        public BottomListSheetBuilder addItem(int i, String str, String str2) {
            this.mItems.add(new BottomSheetListItemData(i != 0 ? ContextCompat.getDrawable(this.mContext, i) : null, str, str2));
            return this;
        }

        public BottomListSheetBuilder addItem(int i, String str, String str2, boolean z) {
            this.mItems.add(new BottomSheetListItemData(i != 0 ? ContextCompat.getDrawable(this.mContext, i) : null, str, str2, z));
            return this;
        }

        public BottomListSheetBuilder addItem(int i, String str, String str2, boolean z, boolean z2) {
            this.mItems.add(new BottomSheetListItemData(i != 0 ? ContextCompat.getDrawable(this.mContext, i) : null, str, str2, z, z2));
            return this;
        }

        public BottomListSheetBuilder addItem(Drawable drawable, String str) {
            this.mItems.add(new BottomSheetListItemData(drawable, str, str));
            return this;
        }

        public BottomListSheetBuilder addItem(String str) {
            this.mItems.add(new BottomSheetListItemData(str, str));
            return this;
        }

        public BottomListSheetBuilder addItem(String str, String str2) {
            this.mItems.add(new BottomSheetListItemData(str, str2));
            return this;
        }

        public QMUIBottomSheet build() {
            this.mDialog = new QMUIBottomSheet(this.mContext);
            this.mDialog.setContentView(buildViews(), new ViewGroup.LayoutParams(-1, -2));
            if (this.mOnBottomDialogDismissListener != null) {
                this.mDialog.setOnDismissListener(this.mOnBottomDialogDismissListener);
            }
            return this.mDialog;
        }

        protected int getContentViewLayoutId() {
            return R.layout.qmui_bottom_sheet_list;
        }

        protected int getListMaxHeight() {
            double screenHeight = QMUIDisplayHelper.getScreenHeight(this.mContext);
            Double.isNaN(screenHeight);
            return (int) (screenHeight * 0.5d);
        }

        public void notifyDataSetChanged() {
            if (this.mAdapter != null) {
                this.mAdapter.notifyDataSetChanged();
            }
            if (needToScroll()) {
                this.mContainerView.getLayoutParams().height = getListMaxHeight();
                this.mContainerView.setSelection(this.mCheckedIndex);
            }
        }

        public BottomListSheetBuilder setCheckedIndex(int i) {
            this.mCheckedIndex = i;
            return this;
        }

        public BottomListSheetBuilder setOnBottomDialogDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.mOnBottomDialogDismissListener = onDismissListener;
            return this;
        }

        public BottomListSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
            this.mOnSheetItemClickListener = onSheetItemClickListener;
            return this;
        }

        public BottomListSheetBuilder setTitle(int i) {
            this.mTitle = this.mContext.getResources().getString(i);
            return this;
        }

        public BottomListSheetBuilder setTitle(String str) {
            this.mTitle = str;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public interface OnBottomSheetShowListener {
        void onShow();
    }

    public QMUIBottomSheet(Context context) {
        super(context, R.style.QMUI_BottomSheet);
        this.mIsAnimating = false;
    }

    private void animateDown() {
        if (this.mContentView == null) {
            return;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(200L);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                QMUIBottomSheet.this.mIsAnimating = false;
                QMUIBottomSheet.this.mContentView.post(new Runnable() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QMUIBottomSheet.super.dismiss();
                        } catch (Exception e) {
                            QMUILog.w(QMUIBottomSheet.TAG, "dismiss error\n" + Log.getStackTraceString(e), new Object[0]);
                        }
                    }
                });
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                QMUIBottomSheet.this.mIsAnimating = true;
            }
        });
        this.mContentView.startAnimation(animationSet);
    }

    private void animateUp() {
        if (this.mContentView == null) {
            return;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(200L);
        animationSet.setFillAfter(true);
        this.mContentView.startAnimation(animationSet);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (this.mIsAnimating) {
            return;
        }
        animateDown();
    }

    public View getContentView() {
        return this.mContentView;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.height = -2;
        attributes.gravity = 81;
        int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext());
        int screenHeight = QMUIDisplayHelper.getScreenHeight(getContext());
        if (screenWidth >= screenHeight) {
            screenWidth = screenHeight;
        }
        attributes.width = screenWidth;
        getWindow().setAttributes(attributes);
        setCanceledOnTouchOutside(true);
    }

    @Override // android.app.Dialog
    public void setContentView(int i) {
        this.mContentView = LayoutInflater.from(getContext()).inflate(i, (ViewGroup) null);
        super.setContentView(this.mContentView);
    }

    @Override // android.app.Dialog
    public void setContentView(@NonNull View view) {
        this.mContentView = view;
        super.setContentView(view);
    }

    @Override // android.app.Dialog
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams layoutParams) {
        this.mContentView = view;
        super.setContentView(view, layoutParams);
    }

    public void setOnBottomSheetShowListener(OnBottomSheetShowListener onBottomSheetShowListener) {
        this.mOnBottomSheetShowListener = onBottomSheetShowListener;
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        animateUp();
        if (this.mOnBottomSheetShowListener != null) {
            this.mOnBottomSheetShowListener.onShow();
        }
    }
}
