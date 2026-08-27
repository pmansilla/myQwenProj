package com.qmuiteam.qmui.widget.grouplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class QMUIGroupListView extends LinearLayout {
    public static final int SEPARATOR_STYLE_NONE = 1;
    public static final int SEPARATOR_STYLE_NORMAL = 0;
    private SparseArray<Section> mSections;
    private int mSeparatorStyle;

    /* loaded from: classes2.dex */
    public static class Section {
        private Context mContext;
        private QMUIGroupListSectionHeaderFooterView mDescriptionView;
        private QMUIGroupListSectionHeaderFooterView mTitleView;
        private boolean mUseDefaultTitleIfNone;
        private boolean mUseTitleViewForSectionSpace = true;
        private int mSeparatorDrawableForSingle = 0;
        private int mSeparatorDrawableForTop = 0;
        private int mSeparatorDrawableForBottom = 0;
        private int mSeparatorDrawableForMiddle = 0;
        private SparseArray<QMUICommonListItemView> mItemViews = new SparseArray<>();

        public Section(Context context) {
            this.mContext = context;
        }

        public Section addItemView(QMUICommonListItemView qMUICommonListItemView, View.OnClickListener onClickListener) {
            return addItemView(qMUICommonListItemView, onClickListener, null);
        }

        public Section addItemView(final QMUICommonListItemView qMUICommonListItemView, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
            if (qMUICommonListItemView.getAccessoryType() == 2) {
                qMUICommonListItemView.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView.Section.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        qMUICommonListItemView.getSwitch().toggle();
                    }
                });
            } else if (onClickListener != null) {
                qMUICommonListItemView.setOnClickListener(onClickListener);
            }
            if (onLongClickListener != null) {
                qMUICommonListItemView.setOnLongClickListener(onLongClickListener);
            }
            this.mItemViews.append(this.mItemViews.size(), qMUICommonListItemView);
            return this;
        }

        public void addTo(QMUIGroupListView qMUIGroupListView) {
            if (this.mTitleView == null) {
                if (this.mUseDefaultTitleIfNone) {
                    setTitle("Section " + qMUIGroupListView.getSectionCount());
                } else if (this.mUseTitleViewForSectionSpace) {
                    setTitle("");
                }
            }
            if (this.mTitleView != null) {
                qMUIGroupListView.addView(this.mTitleView);
            }
            if (qMUIGroupListView.getSeparatorStyle() == 0) {
                if (this.mSeparatorDrawableForSingle == 0) {
                    this.mSeparatorDrawableForSingle = R.drawable.qmui_s_list_item_bg_with_border_double;
                }
                if (this.mSeparatorDrawableForTop == 0) {
                    this.mSeparatorDrawableForTop = R.drawable.qmui_s_list_item_bg_with_border_double;
                }
                if (this.mSeparatorDrawableForBottom == 0) {
                    this.mSeparatorDrawableForBottom = R.drawable.qmui_s_list_item_bg_with_border_bottom;
                }
                if (this.mSeparatorDrawableForMiddle == 0) {
                    this.mSeparatorDrawableForMiddle = R.drawable.qmui_s_list_item_bg_with_border_bottom;
                }
            }
            int size = this.mItemViews.size();
            int i = 0;
            while (i < size) {
                QMUICommonListItemView qMUICommonListItemView = this.mItemViews.get(i);
                QMUIViewHelper.setBackgroundKeepingPadding(qMUICommonListItemView, qMUIGroupListView.getSeparatorStyle() == 0 ? size == 1 ? this.mSeparatorDrawableForSingle : i == 0 ? this.mSeparatorDrawableForTop : i == size + (-1) ? this.mSeparatorDrawableForBottom : this.mSeparatorDrawableForMiddle : R.drawable.qmui_s_list_item_bg_with_border_none);
                qMUIGroupListView.addView(qMUICommonListItemView);
                i++;
            }
            if (this.mDescriptionView != null) {
                qMUIGroupListView.addView(this.mDescriptionView);
            }
            qMUIGroupListView.addSection(this);
        }

        public QMUIGroupListSectionHeaderFooterView createSectionFooter(CharSequence charSequence) {
            return new QMUIGroupListSectionHeaderFooterView(this.mContext, charSequence, true);
        }

        public QMUIGroupListSectionHeaderFooterView createSectionHeader(CharSequence charSequence) {
            return new QMUIGroupListSectionHeaderFooterView(this.mContext, charSequence);
        }

        public void removeFrom(QMUIGroupListView qMUIGroupListView) {
            if (this.mTitleView != null && this.mTitleView.getParent() == qMUIGroupListView) {
                qMUIGroupListView.removeView(this.mTitleView);
            }
            for (int i = 0; i < this.mItemViews.size(); i++) {
                qMUIGroupListView.removeView(this.mItemViews.get(i));
            }
            if (this.mDescriptionView != null && this.mDescriptionView.getParent() == qMUIGroupListView) {
                qMUIGroupListView.removeView(this.mDescriptionView);
            }
            qMUIGroupListView.removeSection(this);
        }

        public Section setDescription(CharSequence charSequence) {
            this.mDescriptionView = createSectionFooter(charSequence);
            return this;
        }

        public Section setSeparatorDrawableRes(int i) {
            this.mSeparatorDrawableForMiddle = i;
            return this;
        }

        public Section setSeparatorDrawableRes(int i, int i2, int i3, int i4) {
            this.mSeparatorDrawableForSingle = i;
            this.mSeparatorDrawableForTop = i2;
            this.mSeparatorDrawableForBottom = i3;
            this.mSeparatorDrawableForMiddle = i4;
            return this;
        }

        public Section setTitle(CharSequence charSequence) {
            this.mTitleView = createSectionHeader(charSequence);
            return this;
        }

        public Section setUseDefaultTitleIfNone(boolean z) {
            this.mUseDefaultTitleIfNone = z;
            return this;
        }

        public Section setUseTitleViewForSectionSpace(boolean z) {
            this.mUseTitleViewForSectionSpace = z;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SeparatorStyle {
    }

    public QMUIGroupListView(Context context) {
        this(context, null, R.attr.QMUIGroupListViewStyle);
    }

    public QMUIGroupListView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUIGroupListViewStyle);
    }

    public QMUIGroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIGroupListView, i, 0);
        this.mSeparatorStyle = obtainStyledAttributes.getInt(R.styleable.QMUIGroupListView_separatorStyle, 0);
        obtainStyledAttributes.recycle();
        this.mSections = new SparseArray<>();
        setOrientation(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSection(Section section) {
        this.mSections.append(this.mSections.size(), section);
    }

    public static Section newSection(Context context) {
        return new Section(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSection(Section section) {
        for (int i = 0; i < this.mSections.size(); i++) {
            if (this.mSections.valueAt(i) == section) {
                this.mSections.remove(i);
            }
        }
    }

    public QMUICommonListItemView createItemView(int i) {
        return createItemView(null, null, null, i, 0);
    }

    public QMUICommonListItemView createItemView(Drawable drawable, CharSequence charSequence, String str, int i, int i2) {
        return i == 0 ? createItemView(drawable, charSequence, str, i, i2, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_list_item_height_higher)) : createItemView(drawable, charSequence, str, i, i2, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_list_item_height));
    }

    public QMUICommonListItemView createItemView(Drawable drawable, CharSequence charSequence, String str, int i, int i2, int i3) {
        QMUICommonListItemView qMUICommonListItemView = new QMUICommonListItemView(getContext());
        qMUICommonListItemView.setOrientation(i);
        qMUICommonListItemView.setLayoutParams(new LinearLayout.LayoutParams(-1, i3));
        qMUICommonListItemView.setImageDrawable(drawable);
        qMUICommonListItemView.setText(charSequence);
        qMUICommonListItemView.setDetailText(str);
        qMUICommonListItemView.setAccessoryType(i2);
        return qMUICommonListItemView;
    }

    public QMUICommonListItemView createItemView(CharSequence charSequence) {
        return createItemView(null, charSequence, null, 1, 0);
    }

    public Section getSection(int i) {
        return this.mSections.get(i);
    }

    public int getSectionCount() {
        return this.mSections.size();
    }

    public int getSeparatorStyle() {
        return this.mSeparatorStyle;
    }

    public void setSeparatorStyle(int i) {
        this.mSeparatorStyle = i;
    }
}
