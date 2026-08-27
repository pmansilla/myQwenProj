package com.qmuiteam.qmui.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class QMUIDialogBuilder<T extends QMUIDialogBuilder> {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    protected QMUILinearLayout mActionContainer;
    protected View mAnchorBottomView;
    protected View mAnchorTopView;
    private Context mContext;
    protected QMUIDialog mDialog;
    protected QMUIDialogView mDialogView;
    private QMUIDialogView.OnDecorationListener mOnDecorationListener;
    protected LinearLayout mRootView;
    protected String mTitle;
    protected TextView mTitleView;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    protected List<QMUIDialogAction> mActions = new ArrayList();
    private int mContentAreaMaxHeight = -1;
    private int mActionContainerOrientation = 0;
    private boolean mChangeAlphaForPressOrDisable = true;
    private int mActionDividerThickness = 0;
    private int mActionDividerColorRes = R.color.qmui_config_color_separator;
    private int mActionDividerInsetStart = 0;
    private int mActionDividerInsetEnd = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Orientation {
    }

    public QMUIDialogBuilder(Context context) {
        this.mContext = context;
    }

    private View createActionContainerSpace(Context context) {
        Space space = new Space(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        layoutParams.weight = 1.0f;
        space.setLayoutParams(layoutParams);
        return space;
    }

    public T addAction(int i, int i2, int i3, QMUIDialogAction.ActionListener actionListener) {
        return addAction(i, this.mContext.getResources().getString(i2), i3, actionListener);
    }

    public T addAction(int i, int i2, QMUIDialogAction.ActionListener actionListener) {
        return addAction(i, i2, 1, actionListener);
    }

    public T addAction(int i, QMUIDialogAction.ActionListener actionListener) {
        return addAction(0, i, actionListener);
    }

    public T addAction(int i, CharSequence charSequence, int i2, QMUIDialogAction.ActionListener actionListener) {
        this.mActions.add(new QMUIDialogAction(this.mContext, i, charSequence, i2, actionListener));
        return this;
    }

    public T addAction(int i, CharSequence charSequence, QMUIDialogAction.ActionListener actionListener) {
        return addAction(i, charSequence, 1, actionListener);
    }

    public T addAction(@Nullable QMUIDialogAction qMUIDialogAction) {
        if (qMUIDialogAction != null) {
            this.mActions.add(qMUIDialogAction);
        }
        return this;
    }

    public T addAction(CharSequence charSequence, QMUIDialogAction.ActionListener actionListener) {
        return addAction(0, charSequence, 1, actionListener);
    }

    public QMUIDialog create() {
        return create(R.style.QMUI_Dialog);
    }

    @SuppressLint({"InflateParams"})
    public QMUIDialog create(@StyleRes int i) {
        this.mDialog = new QMUIDialog(this.mContext, i);
        Context context = this.mDialog.getContext();
        this.mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.qmui_dialog_layout, (ViewGroup) null);
        this.mDialogView = (QMUIDialogView) this.mRootView.findViewById(R.id.dialog);
        this.mDialogView.setOnDecorationListener(this.mOnDecorationListener);
        this.mAnchorTopView = this.mRootView.findViewById(R.id.anchor_top);
        this.mAnchorBottomView = this.mRootView.findViewById(R.id.anchor_bottom);
        onCreateTitle(this.mDialog, this.mDialogView, context);
        onCreateContent(this.mDialog, this.mDialogView, context);
        onCreateHandlerBar(this.mDialog, this.mDialogView, context);
        this.mDialog.addContentView(this.mRootView, new ViewGroup.LayoutParams(-1, -2));
        this.mDialog.setCancelable(this.mCancelable);
        this.mDialog.setCanceledOnTouchOutside(this.mCanceledOnTouchOutside);
        onAfter(this.mDialog, this.mRootView, context);
        return this.mDialog;
    }

    public View getAnchorBottomView() {
        return this.mAnchorBottomView;
    }

    public View getAnchorTopView() {
        return this.mAnchorTopView;
    }

    public Context getBaseContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getContentAreaMaxHeight() {
        if (this.mContentAreaMaxHeight != -1) {
            return this.mContentAreaMaxHeight;
        }
        double screenHeight = QMUIDisplayHelper.getScreenHeight(this.mContext);
        Double.isNaN(screenHeight);
        return ((int) (screenHeight * 0.85d)) - QMUIDisplayHelper.dp2px(this.mContext, 100);
    }

    public List<QMUIDialogAction> getPositiveAction() {
        ArrayList arrayList = new ArrayList();
        for (QMUIDialogAction qMUIDialogAction : this.mActions) {
            if (qMUIDialogAction.getActionProp() == 0) {
                arrayList.add(qMUIDialogAction);
            }
        }
        return arrayList;
    }

    public TextView getTitleView() {
        return this.mTitleView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasTitle() {
        return (this.mTitle == null || this.mTitle.length() == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAfter(QMUIDialog qMUIDialog, LinearLayout linearLayout, Context context) {
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                QMUIDialogBuilder.this.mDialog.cancelOutSide();
            }
        };
        this.mAnchorBottomView.setOnClickListener(onClickListener);
        this.mAnchorTopView.setOnClickListener(onClickListener);
        this.mRootView.setOnClickListener(onClickListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConfigTitleView(TextView textView) {
    }

    protected abstract void onCreateContent(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context);

    /* JADX WARN: Code restructure failed: missing block: B:74:0x005f, code lost:
    
        if (r10 == 3) goto L28;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onCreateHandlerBar(com.qmuiteam.qmui.widget.dialog.QMUIDialog r17, android.view.ViewGroup r18, android.content.Context r19) {
        /*
            Method dump skipped, instructions count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder.onCreateHandlerBar(com.qmuiteam.qmui.widget.dialog.QMUIDialog, android.view.ViewGroup, android.content.Context):void");
    }

    protected void onCreateTitle(QMUIDialog qMUIDialog, ViewGroup viewGroup, Context context) {
        if (hasTitle()) {
            this.mTitleView = new TextView(context);
            this.mTitleView.setText(this.mTitle);
            QMUIResHelper.assignTextViewWithAttr(this.mTitleView, R.attr.qmui_dialog_title_style);
            onConfigTitleView(this.mTitleView);
            this.mTitleView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            viewGroup.addView(this.mTitleView);
        }
    }

    public T setActionContainerOrientation(int i) {
        this.mActionContainerOrientation = i;
        return this;
    }

    public T setActionDivider(int i, int i2, int i3, int i4) {
        this.mActionDividerThickness = i;
        this.mActionDividerColorRes = i2;
        this.mActionDividerInsetStart = i3;
        this.mActionDividerInsetEnd = i4;
        return this;
    }

    public T setCancelable(boolean z) {
        this.mCancelable = z;
        return this;
    }

    public T setCanceledOnTouchOutside(boolean z) {
        this.mCanceledOnTouchOutside = z;
        return this;
    }

    public T setChangeAlphaForPressOrDisable(boolean z) {
        this.mChangeAlphaForPressOrDisable = z;
        return this;
    }

    public T setContentAreaMaxHeight(int i) {
        this.mContentAreaMaxHeight = i;
        return this;
    }

    public T setOnDecorationListener(QMUIDialogView.OnDecorationListener onDecorationListener) {
        this.mOnDecorationListener = onDecorationListener;
        return this;
    }

    public T setTitle(int i) {
        return setTitle(this.mContext.getResources().getString(i));
    }

    public T setTitle(String str) {
        if (str != null && str.length() > 0) {
            this.mTitle = str + this.mContext.getString(R.string.qmui_tool_fixellipsize);
        }
        return this;
    }

    public QMUIDialog show() {
        QMUIDialog create = create();
        create.show();
        return create;
    }
}
