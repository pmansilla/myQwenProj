package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i4;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0035, code lost:
    
        if (r2.mHorizontalChainStyle == 2) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0037, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0039, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0053, code lost:
    
        if (r2.mVerticalChainStyle == 2) goto L18;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x050a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x051c  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0527  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0530  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0542  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x054c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:148:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x052c  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x04f2  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x019e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer r48, android.support.constraint.solver.LinearSystem r49, int r50, int r51, android.support.constraint.solver.widgets.ChainHead r52) {
        /*
            Method dump skipped, instructions count: 1392
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Chain.applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):void");
    }
}
