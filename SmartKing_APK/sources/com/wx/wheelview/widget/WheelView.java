package com.wx.wheelview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.adapter.BaseWheelAdapter;
import com.wx.wheelview.adapter.SimpleWheelAdapter;
import com.wx.wheelview.common.WheelViewException;
import com.wx.wheelview.graphics.DrawableFactory;
import com.wx.wheelview.util.WheelUtils;
import java.util.HashMap;
import java.util.List;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* loaded from: classes2.dex */
public class WheelView<T> extends ListView implements IWheelView<T> {
    private boolean mClickable;
    private int mCurrentPositon;
    private int mExtraMargin;
    private String mExtraText;
    private int mExtraTextColor;
    private int mExtraTextSize;
    private Handler mHandler;
    private int mItemH;
    private HashMap<String, List<T>> mJoinMap;
    private WheelView mJoinWheelView;
    private List<T> mList;
    private boolean mLoop;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AbsListView.OnScrollListener mOnScrollListener;
    private OnWheelItemClickListener<T> mOnWheelItemClickListener;
    private OnWheelItemSelectedListener<T> mOnWheelItemSelectedListener;
    private int mSelection;
    private Skin mSkin;
    private WheelViewStyle mStyle;
    private Paint mTextPaint;
    private View.OnTouchListener mTouchListener;
    private BaseWheelAdapter<T> mWheelAdapter;
    private int mWheelSize;

    /* loaded from: classes2.dex */
    public interface OnWheelItemClickListener<T> {
        void onItemClick(int i, T t);
    }

    /* loaded from: classes2.dex */
    public interface OnWheelItemSelectedListener<T> {
        void onItemSelected(int i, T t);
    }

    /* loaded from: classes2.dex */
    public enum Skin {
        Common,
        Holo,
        None
    }

    /* loaded from: classes2.dex */
    public static class WheelViewStyle {
        public int backgroundColor;
        public int holoBorderColor;
        public int holoBorderWidth;
        public int selectedTextColor;
        public int selectedTextSize;
        public float selectedTextZoom;
        public float textAlpha;
        public int textColor;
        public int textSize;

        public WheelViewStyle() {
            this.backgroundColor = -1;
            this.holoBorderColor = -1;
            this.holoBorderWidth = -1;
            this.textColor = -1;
            this.selectedTextColor = -1;
            this.textSize = -1;
            this.selectedTextSize = -1;
            this.textAlpha = -1.0f;
            this.selectedTextZoom = -1.0f;
        }

        public WheelViewStyle(WheelViewStyle wheelViewStyle) {
            this.backgroundColor = -1;
            this.holoBorderColor = -1;
            this.holoBorderWidth = -1;
            this.textColor = -1;
            this.selectedTextColor = -1;
            this.textSize = -1;
            this.selectedTextSize = -1;
            this.textAlpha = -1.0f;
            this.selectedTextZoom = -1.0f;
            this.backgroundColor = wheelViewStyle.backgroundColor;
            this.holoBorderColor = wheelViewStyle.holoBorderColor;
            this.holoBorderWidth = wheelViewStyle.holoBorderWidth;
            this.textColor = wheelViewStyle.textColor;
            this.selectedTextColor = wheelViewStyle.selectedTextColor;
            this.textSize = wheelViewStyle.textSize;
            this.selectedTextSize = wheelViewStyle.selectedTextSize;
            this.textAlpha = wheelViewStyle.textAlpha;
            this.selectedTextZoom = wheelViewStyle.selectedTextZoom;
        }
    }

