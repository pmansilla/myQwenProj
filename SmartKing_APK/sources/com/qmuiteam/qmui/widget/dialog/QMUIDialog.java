package com.qmuiteam.qmui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIWrapContentScrollView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class QMUIDialog extends Dialog {
    private Context mBaseContext;
    boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private boolean mCanceledOnTouchOutsideSet;

    /* loaded from: classes2.dex */
    public static abstract class AutoResizeDialogBuilder extends QMUIDialogBuilder {
        private int mAnchorHeight;
        private int mScreenHeight;
        private int mScrollHeight;
        private ScrollView mScrollerView;

        public AutoResizeDialogBuilder(Context context) {
            super(context);
            this.mAnchorHeight = 0;
            this.mScreenHeight = 0;
            this.mScrollHeight = 0;
        }

        private void bindEvent(final Context context) {
            this.mAnchorTopView.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.AutoResizeDialogBuilder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AutoResizeDialogBuilder.this.mDialog.dismiss();
                }
            });
            this.mAnchorBottomView.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.AutoResizeDialogBuilder.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AutoResizeDialogBuilder.this.mDialog.dismiss();
                }
            });
            this.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.AutoResizeDialogBuilder.3
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    View decorView = AutoResizeDialogBuilder.this.mDialog.getWindow().getDecorView();
                    Rect rect = new Rect();
                    decorView.getWindowVisibleDisplayFrame(rect);
                    AutoResizeDialogBuilder.this.mScreenHeight = QMUIDisplayHelper.getScreenHeight(context);
                    int i = AutoResizeDialogBuilder.this.mScreenHeight - rect.bottom;
                    if (i == AutoResizeDialogBuilder.this.mAnchorHeight) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) AutoResizeDialogBuilder.this.mDialogView.getLayoutParams();
                        int i2 = ((AutoResizeDialogBuilder.this.mScreenHeight - layoutParams.bottomMargin) - layoutParams.topMargin) - rect.top;
                        double measuredHeight = AutoResizeDialogBuilder.this.mScrollerView.getMeasuredHeight();
                        double d = i2;
                        Double.isNaN(d);
                        double d2 = d * 0.8d;
                        if (measuredHeight > d2) {
                            AutoResizeDialogBuilder.this.mScrollHeight = (int) d2;
                            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) AutoResizeDialogBuilder.this.mScrollerView.getLayoutParams();
                            layoutParams2.height = AutoResizeDialogBuilder.this.mScrollHeight;
                            AutoResizeDialogBuilder.this.mScrollerView.setLayoutParams(layoutParams2);
                            return;
                        }
                        return;
                    }
                    AutoResizeDialogBuilder.this.mAnchorHeight = i;
                    LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) AutoResizeDialogBuilder.this.mAnchorBottomView.getLayoutParams();
                    layoutParams3.height = AutoResizeDialogBuilder.this.mAnchorHeight;
                    AutoResizeDialogBuilder.this.mAnchorBottomView.setLayoutParams(layoutParams3);
                    LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) AutoResizeDialogBuilder.this.mScrollerView.getLayoutParams();
                    if (AutoResizeDialogBuilder.this.onGetScrollHeight() == -2) {
                        AutoResizeDialogBuilder.this.mScrollHeight = Math.max(AutoResizeDialogBuilder.this.mScrollHeight, AutoResizeDialogBuilder.this.mScrollerView.getMeasuredHeight());
                    } else {
                        AutoResizeDialogBuilder.this.mScrollHeight = AutoResizeDialogBuilder.this.onGetScrollHeight();
                    }
                    if (AutoResizeDialogBuilder.this.mAnchorHeight == 0) {
                        layoutParams4.height = AutoResizeDialogBuilder.this.mScrollHeight;
                    } else {
                        AutoResizeDialogBuilder.this.mScrollerView.getChildAt(0).requestFocus();
                        layoutParams4.height = AutoResizeDialogBuilder.this.mScrollHeight - AutoResizeDialogBuilder.this.mAnchorHeight;
                    }
                    AutoResizeDialogBuilder.this.mScrollerView.setLayoutParams(layoutParams4);
                }
            });
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onAfter(QMUIDialog qMUIDialog, LinearLayout linearLayout, Context context) {
            super.onAfter(qMUIDialog, linearLayout, context);
            bindEvent(context);
        }

        public abstract View onBuildContent(QMUIDialog qMUIDialog, ScrollView scrollView);

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            this.mScrollerView = new ScrollView(context);
            this.mScrollerView.setLayoutParams(new LinearLayout.LayoutParams(-1, onGetScrollHeight()));
            this.mScrollerView.addView(onBuildContent(qMUIDialog, this.mScrollerView));
            viewGroup.addView(this.mScrollerView);
        }

        public int onGetScrollHeight() {
            return -2;
        }
    }

    /* loaded from: classes2.dex */
    public static class CheckBoxMessageDialogBuilder extends QMUIDialogBuilder<CheckBoxMessageDialogBuilder> {
        private Drawable mCheckMarkDrawable;
        private boolean mIsChecked;
        protected String mMessage;
        private QMUIWrapContentScrollView mScrollContainer;
        private QMUISpanTouchFixTextView mTextView;

        public CheckBoxMessageDialogBuilder(Context context) {
            super(context);
            this.mIsChecked = false;
            this.mCheckMarkDrawable = QMUIResHelper.getAttrDrawable(context, R.attr.qmui_s_checkbox);
        }

        public QMUISpanTouchFixTextView getTextView() {
            return this.mTextView;
        }

        public boolean isChecked() {
            return this.mIsChecked;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            if (this.mMessage == null || this.mMessage.length() == 0) {
                return;
            }
            this.mScrollContainer = new QMUIWrapContentScrollView(context);
            this.mTextView = new QMUISpanTouchFixTextView(context);
            this.mTextView.setMovementMethodDefault();
            MessageDialogBuilder.assignMessageTvWithAttr(this.mTextView, hasTitle(), R.attr.qmui_dialog_message_content_style);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = this.mTextView.getGravity();
            this.mScrollContainer.addView(this.mTextView, layoutParams);
            this.mScrollContainer.setVerticalScrollBarEnabled(false);
            this.mScrollContainer.setMaxHeight(getContentAreaMaxHeight());
            this.mTextView.setText(this.mMessage);
            this.mCheckMarkDrawable.setBounds(0, 0, this.mCheckMarkDrawable.getIntrinsicWidth(), this.mCheckMarkDrawable.getIntrinsicHeight());
            this.mTextView.setCompoundDrawables(this.mCheckMarkDrawable, null, null, null);
            this.mTextView.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.CheckBoxMessageDialogBuilder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CheckBoxMessageDialogBuilder.this.setChecked(!CheckBoxMessageDialogBuilder.this.mIsChecked);
                }
            });
            this.mTextView.setSelected(this.mIsChecked);
            viewGroup.addView(this.mScrollContainer);
        }

        public CheckBoxMessageDialogBuilder setChecked(boolean z) {
            if (this.mIsChecked != z) {
                this.mIsChecked = z;
                if (this.mTextView != null) {
                    this.mTextView.setSelected(z);
                }
            }
            return this;
        }

        public CheckBoxMessageDialogBuilder setMessage(int i) {
            return setMessage(getBaseContext().getResources().getString(i));
        }

        public CheckBoxMessageDialogBuilder setMessage(String str) {
            this.mMessage = str;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class CheckableDialogBuilder extends MenuBaseDialogBuilder<CheckableDialogBuilder> {
        private int mCheckedIndex;

        public CheckableDialogBuilder(Context context) {
            super(context);
            this.mCheckedIndex = -1;
        }

        public CheckableDialogBuilder addItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            for (CharSequence charSequence : charSequenceArr) {
                addItem(new QMUIDialogMenuItemView.MarkItemView(getBaseContext(), charSequence), onClickListener);
            }
            return this;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        public int getCheckedIndex() {
            return this.mCheckedIndex;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder, com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            super.onCreateContent(qMUIDialog, viewGroup, context);
            if (this.mCheckedIndex <= -1 || this.mCheckedIndex >= this.mMenuItemViews.size()) {
                return;
            }
            this.mMenuItemViews.get(this.mCheckedIndex).setChecked(true);
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder
        protected void onItemClick(int i) {
            for (int i2 = 0; i2 < this.mMenuItemViews.size(); i2++) {
                QMUIDialogMenuItemView qMUIDialogMenuItemView = this.mMenuItemViews.get(i2);
                if (i2 == i) {
                    qMUIDialogMenuItemView.setChecked(true);
                    this.mCheckedIndex = i;
                } else {
                    qMUIDialogMenuItemView.setChecked(false);
                }
            }
        }

        public CheckableDialogBuilder setCheckedIndex(int i) {
            this.mCheckedIndex = i;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class CustomDialogBuilder extends QMUIDialogBuilder {
        private int mLayoutId;

        public CustomDialogBuilder(Context context) {
            super(context);
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            viewGroup.addView(LayoutInflater.from(context).inflate(this.mLayoutId, viewGroup, false));
        }

        public CustomDialogBuilder setLayout(@LayoutRes int i) {
            this.mLayoutId = i;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class EditTextDialogBuilder extends QMUIDialogBuilder<EditTextDialogBuilder> {
        private CharSequence mDefaultText;
        protected EditText mEditText;
        private int mInputType;
        protected RelativeLayout mMainLayout;
        protected String mPlaceholder;
        protected ImageView mRightImageView;
        protected TransformationMethod mTransformationMethod;

        public EditTextDialogBuilder(Context context) {
            super(context);
            this.mInputType = 1;
            this.mDefaultText = null;
        }

        protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(0, this.mRightImageView.getId());
            layoutParams.addRule(15, -1);
            return layoutParams;
        }

        protected RelativeLayout.LayoutParams createRightIconLayoutParams() {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(11, -1);
            layoutParams.addRule(15, -1);
            layoutParams.leftMargin = QMUIDisplayHelper.dpToPx(5);
            return layoutParams;
        }

        public EditText getEditText() {
            return this.mEditText;
        }

        public ImageView getRightImageView() {
            return this.mRightImageView;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onAfter(QMUIDialog qMUIDialog, LinearLayout linearLayout, Context context) {
            super.onAfter(qMUIDialog, linearLayout, context);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            qMUIDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.EditTextDialogBuilder.1
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    inputMethodManager.hideSoftInputFromWindow(EditTextDialogBuilder.this.mEditText.getWindowToken(), 0);
                }
            });
            this.mEditText.postDelayed(new Runnable() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.EditTextDialogBuilder.2
                @Override // java.lang.Runnable
                public void run() {
                    EditTextDialogBuilder.this.mEditText.requestFocus();
                    inputMethodManager.showSoftInput(EditTextDialogBuilder.this.mEditText, 0);
                }
            }, 300L);
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            this.mEditText = new EditText(context);
            MessageDialogBuilder.assignMessageTvWithAttr(this.mEditText, hasTitle(), R.attr.qmui_dialog_edit_content_style);
            this.mEditText.setFocusable(true);
            this.mEditText.setFocusableInTouchMode(true);
            this.mEditText.setImeOptions(2);
            this.mEditText.setId(R.id.qmui_dialog_edit_input);
            if (!QMUILangHelper.isNullOrEmpty(this.mDefaultText)) {
                this.mEditText.setText(this.mDefaultText);
            }
            this.mRightImageView = new ImageView(context);
            this.mRightImageView.setId(R.id.qmui_dialog_edit_right_icon);
            this.mRightImageView.setVisibility(8);
            this.mMainLayout = new RelativeLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.topMargin = this.mEditText.getPaddingTop();
            layoutParams.leftMargin = this.mEditText.getPaddingLeft();
            layoutParams.rightMargin = this.mEditText.getPaddingRight();
            layoutParams.bottomMargin = this.mEditText.getPaddingBottom();
            this.mMainLayout.setBackgroundResource(R.drawable.qmui_edittext_bg_border_bottom);
            this.mMainLayout.setLayoutParams(layoutParams);
            if (this.mTransformationMethod != null) {
                this.mEditText.setTransformationMethod(this.mTransformationMethod);
            } else {
                this.mEditText.setInputType(this.mInputType);
            }
            this.mEditText.setBackgroundResource(0);
            this.mEditText.setPadding(0, 0, 0, QMUIDisplayHelper.dpToPx(5));
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams2.addRule(0, this.mRightImageView.getId());
            layoutParams2.addRule(15, -1);
            if (this.mPlaceholder != null) {
                this.mEditText.setHint(this.mPlaceholder);
            }
            this.mMainLayout.addView(this.mEditText, createEditTextLayoutParams());
            this.mMainLayout.addView(this.mRightImageView, createRightIconLayoutParams());
            viewGroup.addView(this.mMainLayout);
        }

        public EditTextDialogBuilder setDefaultText(CharSequence charSequence) {
            this.mDefaultText = charSequence;
            return this;
        }

        public EditTextDialogBuilder setInputType(int i) {
            this.mInputType = i;
            return this;
        }

        public EditTextDialogBuilder setPlaceholder(int i) {
            return setPlaceholder(getBaseContext().getResources().getString(i));
        }

        public EditTextDialogBuilder setPlaceholder(String str) {
            this.mPlaceholder = str;
            return this;
        }

        public EditTextDialogBuilder setTransformationMethod(TransformationMethod transformationMethod) {
            this.mTransformationMethod = transformationMethod;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class MenuBaseDialogBuilder<T extends QMUIDialogBuilder> extends QMUIDialogBuilder<T> {
        protected QMUIWrapContentScrollView mContentScrollView;
        protected LinearLayout mMenuItemContainer;
        protected LinearLayout.LayoutParams mMenuItemLp;
        protected ArrayList<QMUIDialogMenuItemView> mMenuItemViews;

        public MenuBaseDialogBuilder(Context context) {
            super(context);
            this.mMenuItemViews = new ArrayList<>();
        }

        public T addItem(QMUIDialogMenuItemView qMUIDialogMenuItemView, final DialogInterface.OnClickListener onClickListener) {
            qMUIDialogMenuItemView.setMenuIndex(this.mMenuItemViews.size());
            qMUIDialogMenuItemView.setListener(new QMUIDialogMenuItemView.MenuItemViewListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder.1
                @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView.MenuItemViewListener
                public void onClick(int i) {
                    MenuBaseDialogBuilder.this.onItemClick(i);
                    if (onClickListener != null) {
                        onClickListener.onClick(MenuBaseDialogBuilder.this.mDialog, i);
                    }
                }
            });
            this.mMenuItemViews.add(qMUIDialogMenuItemView);
            return this;
        }

        public void clear() {
            this.mMenuItemViews.clear();
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            this.mMenuItemContainer = new LinearLayout(context);
            this.mMenuItemContainer.setOrientation(1);
            this.mMenuItemContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.QMUIDialogMenuContainerStyleDef, R.attr.qmui_dialog_menu_container_style, 0);
            int indexCount = obtainStyledAttributes.getIndexCount();
            int i = -1;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            for (int i7 = 0; i7 < indexCount; i7++) {
                int index = obtainStyledAttributes.getIndex(i7);
                if (index == R.styleable.QMUIDialogMenuContainerStyleDef_android_paddingTop) {
                    i2 = obtainStyledAttributes.getDimensionPixelSize(index, i2);
                } else if (index == R.styleable.QMUIDialogMenuContainerStyleDef_android_paddingBottom) {
                    i4 = obtainStyledAttributes.getDimensionPixelSize(index, i4);
                } else if (index == R.styleable.QMUIDialogMenuContainerStyleDef_qmui_dialog_menu_container_single_padding_vertical) {
                    i3 = obtainStyledAttributes.getDimensionPixelSize(index, i3);
                } else if (index == R.styleable.QMUIDialogMenuContainerStyleDef_qmui_dialog_menu_container_padding_top_when_title_exist) {
                    i5 = obtainStyledAttributes.getDimensionPixelSize(index, i5);
                } else if (index == R.styleable.QMUIDialogMenuContainerStyleDef_qmui_dialog_menu_container_padding_bottom_when_action_exist) {
                    i6 = obtainStyledAttributes.getDimensionPixelSize(index, i6);
                } else if (index == R.styleable.QMUIDialogMenuContainerStyleDef_qmui_dialog_menu_item_height) {
                    i = obtainStyledAttributes.getDimensionPixelSize(index, i);
                }
            }
            obtainStyledAttributes.recycle();
            this.mMenuItemLp = new LinearLayout.LayoutParams(-1, i);
            this.mMenuItemLp.gravity = 16;
            if (this.mMenuItemViews.size() == 1) {
                i2 = i3;
            } else {
                i3 = i4;
            }
            if (!hasTitle()) {
                i5 = i2;
            }
            if (this.mActions.size() <= 0) {
                i6 = i3;
            }
            this.mMenuItemContainer.setPadding(0, i5, 0, i6);
            Iterator<QMUIDialogMenuItemView> it = this.mMenuItemViews.iterator();
            while (it.hasNext()) {
                this.mMenuItemContainer.addView(it.next(), this.mMenuItemLp);
            }
            this.mContentScrollView = new QMUIWrapContentScrollView(context);
            this.mContentScrollView.setMaxHeight(getContentAreaMaxHeight());
            this.mContentScrollView.addView(this.mMenuItemContainer);
            this.mContentScrollView.setVerticalScrollBarEnabled(false);
            viewGroup.addView(this.mContentScrollView);
        }

        protected void onItemClick(int i) {
        }
    }

    /* loaded from: classes2.dex */
    public static class MenuDialogBuilder extends MenuBaseDialogBuilder<MenuDialogBuilder> {
        public MenuDialogBuilder(Context context) {
            super(context);
        }

        public MenuDialogBuilder addItem(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            addItem(new QMUIDialogMenuItemView.TextItemView(getBaseContext(), charSequence), onClickListener);
            return this;
        }

        public MenuDialogBuilder addItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            for (CharSequence charSequence : charSequenceArr) {
                addItem(new QMUIDialogMenuItemView.TextItemView(getBaseContext(), charSequence), onClickListener);
            }
            return this;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }
    }

    /* loaded from: classes2.dex */
    public static class MessageDialogBuilder extends QMUIDialogBuilder<MessageDialogBuilder> {
        protected CharSequence mMessage;
        private QMUIWrapContentScrollView mScrollContainer;
        private QMUISpanTouchFixTextView mTextView;

        public MessageDialogBuilder(Context context) {
            super(context);
        }

        public static void assignMessageTvWithAttr(TextView textView, boolean z, int i) {
            QMUIResHelper.assignTextViewWithAttr(textView, i);
            if (z) {
                return;
            }
            TypedArray obtainStyledAttributes = textView.getContext().obtainStyledAttributes(null, R.styleable.QMUIDialogMessageTvCustomDef, i, 0);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i2 = 0; i2 < indexCount; i2++) {
                int index = obtainStyledAttributes.getIndex(i2);
                if (index == R.styleable.QMUIDialogMessageTvCustomDef_qmui_paddingTopWhenNotTitle) {
                    textView.setPadding(textView.getPaddingLeft(), obtainStyledAttributes.getDimensionPixelSize(index, textView.getPaddingTop()), textView.getPaddingRight(), textView.getPaddingBottom());
                }
            }
            obtainStyledAttributes.recycle();
        }

        public QMUISpanTouchFixTextView getTextView() {
            return this.mTextView;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onConfigTitleView(TextView textView) {
            super.onConfigTitleView(textView);
            if (this.mMessage == null || this.mMessage.length() == 0) {
                TypedArray obtainStyledAttributes = textView.getContext().obtainStyledAttributes(null, R.styleable.QMUIDialogTitleTvCustomDef, R.attr.qmui_dialog_title_style, 0);
                int indexCount = obtainStyledAttributes.getIndexCount();
                for (int i = 0; i < indexCount; i++) {
                    int index = obtainStyledAttributes.getIndex(i);
                    if (index == R.styleable.QMUIDialogTitleTvCustomDef_qmui_paddingBottomWhenNotContent) {
                        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), obtainStyledAttributes.getDimensionPixelSize(index, textView.getPaddingBottom()));
                    }
                }
                obtainStyledAttributes.recycle();
            }
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            if (this.mMessage == null || this.mMessage.length() == 0) {
                return;
            }
            this.mTextView = new QMUISpanTouchFixTextView(context);
            assignMessageTvWithAttr(this.mTextView, hasTitle(), R.attr.qmui_dialog_message_content_style);
            this.mTextView.setText(this.mMessage);
            this.mTextView.setMovementMethodDefault();
            this.mScrollContainer = new QMUIWrapContentScrollView(context);
            this.mScrollContainer.setMaxHeight(getContentAreaMaxHeight());
            this.mScrollContainer.setVerticalScrollBarEnabled(false);
            this.mScrollContainer.addView(this.mTextView);
            viewGroup.addView(this.mScrollContainer);
        }

        public MessageDialogBuilder setMessage(int i) {
            return setMessage(getBaseContext().getResources().getString(i));
        }

        public MessageDialogBuilder setMessage(CharSequence charSequence) {
            this.mMessage = charSequence;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class MultiCheckableDialogBuilder extends MenuBaseDialogBuilder<MultiCheckableDialogBuilder> {
        private int mCheckedItems;

        public MultiCheckableDialogBuilder(Context context) {
            super(context);
        }

        public MultiCheckableDialogBuilder addItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            for (CharSequence charSequence : charSequenceArr) {
                addItem(new QMUIDialogMenuItemView.CheckItemView(getBaseContext(), true, charSequence), onClickListener);
            }
            return this;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        protected boolean existCheckedItem() {
            return getCheckedItemRecord() <= 0;
        }

        public int[] getCheckedItemIndexes() {
            ArrayList arrayList = new ArrayList();
            int size = this.mMenuItemViews.size();
            for (int i = 0; i < size; i++) {
                QMUIDialogMenuItemView qMUIDialogMenuItemView = this.mMenuItemViews.get(i);
                if (qMUIDialogMenuItemView.isChecked()) {
                    arrayList.add(Integer.valueOf(qMUIDialogMenuItemView.getMenuIndex()));
                }
            }
            int[] iArr = new int[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                iArr[i2] = ((Integer) arrayList.get(i2)).intValue();
            }
            return iArr;
        }

        public int getCheckedItemRecord() {
            int size = this.mMenuItemViews.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                QMUIDialogMenuItemView qMUIDialogMenuItemView = this.mMenuItemViews.get(i2);
                if (qMUIDialogMenuItemView.isChecked()) {
                    i += 2 << qMUIDialogMenuItemView.getMenuIndex();
                }
            }
            this.mCheckedItems = i;
            return i;
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder, com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder
        protected void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
            super.onCreateContent(qMUIDialog, viewGroup, context);
            for (int i = 0; i < this.mMenuItemViews.size(); i++) {
                int i2 = 2 << i;
                this.mMenuItemViews.get(i).setChecked((this.mCheckedItems & i2) == i2);
            }
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuBaseDialogBuilder
        protected void onItemClick(int i) {
            this.mMenuItemViews.get(i).setChecked(!r2.isChecked());
        }

        public MultiCheckableDialogBuilder setCheckedItems(int i) {
            this.mCheckedItems = i;
            return this;
        }

        public MultiCheckableDialogBuilder setCheckedItems(int[] iArr) {
            int i = 0;
            if (iArr != null && iArr.length > 0) {
                int length = iArr.length;
                int i2 = 0;
                while (i < length) {
                    i2 += 2 << iArr[i];
                    i++;
                }
                i = i2;
            }
            return setCheckedItems(i);
        }
    }

    public QMUIDialog(Context context) {
        this(context, R.style.QMUI_Dialog);
    }

    public QMUIDialog(Context context, int i) {
        super(context, i);
        this.mCancelable = true;
        this.mCanceledOnTouchOutside = true;
        this.mBaseContext = context;
        init();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    private void initDialog() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.gravity = 17;
        window.setAttributes(attributes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelOutSide() {
        if (this.mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
            cancel();
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initDialog();
    }

    @Override // android.app.Dialog
    public void setCancelable(boolean z) {
        super.setCancelable(z);
        this.mCancelable = z;
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
        if (z && !this.mCancelable) {
            this.mCancelable = true;
        }
        this.mCanceledOnTouchOutside = z;
        this.mCanceledOnTouchOutsideSet = true;
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!this.mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                this.mCanceledOnTouchOutside = true;
            } else {
                TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{android.R.attr.windowCloseOnTouchOutside});
                this.mCanceledOnTouchOutside = obtainStyledAttributes.getBoolean(0, true);
                obtainStyledAttributes.recycle();
            }
            this.mCanceledOnTouchOutsideSet = true;
        }
        return this.mCanceledOnTouchOutside;
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
    }

    public void showWithImmersiveCheck() {
        if (this.mBaseContext instanceof Activity) {
            showWithImmersiveCheck((Activity) this.mBaseContext);
        } else {
            super.show();
        }
    }

    public void showWithImmersiveCheck(Activity activity) {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility() & 1024;
        if (systemUiVisibility != 1024 && systemUiVisibility != 1024) {
            super.show();
            return;
        }
        window.setFlags(8, 8);
        window.getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        super.show();
        window.clearFlags(8);
    }
}
