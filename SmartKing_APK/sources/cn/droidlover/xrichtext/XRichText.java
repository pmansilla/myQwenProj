package cn.droidlover.xrichtext;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class XRichText extends TextView implements ViewTreeObserver.OnGlobalLayoutListener {
    Callback callback;
    ImageLoader downLoader;
    private HashMap<String, ImageHolder> imageHolderMap;
    LocalImageGetter imgGetter;
    private boolean isInit;
    private int richWidth;
    private static Pattern PATTERN_IMG_TAG = Pattern.compile("\\<img(.*?)\\>");
    private static Pattern PATTERN_IMG_WIDTH = Pattern.compile("width=\"(.*?)\"");
    private static Pattern PATTERN_IMG_HEIGHT = Pattern.compile("height=\"(.*?)\"");
    private static Pattern PATTERN_IMG_SRC = Pattern.compile("src=\"(.*?)\"");

    /* loaded from: classes.dex */
    public static class BaseClickCallback implements Callback {
        @Override // cn.droidlover.xrichtext.XRichText.Callback
        public void onFix(ImageHolder imageHolder) {
        }

        @Override // cn.droidlover.xrichtext.XRichText.Callback
        public void onImageClick(List<String> list, int i) {
        }

        @Override // cn.droidlover.xrichtext.XRichText.Callback
        public boolean onLinkClick(String str) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        void onFix(ImageHolder imageHolder);

        void onImageClick(List<String> list, int i);

        boolean onLinkClick(String str);
    }

    /* loaded from: classes.dex */
    public interface ILoad {
        void afterLoad();
    }

    /* loaded from: classes.dex */
    public static class ImageHolder {
        private int position;
        private String src;
        private int width = -1;
        private int height = -1;
        private Style style = Style.CENTER;

        public ImageHolder(String str, int i) {
            this.src = str;
            this.position = i;
        }

        public int getHeight() {
            return this.height;
        }

        public int getPosition() {
            return this.position;
        }

        public String getSrc() {
            return this.src;
        }

        public Style getStyle() {
            return this.style;
        }

        public int getWidth() {
            return this.width;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public void setPosition(int i) {
            this.position = i;
        }

        public void setSrc(String str) {
            this.src = str;
        }

        public void setStyle(Style style) {
            this.style = style;
        }

        public void setWidth(int i) {
            this.width = i;
        }

        public Bitmap valid(Bitmap bitmap, int i) {
            if (bitmap == null) {
                return null;
            }
            int i2 = this.width;
            int i3 = this.height;
            if (i2 == -1 || i3 == -1) {
                i2 = bitmap.getWidth();
                i3 = bitmap.getHeight();
            }
            if (i2 >= i) {
                i3 = (int) (i3 * ((i * 1.0f) / i2));
            } else {
                i = i2;
            }
            this.width = i;
            this.height = i3;
            return Kit.scaleImageTo(bitmap, i, i3, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Kit {
        private Kit() {
        }

        private static Bitmap scaleImage(Bitmap bitmap, float f, float f2, boolean z) {
            if (bitmap == null) {
                return null;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(f, f2);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (z && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return createBitmap;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Bitmap scaleImageTo(Bitmap bitmap, int i, int i2, boolean z) {
            return scaleImage(bitmap, i / bitmap.getWidth(), i2 / bitmap.getHeight(), z);
        }
    }

    /* loaded from: classes.dex */
    public static class LinkSpan extends URLSpan {
        private Callback callback;

        public LinkSpan(String str, Callback callback) {
            super(str);
            this.callback = callback;
        }

        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
        public void onClick(View view) {
            if (this.callback == null || !this.callback.onLinkClick(getURL())) {
                super.onClick(view);
            }
        }
    }

    /* loaded from: classes.dex */
    private class LocalImageGetter implements Html.ImageGetter {
        private LocalImageGetter() {
        }

        @Override // android.text.Html.ImageGetter
        public Drawable getDrawable(String str) {
            final UrlDrawable urlDrawable = new UrlDrawable();
            final ImageHolder imageHolder = (ImageHolder) XRichText.this.imageHolderMap.get(str);
            if (imageHolder == null) {
                return null;
            }
            if (XRichText.this.downLoader == null) {
                XRichText.this.downLoader = new BaseImageLoader(XRichText.this.getContext());
            }
            LoaderTask.getThreadPoolExecutor().execute(new Runnable() { // from class: cn.droidlover.xrichtext.XRichText.LocalImageGetter.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        final Bitmap bitmap = XRichText.this.downLoader.getBitmap(imageHolder.getSrc());
                        if (bitmap != null) {
                            LoaderTask.getMainHandler().obtainMessage(100, new ILoad() { // from class: cn.droidlover.xrichtext.XRichText.LocalImageGetter.1.1
                                @Override // cn.droidlover.xrichtext.XRichText.ILoad
                                public void afterLoad() {
                                    XRichText.this.fillBmp(urlDrawable, imageHolder, bitmap);
                                }
                            }).sendToTarget();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return urlDrawable;
        }
    }

    /* loaded from: classes.dex */
    public enum Style {
        LEFT,
        CENTER,
        RIGHT
    }

    /* loaded from: classes.dex */
    public static class UrlDrawable extends BitmapDrawable {
        private Bitmap bitmap;
        private Paint paint = new Paint();
        private Rect rect;

        @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.bitmap != null) {
                canvas.drawBitmap(this.bitmap, this.rect.left, this.rect.top, this.paint);
            }
        }

        public void setBitmap(Bitmap bitmap, Rect rect) {
            this.bitmap = bitmap;
            this.rect = rect;
        }
    }

    public XRichText(Context context) {
        super(context);
        this.imageHolderMap = new HashMap<>();
        this.isInit = true;
    }

    public XRichText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.imageHolderMap = new HashMap<>();
        this.isInit = true;
    }

    public XRichText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.imageHolderMap = new HashMap<>();
        this.isInit = true;
    }

    private static String getTextBetweenQuotation(String str) {
        Matcher matcher = Pattern.compile("\"(.*?)\"").matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private void queryImgs(String str) {
        Matcher matcher = PATTERN_IMG_TAG.matcher(str);
        int i = 0;
        while (matcher.find()) {
            String trim = matcher.group().trim();
            Matcher matcher2 = PATTERN_IMG_SRC.matcher(trim);
            String textBetweenQuotation = matcher2.find() ? getTextBetweenQuotation(matcher2.group().trim().substring(4)) : null;
            if (!TextUtils.isEmpty(textBetweenQuotation)) {
                ImageHolder imageHolder = new ImageHolder(textBetweenQuotation, i);
                Matcher matcher3 = PATTERN_IMG_WIDTH.matcher(trim);
                if (matcher3.find()) {
                    imageHolder.setWidth(str2Int(getTextBetweenQuotation(matcher3.group().trim().substring(6))));
                }
                Matcher matcher4 = PATTERN_IMG_HEIGHT.matcher(trim);
                if (matcher4.find()) {
                    imageHolder.setHeight(str2Int(getTextBetweenQuotation(matcher4.group().trim().substring(6))));
                }
                this.imageHolderMap.put(imageHolder.src, imageHolder);
                i++;
            }
        }
    }

    private int str2Int(String str) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            return -1;
        }
    }

    private void wrapDrawable(UrlDrawable urlDrawable, ImageHolder imageHolder, Bitmap bitmap) {
        if (bitmap.getWidth() > this.richWidth) {
            return;
        }
        Rect rect = null;
        switch (imageHolder.style) {
            case LEFT:
                rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                break;
            case CENTER:
                int width = (this.richWidth - bitmap.getWidth()) / 2;
                if (width < 0) {
                    width = 0;
                }
                rect = new Rect(width, 0, bitmap.getWidth() + width, bitmap.getHeight());
                break;
            case RIGHT:
                int width2 = this.richWidth - bitmap.getWidth();
                if (width2 < 0) {
                    width2 = 0;
                }
                rect = new Rect(width2, 0, this.richWidth, bitmap.getHeight());
                break;
        }
        urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        urlDrawable.setBitmap(bitmap, rect);
        setText(getText());
    }

    public XRichText callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public void fillBmp(UrlDrawable urlDrawable, ImageHolder imageHolder, Bitmap bitmap) {
        if (urlDrawable == null || imageHolder == null || bitmap == null || this.richWidth <= 0) {
            return;
        }
        if (this.callback != null) {
            this.callback.onFix(imageHolder);
        }
        Bitmap valid = imageHolder.valid(bitmap, this.richWidth);
        if (valid == null) {
            return;
        }
        wrapDrawable(urlDrawable, imageHolder, valid);
    }

    public XRichText imageDownloader(ImageLoader imageLoader) {
        this.downLoader = imageLoader;
        return this;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override // android.view.View
    @TargetApi(16)
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        if (this.isInit) {
            this.richWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
            if (this.richWidth > 0) {
                this.isInit = false;
            }
        }
    }

    public void text(String str) {
        queryImgs(str);
        if (this.imgGetter == null) {
            this.imgGetter = new LocalImageGetter();
        }
        Spanned fromHtml = Html.fromHtml(str, this.imgGetter, null);
        SpannableStringBuilder spannableStringBuilder = fromHtml instanceof SpannableStringBuilder ? (SpannableStringBuilder) fromHtml : new SpannableStringBuilder(fromHtml);
        ImageSpan[] imageSpanArr = (ImageSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final ArrayList arrayList = new ArrayList();
        int length = imageSpanArr.length;
        for (final int i = 0; i < length; i++) {
            ImageSpan imageSpan = imageSpanArr[i];
            String source = imageSpan.getSource();
            int spanStart = spannableStringBuilder.getSpanStart(imageSpan);
            int spanEnd = spannableStringBuilder.getSpanEnd(imageSpan);
            arrayList.add(source);
            ClickableSpan clickableSpan = new ClickableSpan() { // from class: cn.droidlover.xrichtext.XRichText.1
                @Override // android.text.style.ClickableSpan
                public void onClick(View view) {
                    if (XRichText.this.callback != null) {
                        XRichText.this.callback.onImageClick((ArrayList) arrayList, i);
                    }
                }
            };
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannableStringBuilder.getSpans(spanStart, spanEnd, ClickableSpan.class);
            if (clickableSpanArr != null && clickableSpanArr.length != 0) {
                for (ClickableSpan clickableSpan2 : clickableSpanArr) {
                    spannableStringBuilder.removeSpan(clickableSpan2);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, spanStart, spanEnd, 33);
        }
        URLSpan[] uRLSpanArr = (URLSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
        int length2 = uRLSpanArr == null ? 0 : uRLSpanArr.length;
        for (int i2 = 0; i2 < length2; i2++) {
            URLSpan uRLSpan = uRLSpanArr[i2];
            int spanStart2 = spannableStringBuilder.getSpanStart(uRLSpan);
            int spanEnd2 = spannableStringBuilder.getSpanEnd(uRLSpan);
            spannableStringBuilder.removeSpan(uRLSpan);
            spannableStringBuilder.setSpan(new LinkSpan(uRLSpan.getURL(), this.callback), spanStart2, spanEnd2, 33);
        }
        super.setText(fromHtml);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
}