    public WheelView(Context context) {
        super(context);
        this.mItemH = 0;
        this.mWheelSize = 3;
        this.mLoop = false;
        this.mList = null;
        this.mCurrentPositon = -1;
        this.mSelection = 0;
        this.mClickable = false;
        this.mSkin = Skin.None;
        this.mHandler = new Handler() { // from class: com.wx.wheelview.widget.WheelView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 256) {
                    if (WheelView.this.mOnWheelItemSelectedListener != null) {
                        WheelView.this.mOnWheelItemSelectedListener.onItemSelected(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                    }
                    if (WheelView.this.mJoinWheelView != null) {
                        if (WheelView.this.mJoinMap.isEmpty()) {
                            throw new WheelViewException("JoinList is error.");
                        }
                        WheelView.this.mJoinWheelView.resetDataFromTop((List) WheelView.this.mJoinMap.get(WheelView.this.mList.get(WheelView.this.getCurrentPosition())));
                    }
                }
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.wx.wheelview.widget.WheelView.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (WheelView.this.mOnWheelItemClickListener != null) {
                    WheelView.this.mOnWheelItemClickListener.onItemClick(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.wx.wheelview.widget.WheelView.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.wx.wheelview.widget.WheelView.4
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i2 != 0) {
                    WheelView.this.refreshCurrentPosition(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                View childAt;
                if (i != 0 || (childAt = WheelView.this.getChildAt(0)) == null) {
                    return;
                }
                float y = childAt.getY();
                if (y == 0.0f || WheelView.this.mItemH == 0) {
                    return;
                }
                if (Math.abs(y) < WheelView.this.mItemH / 2) {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(y), 50);
                } else {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(WheelView.this.mItemH + y), 50);
                }
            }
        };
        init();
    }

