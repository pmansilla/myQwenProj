package com.czw.compatibility;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/* loaded from: classes.dex */
public class AndroidBug5497Workaround {
    private ViewGroup.LayoutParams frameLayoutParams;
    private View mChildOfContent;
    private int usableHeightPrevious;

    private AndroidBug5497Workaround(View view) {
        if (view != null) {
            this.mChildOfContent = view;
            this.mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.czw.compatibility.AndroidBug5497Workaround.1
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    AndroidBug5497Workaround.this.possiblyResizeChildOfContent();
                }
            });
            this.frameLayoutParams = this.mChildOfContent.getLayoutParams();
        }
    }

    public static void assistActivity(View view) {
        new AndroidBug5497Workaround(view);
    }

    private int computeUsableHeight() {
        Rect rect = new Rect();
        this.mChildOfContent.getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void possiblyResizeChildOfContent() {
        int computeUsableHeight = computeUsableHeight();
        if (computeUsableHeight != this.usableHeightPrevious) {
            this.frameLayoutParams.height = computeUsableHeight;
            this.mChildOfContent.requestLayout();
            this.usableHeightPrevious = computeUsableHeight;
        }
    }
}
