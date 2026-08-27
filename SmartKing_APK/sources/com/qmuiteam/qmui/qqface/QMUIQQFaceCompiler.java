package com.qmuiteam.qmui.qqface;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.util.LruCache;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUILangHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QMUIQQFaceCompiler {
    private static final int SPAN_COLUMN = 2;
    private static volatile QMUIQQFaceCompiler sInstance;
    private LruCache<CharSequence, ElementList> mCache = new LruCache<>(30);
    private IQMUIQQFaceManager mQQFaceManager;

    /* loaded from: classes2.dex */
    public static class Element {
        private ElementList mChildList;
        private int mDrawableRes;
        private Drawable mSpecialBoundsDrawable;
        private CharSequence mText;
        private QMUITouchableSpan mTouchableSpan;
        private ElementType mType;

        public static Element createDrawableElement(int i) {
            Element element = new Element();
            element.mType = ElementType.DRAWABLE;
            element.mDrawableRes = i;
            return element;
        }

        public static Element createNextLineElement() {
            Element element = new Element();
            element.mType = ElementType.NEXTLINE;
            return element;
        }

        public static Element createSpeaicalBoundsDrawableElement(Drawable drawable) {
            Element element = new Element();
            element.mType = ElementType.SPECIAL_BOUNDS_DRAWABLE;
            element.mSpecialBoundsDrawable = drawable;
            return element;
        }

        public static Element createTextElement(CharSequence charSequence) {
            Element element = new Element();
            element.mType = ElementType.TEXT;
            element.mText = charSequence;
            return element;
        }

        public static Element createTouchSpanElement(CharSequence charSequence, QMUITouchableSpan qMUITouchableSpan, QMUIQQFaceCompiler qMUIQQFaceCompiler) {
            Element element = new Element();
            element.mType = ElementType.SPAN;
            element.mChildList = qMUIQQFaceCompiler.compile(charSequence, 0, charSequence.length(), true);
            element.mTouchableSpan = qMUITouchableSpan;
            return element;
        }

        public ElementList getChildList() {
            return this.mChildList;
        }

        public int getDrawableRes() {
            return this.mDrawableRes;
        }

        public Drawable getSpecialBoundsDrawable() {
            return this.mSpecialBoundsDrawable;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public QMUITouchableSpan getTouchableSpan() {
            return this.mTouchableSpan;
        }

        public ElementType getType() {
            return this.mType;
        }
    }

    /* loaded from: classes2.dex */
    public static class ElementList {
        private int mEnd;
        private int mStart;
        private int mQQFaceCount = 0;
        private int mNewLineCount = 0;
        private List<Element> mElements = new ArrayList();

        public ElementList(int i, int i2) {
            this.mStart = i;
            this.mEnd = i2;
        }

        public void add(Element element) {
            if (element.getType() == ElementType.DRAWABLE) {
                this.mQQFaceCount++;
            } else if (element.getType() == ElementType.NEXTLINE) {
                this.mNewLineCount++;
            } else if (element.getType() == ElementType.SPAN) {
                this.mQQFaceCount += element.getChildList().getQQFaceCount();
                this.mNewLineCount += element.getChildList().getNewLineCount();
            }
            this.mElements.add(element);
        }

        public List<Element> getElements() {
            return this.mElements;
        }

        public int getEnd() {
            return this.mEnd;
        }

        public int getNewLineCount() {
            return this.mNewLineCount;
        }

        public int getQQFaceCount() {
            return this.mQQFaceCount;
        }

        public int getStart() {
            return this.mStart;
        }
    }

    /* loaded from: classes2.dex */
    public enum ElementType {
        TEXT,
        DRAWABLE,
        SPECIAL_BOUNDS_DRAWABLE,
        SPAN,
        NEXTLINE
    }

    private QMUIQQFaceCompiler(IQMUIQQFaceManager iQMUIQQFaceManager) {
        this.mQQFaceManager = iQMUIQQFaceManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ElementList compile(CharSequence charSequence, int i, int i2, boolean z) {
        QMUITouchableSpan[] qMUITouchableSpanArr;
        int[] iArr;
        boolean z2;
        int[] iArr2 = null;
        if (QMUILangHelper.isNullOrEmpty(charSequence)) {
            return null;
        }
        if (i < 0 || i >= charSequence.length()) {
            throw new IllegalArgumentException("start must >= 0 and < text.length");
        }
        if (i2 <= i) {
            throw new IllegalArgumentException("end must > start");
        }
        int length = charSequence.length();
        int i3 = i2 > length ? length : i2;
        if (z || !(charSequence instanceof Spannable)) {
            qMUITouchableSpanArr = null;
            iArr = null;
            z2 = false;
        } else {
            Spannable spannable = (Spannable) charSequence;
            QMUITouchableSpan[] qMUITouchableSpanArr2 = (QMUITouchableSpan[]) spannable.getSpans(0, charSequence.length() - 1, QMUITouchableSpan.class);
            z2 = qMUITouchableSpanArr2.length > 0;
            if (z2) {
                iArr2 = new int[qMUITouchableSpanArr2.length * 2];
                for (int i4 = 0; i4 < qMUITouchableSpanArr2.length; i4++) {
                    int i5 = i4 * 2;
                    iArr2[i5] = spannable.getSpanStart(qMUITouchableSpanArr2[i4]);
                    iArr2[i5 + 1] = spannable.getSpanEnd(qMUITouchableSpanArr2[i4]);
                }
            }
            qMUITouchableSpanArr = qMUITouchableSpanArr2;
            iArr = iArr2;
        }
        ElementList elementList = this.mCache.get(charSequence);
        if (!z2 && elementList != null && i == elementList.getStart() && i3 == elementList.getEnd()) {
            return elementList;
        }
        ElementList realCompile = realCompile(charSequence, i, i3, qMUITouchableSpanArr, iArr);
        this.mCache.put(charSequence, realCompile);
        return realCompile;
    }

    public static QMUIQQFaceCompiler getInstance(IQMUIQQFaceManager iQMUIQQFaceManager) {
        if (sInstance == null) {
            synchronized (QMUIQQFaceCompiler.class) {
                if (sInstance == null) {
                    sInstance = new QMUIQQFaceCompiler(iQMUIQQFaceManager);
                }
            }
        }
        return sInstance;
    }

    private ElementList realCompile(CharSequence charSequence, int i, int i2, QMUITouchableSpan[] qMUITouchableSpanArr, int[] iArr) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int codePointAt;
        QMUITouchableSpan[] qMUITouchableSpanArr2 = qMUITouchableSpanArr;
        int length = charSequence.length();
        int i9 = 1;
        if (qMUITouchableSpanArr2 == null || qMUITouchableSpanArr2.length <= 0) {
            i3 = -1;
            i4 = Integer.MAX_VALUE;
            i5 = Integer.MAX_VALUE;
        } else {
            int i10 = iArr[0];
            i5 = iArr[1];
            i4 = i10;
            i3 = 0;
        }
        ElementList elementList = new ElementList(i, i2);
        if (i > 0) {
            elementList.add(Element.createTextElement(charSequence.subSequence(0, i)));
        }
        int i11 = i3;
        int i12 = i5;
        boolean z = false;
        int i13 = i;
        int i14 = i13;
        while (i13 < i2) {
            if (i13 == i4) {
                if (i13 - i14 > 0) {
                    if (z) {
                        i14--;
                        z = false;
                    }
                    elementList.add(Element.createTextElement(charSequence.subSequence(i14, i13)));
                }
                elementList.add(Element.createTouchSpanElement(charSequence.subSequence(i4, i12), qMUITouchableSpanArr2[i11], this));
                i11++;
                if (i11 >= qMUITouchableSpanArr2.length) {
                    i13 = i12;
                    i14 = i13;
                    i12 = Integer.MAX_VALUE;
                    i4 = Integer.MAX_VALUE;
                } else {
                    int i15 = i11 * 2;
                    i4 = iArr[i15];
                    i14 = i12;
                    i12 = iArr[i15 + i9];
                    i13 = i14;
                }
            } else {
                char charAt = charSequence.charAt(i13);
                if (charAt == '[') {
                    if (i13 - i14 > 0) {
                        elementList.add(Element.createTextElement(charSequence.subSequence(i14, i13)));
                    }
                    i14 = i13;
                    z = true;
                    i13++;
                    i9 = 1;
                } else if (charAt == ']' && z) {
                    i13++;
                    if (i13 - i14 > 0) {
                        String charSequence2 = charSequence.subSequence(i14, i13).toString();
                        Drawable specialBoundsDrawable = this.mQQFaceManager.getSpecialBoundsDrawable(charSequence2);
                        if (specialBoundsDrawable != null) {
                            elementList.add(Element.createSpeaicalBoundsDrawableElement(specialBoundsDrawable));
                        } else {
                            int qQfaceResource = this.mQQFaceManager.getQQfaceResource(charSequence2);
                            if (qQfaceResource != 0) {
                                elementList.add(Element.createDrawableElement(qQfaceResource));
                            }
                        }
                        i14 = i13;
                    }
                    i9 = 1;
                    z = false;
                } else {
                    if (charAt == '\n') {
                        if (z) {
                            z = false;
                        }
                        if (i13 - i14 > 0) {
                            elementList.add(Element.createTextElement(charSequence.subSequence(i14, i13)));
                        }
                        elementList.add(Element.createNextLineElement());
                        i14 = i13 + 1;
                        i13 = i14;
                    } else {
                        if (z) {
                            if (i13 - i14 > 8) {
                                z = false;
                            } else {
                                i13++;
                                qMUITouchableSpanArr2 = qMUITouchableSpanArr;
                            }
                        }
                        if (this.mQQFaceManager.maybeSoftBankEmoji(charAt)) {
                            i7 = this.mQQFaceManager.getSoftbankEmojiResource(charAt);
                            i6 = i7 == 0 ? 0 : 1;
                        } else {
                            i6 = 0;
                            i7 = 0;
                        }
                        if (i7 == 0) {
                            int codePointAt2 = Character.codePointAt(charSequence, i13);
                            int charCount = Character.charCount(codePointAt2);
                            if (this.mQQFaceManager.maybeEmoji(codePointAt2)) {
                                i7 = this.mQQFaceManager.getEmojiResource(codePointAt2);
                            }
                            i6 = (i7 != 0 || (i8 = i + charCount) >= i2 || (i7 = this.mQQFaceManager.getDoubleUnicodeEmoji(codePointAt2, (codePointAt = Character.codePointAt(charSequence, i8)))) == 0) ? charCount : Character.charCount(codePointAt) + charCount;
                        }
                        if (i7 != 0) {
                            if (i14 != i13) {
                                elementList.add(Element.createTextElement(charSequence.subSequence(i14, i13)));
                            }
                            elementList.add(Element.createDrawableElement(i7));
                            i13 += i6;
                            i14 = i13;
                        } else {
                            i13++;
                        }
                        qMUITouchableSpanArr2 = qMUITouchableSpanArr;
                    }
                    i9 = 1;
                }
            }
        }
        if (i14 < i2) {
            elementList.add(Element.createTextElement(charSequence.subSequence(i14, length)));
        }
        return elementList;
    }

    public ElementList compile(CharSequence charSequence) {
        if (QMUILangHelper.isNullOrEmpty(charSequence)) {
            return null;
        }
        return compile(charSequence, 0, charSequence.length());
    }

    public ElementList compile(CharSequence charSequence, int i, int i2) {
        return compile(charSequence, i, i2, false);
    }

    public int getSpecialBoundsMaxHeight() {
        return this.mQQFaceManager.getSpecialDrawableMaxHeight();
    }

    public void setCache(LruCache<CharSequence, ElementList> lruCache) {
        this.mCache = lruCache;
    }
}