    public WheelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mItemH = 0;
        this.mWheelSize = 3;
        this.mLoop = false;
        this.mList = null;
        this.mCurrentPositon = -1;
        this.mSelection = 0;
        this.mClickable = false;
        this.mSkin = Skin.None;
        this.mHandler = new Handler() { // from class: com.wx.wheelview.widget.WheelView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 256) {
                    if (WheelView.this.mOnWheelItemSelectedListener != null) {
                        WheelView.this.mOnWheelItemSelectedListener.onItemSelected(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                    }
                    if (WheelView.this.mJoinWheelView != null) {
                        if (WheelView.this.mJoinMap.isEmpty()) {
                            throw new WheelViewException("JoinList is error.");
                        }
                        WheelView.this.mJoinWheelView.resetDataFromTop((List) WheelView.this.mJoinMap.get(WheelView.this.mList.get(WheelView.this.getCurrentPosition())));
                    }
                }
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.wx.wheelview.widget.WheelView.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (WheelView.this.mOnWheelItemClickListener != null) {
                    WheelView.this.mOnWheelItemClickListener.onItemClick(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.wx.wheelview.widget.WheelView.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.wx.wheelview.widget.WheelView.4
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i2 != 0) {
                    WheelView.this.refreshCurrentPosition(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                View childAt;
                if (i != 0 || (childAt = WheelView.this.getChildAt(0)) == null) {
                    return;
                }
                float y = childAt.getY();
                if (y == 0.0f || WheelView.this.mItemH == 0) {
                    return;
                }
                if (Math.abs(y) < WheelView.this.mItemH / 2) {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(y), 50);
                } else {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(WheelView.this.mItemH + y), 50);
                }
            }
        };
        init();
    }

    public WheelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mItemH = 0;
        this.mWheelSize = 3;
        this.mLoop = false;
        this.mList = null;
        this.mCurrentPositon = -1;
        this.mSelection = 0;
        this.mClickable = false;
        this.mSkin = Skin.None;
        this.mHandler = new Handler() { // from class: com.wx.wheelview.widget.WheelView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 256) {
                    if (WheelView.this.mOnWheelItemSelectedListener != null) {
                        WheelView.this.mOnWheelItemSelectedListener.onItemSelected(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                    }
                    if (WheelView.this.mJoinWheelView != null) {
                        if (WheelView.this.mJoinMap.isEmpty()) {
                            throw new WheelViewException("JoinList is error.");
                        }
                        WheelView.this.mJoinWheelView.resetDataFromTop((List) WheelView.this.mJoinMap.get(WheelView.this.mList.get(WheelView.this.getCurrentPosition())));
                    }
                }
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.wx.wheelview.widget.WheelView.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                if (WheelView.this.mOnWheelItemClickListener != null) {
                    WheelView.this.mOnWheelItemClickListener.onItemClick(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.wx.wheelview.widget.WheelView.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.wx.wheelview.widget.WheelView.4
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i2, int i22, int i3) {
                if (i22 != 0) {
                    WheelView.this.refreshCurrentPosition(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i2) {
                View childAt;
                if (i2 != 0 || (childAt = WheelView.this.getChildAt(0)) == null) {
                    return;
                }
                float y = childAt.getY();
                if (y == 0.0f || WheelView.this.mItemH == 0) {
                    return;
                }
                if (Math.abs(y) < WheelView.this.mItemH / 2) {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(y), 50);
                } else {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(WheelView.this.mItemH + y), 50);
                }
            }
        };
        init();
    }

    public WheelView(Context context, WheelViewStyle wheelViewStyle) {
        super(context);
        this.mItemH = 0;
        this.mWheelSize = 3;
        this.mLoop = false;
        this.mList = null;
        this.mCurrentPositon = -1;
        this.mSelection = 0;
        this.mClickable = false;
        this.mSkin = Skin.None;
        this.mHandler = new Handler() { // from class: com.wx.wheelview.widget.WheelView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 256) {
                    if (WheelView.this.mOnWheelItemSelectedListener != null) {
                        WheelView.this.mOnWheelItemSelectedListener.onItemSelected(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                    }
                    if (WheelView.this.mJoinWheelView != null) {
                        if (WheelView.this.mJoinMap.isEmpty()) {
                            throw new WheelViewException("JoinList is error.");
                        }
                        WheelView.this.mJoinWheelView.resetDataFromTop((List) WheelView.this.mJoinMap.get(WheelView.this.mList.get(WheelView.this.getCurrentPosition())));
                    }
                }
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener() { // from class: com.wx.wheelview.widget.WheelView.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                if (WheelView.this.mOnWheelItemClickListener != null) {
                    WheelView.this.mOnWheelItemClickListener.onItemClick(WheelView.this.getCurrentPosition(), WheelView.this.getSelectionItem());
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.wx.wheelview.widget.WheelView.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.wx.wheelview.widget.WheelView.4
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i2, int i22, int i3) {
                if (i22 != 0) {
                    WheelView.this.refreshCurrentPosition(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i2) {
                View childAt;
                if (i2 != 0 || (childAt = WheelView.this.getChildAt(0)) == null) {
                    return;
                }
                float y = childAt.getY();
                if (y == 0.0f || WheelView.this.mItemH == 0) {
                    return;
                }
                if (Math.abs(y) < WheelView.this.mItemH / 2) {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(y), 50);
                } else {
                    WheelView.this.smoothScrollBy(WheelView.this.getSmoothDistance(WheelView.this.mItemH + y), 50);
                }
            }
        };
        setStyle(wheelViewStyle);
        init();
    }

    private void addOnGlobalLayoutListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.wx.wheelview.widget.WheelView.5
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    WheelView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    WheelView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (WheelView.this.getChildCount() <= 0 || WheelView.this.mItemH != 0) {
                    return;
                }
                WheelView.this.mItemH = WheelView.this.getChildAt(0).getHeight();
                if (WheelView.this.mItemH == 0) {
                    throw new WheelViewException("wheel item is error.");
                }
                WheelView.this.getLayoutParams().height = WheelView.this.mItemH * WheelView.this.mWheelSize;
                WheelView.this.refreshVisibleItems(WheelView.this.getFirstVisiblePosition(), WheelView.this.getCurrentPosition() + (WheelView.this.mWheelSize / 2), WheelView.this.mWheelSize / 2);
                WheelView.this.setBackground();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRealPosition(int i) {
        if (WheelUtils.isEmpty(this.mList)) {
            return 0;
        }
        return this.mLoop ? (i + ((LockFreeTaskQueueCore.MAX_CAPACITY_MASK / this.mList.size()) * this.mList.size())) - (this.mWheelSize / 2) : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSmoothDistance(float f) {
        return Math.abs(f) <= 2.0f ? (int) f : Math.abs(f) < 12.0f ? f > 0.0f ? 2 : -2 : (int) (f / 6.0f);
    }

    private void init() {
        if (this.mStyle == null) {
            this.mStyle = new WheelViewStyle();
        }
        this.mTextPaint = new Paint(1);
        setTag("com.wx.wheelview");
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(0);
        setFadingEdgeLength(0);
        setOverScrollMode(2);
        setDividerHeight(0);
        setOnItemClickListener(this.mOnItemClickListener);
        setOnScrollListener(this.mOnScrollListener);
        setOnTouchListener(this.mTouchListener);
        if (Build.VERSION.SDK_INT >= 21) {
            setNestedScrollingEnabled(true);
        }
        addOnGlobalLayoutListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshCurrentPosition(boolean z) {
        if (getChildAt(0) == null || this.mItemH == 0) {
            return;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        if (this.mLoop && firstVisiblePosition == 0) {
            return;
        }
        int i = Math.abs(getChildAt(0).getY()) <= ((float) (this.mItemH / 2)) ? firstVisiblePosition : firstVisiblePosition + 1;
        refreshVisibleItems(firstVisiblePosition, (this.mWheelSize / 2) + i, this.mWheelSize / 2);
        if (this.mLoop) {
            i = (i + (this.mWheelSize / 2)) % getWheelCount();
        }
        if (i != this.mCurrentPositon || z) {
            this.mCurrentPositon = i;
            this.mWheelAdapter.setCurrentPosition(i);
            this.mHandler.removeMessages(256);
            this.mHandler.sendEmptyMessageDelayed(256, 300L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void refreshTextView(int r11, int r12, android.view.View r13, android.widget.TextView r14) {
        /*
            r10 = this;
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r1 = 1098907648(0x41800000, float:16.0)
            r2 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r3 = -1
            if (r12 != r11) goto L52
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.selectedTextColor
            if (r11 == r3) goto L15
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r2 = r11.selectedTextColor
        L13:
            r7 = r2
            goto L22
        L15:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.textColor
            if (r11 == r3) goto L20
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r2 = r11.textColor
            goto L13
        L20:
            r7 = -16777216(0xffffffffff000000, float:-1.7014118E38)
        L22:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.textSize
            if (r11 == r3) goto L2d
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.textSize
            float r1 = (float) r11
        L2d:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.selectedTextSize
            if (r11 == r3) goto L3a
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            int r11 = r11.selectedTextSize
            float r11 = (float) r11
            r8 = r11
            goto L49
        L3a:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            float r11 = r11.selectedTextZoom
            int r11 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1))
            if (r11 == 0) goto L48
            com.wx.wheelview.widget.WheelView$WheelViewStyle r11 = r10.mStyle
            float r11 = r11.selectedTextZoom
            float r1 = r1 * r11
        L48:
            r8 = r1
        L49:
            r9 = 1065353216(0x3f800000, float:1.0)
            r4 = r10
            r5 = r13
            r6 = r14
            r4.setTextView(r5, r6, r7, r8, r9)
            goto L93
        L52:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r4 = r10.mStyle
            int r4 = r4.textColor
            if (r4 == r3) goto L5e
            com.wx.wheelview.widget.WheelView$WheelViewStyle r2 = r10.mStyle
            int r2 = r2.textColor
            r7 = r2
            goto L60
        L5e:
            r7 = -16777216(0xffffffffff000000, float:-1.7014118E38)
        L60:
            com.wx.wheelview.widget.WheelView$WheelViewStyle r2 = r10.mStyle
            int r2 = r2.textSize
            if (r2 == r3) goto L6d
            com.wx.wheelview.widget.WheelView$WheelViewStyle r1 = r10.mStyle
            int r1 = r1.textSize
            float r1 = (float) r1
            r8 = r1
            goto L6f
        L6d:
            r8 = 1098907648(0x41800000, float:16.0)
        L6f:
            int r11 = r11 - r12
            int r11 = java.lang.Math.abs(r11)
            com.wx.wheelview.widget.WheelView$WheelViewStyle r12 = r10.mStyle
            float r12 = r12.textAlpha
            int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r12 == 0) goto L82
            com.wx.wheelview.widget.WheelView$WheelViewStyle r12 = r10.mStyle
            float r12 = r12.textAlpha
            double r0 = (double) r12
            goto L87
        L82:
            r0 = 4604480258916220928(0x3fe6666660000000, double:0.699999988079071)
        L87:
            double r11 = (double) r11
            double r11 = java.lang.Math.pow(r0, r11)
            float r9 = (float) r11
            r4 = r10
            r5 = r13
            r6 = r14
            r4.setTextView(r5, r6, r7, r8, r9)
        L93:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.wx.wheelview.widget.WheelView.refreshTextView(int, int, android.view.View, android.widget.TextView):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshVisibleItems(int i, int i2, int i3) {
        for (int i4 = i2 - i3; i4 <= i2 + i3; i4++) {
            View childAt = getChildAt(i4 - i);
            if (childAt != null) {
                if ((this.mWheelAdapter instanceof ArrayWheelAdapter) || (this.mWheelAdapter instanceof SimpleWheelAdapter)) {
                    refreshTextView(i4, i2, childAt, (TextView) childAt.findViewWithTag(101));
                } else {
                    TextView findTextView = WheelUtils.findTextView(childAt);
                    if (findTextView != null) {
                        refreshTextView(i4, i2, childAt, findTextView);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBackground() {
        Drawable createDrawable = DrawableFactory.createDrawable(this.mSkin, getWidth(), this.mItemH * this.mWheelSize, this.mStyle, this.mWheelSize, this.mItemH);
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(createDrawable);
        } else {
            setBackgroundDrawable(createDrawable);
        }
    }

    private void setTextView(View view, TextView textView, int i, float f, float f2) {
        textView.setTextColor(i);
        textView.setTextSize(1, f);
        view.setAlpha(f2);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (TextUtils.isEmpty(this.mExtraText)) {
            return;
        }
        Rect rect = new Rect(0, this.mItemH * (this.mWheelSize / 2), getWidth(), this.mItemH * ((this.mWheelSize / 2) + 1));
        this.mTextPaint.setTextSize(this.mExtraTextSize);
        this.mTextPaint.setColor(this.mExtraTextColor);
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        int i = (((rect.bottom + rect.top) - fontMetricsInt.bottom) - fontMetricsInt.top) / 2;
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(this.mExtraText, rect.centerX() + this.mExtraMargin, i, this.mTextPaint);
    }

    public int getCurrentPosition() {
        return this.mCurrentPositon;
    }

    public int getSelection() {
        return this.mSelection;
    }

    public T getSelectionItem() {
        int currentPosition = getCurrentPosition();
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (this.mList == null || this.mList.size() <= currentPosition) {
            return null;
        }
        return this.mList.get(currentPosition);
    }

    public Skin getSkin() {
        return this.mSkin;
    }

    public WheelViewStyle getStyle() {
        return this.mStyle;
    }

    public int getWheelCount() {
        if (WheelUtils.isEmpty(this.mList)) {
            return 0;
        }
        return this.mList.size();
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void join(WheelView wheelView) {
        if (wheelView == null) {
            throw new WheelViewException("wheelview cannot be null.");
        }
        this.mJoinWheelView = wheelView;
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void joinDatas(HashMap<String, List<T>> hashMap) {
        this.mJoinMap = hashMap;
    }

    public void resetDataFromTop(final List<T> list) {
        if (WheelUtils.isEmpty(list)) {
            throw new WheelViewException("join map data is error.");
        }
        postDelayed(new Runnable() { // from class: com.wx.wheelview.widget.WheelView.6
            @Override // java.lang.Runnable
            public void run() {
                WheelView.this.setWheelData(list);
                WheelView.super.setSelection(WheelView.this.mSelection);
                WheelView.this.refreshCurrentPosition(true);
            }
        }, 10L);
    }

    @Override // android.widget.AdapterView
    @Deprecated
    public void setAdapter(ListAdapter listAdapter) {
        if (listAdapter == null || !(listAdapter instanceof BaseWheelAdapter)) {
            throw new WheelViewException("please invoke setWheelAdapter method.");
        }
        setWheelAdapter((BaseWheelAdapter) listAdapter);
    }

    public void setExtraText(String str, int i, int i2, int i3) {
        this.mExtraText = str;
        this.mExtraTextColor = i;
        this.mExtraTextSize = i2;
        this.mExtraMargin = i3;
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void setLoop(boolean z) {
        if (z != this.mLoop) {
            this.mLoop = z;
            setSelection(0);
            if (this.mWheelAdapter != null) {
                this.mWheelAdapter.setLoop(z);
            }
        }
    }

    public void setOnWheelItemClickListener(OnWheelItemClickListener<T> onWheelItemClickListener) {
        this.mOnWheelItemClickListener = onWheelItemClickListener;
    }

    public void setOnWheelItemSelectedListener(OnWheelItemSelectedListener<T> onWheelItemSelectedListener) {
        this.mOnWheelItemSelectedListener = onWheelItemSelectedListener;
    }

    @Override // android.widget.ListView, android.widget.AdapterView
    public void setSelection(final int i) {
        this.mSelection = i;
        setVisibility(4);
        postDelayed(new Runnable() { // from class: com.wx.wheelview.widget.WheelView.7
            @Override // java.lang.Runnable
            public void run() {
                WheelView.super.setSelection(WheelView.this.getRealPosition(i));
                WheelView.this.refreshCurrentPosition(false);
                WheelView.this.setVisibility(0);
            }
        }, 500L);
    }

    public void setSkin(Skin skin) {
        this.mSkin = skin;
    }

    public void setStyle(WheelViewStyle wheelViewStyle) {
        this.mStyle = wheelViewStyle;
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void setWheelAdapter(BaseWheelAdapter<T> baseWheelAdapter) {
        super.setAdapter((ListAdapter) baseWheelAdapter);
        this.mWheelAdapter = baseWheelAdapter;
        this.mWheelAdapter.setData(this.mList).setWheelSize(this.mWheelSize).setLoop(this.mLoop).setClickable(this.mClickable);
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void setWheelClickable(boolean z) {
        if (z != this.mClickable) {
            this.mClickable = z;
            if (this.mWheelAdapter != null) {
                this.mWheelAdapter.setClickable(z);
            }
        }
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void setWheelData(List<T> list) {
        if (WheelUtils.isEmpty(list)) {
            throw new WheelViewException("wheel datas are error.");
        }
        this.mList = list;
        if (this.mWheelAdapter != null) {
            this.mWheelAdapter.setData(list);
        }
    }

    @Override // com.wx.wheelview.widget.IWheelView
    public void setWheelSize(int i) {
        if ((i & 1) == 0) {
            throw new WheelViewException("wheel size must be an odd number.");
        }
        this.mWheelSize = i;
        if (this.mWheelAdapter != null) {
            this.mWheelAdapter.setWheelSize(i);
        }
    }
}
