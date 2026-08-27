package com.qmuiteam.qmui.link;

import android.R;
import android.content.res.ColorStateList;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.qmuiteam.qmui.span.QMUIOnSpanClickListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.panpf.sketch.uri.HttpUriModel;
import me.panpf.sketch.uri.HttpsUriModel;

/* loaded from: classes2.dex */
public class QMUILinkify {
    public static final int ALL = 15;
    public static final int EMAIL_ADDRESSES = 2;
    public static final int MAP_ADDRESSES = 8;
    private static final int MAX_NUMBER = 21;
    public static final int PHONE_NUMBERS = 4;
    private static final int PHONE_NUMBER_MINIMUM_DIGITS = 7;
    private static final String UrlEndAppendNextChars = "[$]";
    public static final int WEB_URLS = 1;
    public static final Pattern WECHAT_PHONE = Pattern.compile("\\+?(\\d{2,8}([- ]?\\d{3,8}){2,6}|\\d{5,20})");
    public static final Pattern NOT_PHONE = Pattern.compile("^\\d+(\\.\\d+)+(-\\d+)*$");
    public static final MatchFilter sUrlMatchFilter = new MatchFilter() { // from class: com.qmuiteam.qmui.link.QMUILinkify.1
        @Override // com.qmuiteam.qmui.link.QMUILinkify.MatchFilter
        public final boolean acceptMatch(CharSequence charSequence, int i, int i2) {
            for (int i3 = i; i3 < i2; i3++) {
                try {
                    if (charSequence.charAt(i3) > 256) {
                        return false;
                    }
                } catch (Exception unused) {
                }
            }
            try {
                char charAt = charSequence.charAt(i2);
                if (charAt < 256 && QMUILinkify.UrlEndAppendNextChars.indexOf(charAt) < 0) {
                    if (!Character.isWhitespace(charAt)) {
                        return false;
                    }
                }
            } catch (Exception unused2) {
            }
            if (i == 0) {
                return true;
            }
            return charSequence.charAt(i - 1) != '@';
        }
    };
    public static final MatchFilter sPhoneNumberMatchFilter = new MatchFilter() { // from class: com.qmuiteam.qmui.link.QMUILinkify.2
        @Override // com.qmuiteam.qmui.link.QMUILinkify.MatchFilter
        public final boolean acceptMatch(CharSequence charSequence, int i, int i2) {
            int i3 = 0;
            while (i < i2) {
                if (Character.isDigit(charSequence.charAt(i)) && (i3 = i3 + 1) >= 7) {
                    return true;
                }
                i++;
            }
            return false;
        }
    };
    public static final TransformFilter sPhoneNumberTransformFilter = new TransformFilter() { // from class: com.qmuiteam.qmui.link.QMUILinkify.3
        @Override // com.qmuiteam.qmui.link.QMUILinkify.TransformFilter
        public final String transformUrl(Matcher matcher, String str) {
            return Patterns.digitsAndPlusOnly(matcher);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class LinkSpec {
        int end;
        int start;
        String url;

        private LinkSpec() {
        }
    }

    /* loaded from: classes2.dex */
    public interface MatchFilter {
        boolean acceptMatch(CharSequence charSequence, int i, int i2);
    }

    /* loaded from: classes2.dex */
    private static abstract class StyleableURLSpan extends URLSpan implements ITouchableSpan {
        protected QMUIOnSpanClickListener mOnSpanClickListener;
        protected boolean mPressed;
        protected String mUrl;

        public StyleableURLSpan(String str, QMUIOnSpanClickListener qMUIOnSpanClickListener) {
            super(str);
            this.mPressed = false;
            this.mUrl = str;
            this.mOnSpanClickListener = qMUIOnSpanClickListener;
        }

        @Override // android.text.style.URLSpan, android.text.style.ClickableSpan, com.qmuiteam.qmui.link.ITouchableSpan
        public void onClick(View view) {
            if (this.mOnSpanClickListener.onSpanClick(this.mUrl)) {
                return;
            }
            super.onClick(view);
        }

        @Override // com.qmuiteam.qmui.link.ITouchableSpan
        public void setPressed(boolean z) {
            this.mPressed = z;
        }
    }

    /* loaded from: classes2.dex */
    public interface TransformFilter {
        String transformUrl(Matcher matcher, String str);
    }

    private static void addLinkMovementMethod(TextView textView) {
        MovementMethod movementMethod = textView.getMovementMethod();
        if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static void addLinks(TextView textView, Pattern pattern, String str) {
        addLinks(textView, pattern, str, (MatchFilter) null, (TransformFilter) null);
    }

    public static void addLinks(TextView textView, Pattern pattern, String str, MatchFilter matchFilter, TransformFilter transformFilter) {
        SpannableString valueOf = SpannableString.valueOf(textView.getText());
        if (addLinks(valueOf, pattern, str, matchFilter, transformFilter)) {
            textView.setText(valueOf);
            addLinkMovementMethod(textView);
        }
    }

    public static boolean addLinks(Spannable spannable, int i, ColorStateList colorStateList, ColorStateList colorStateList2, QMUIOnSpanClickListener qMUIOnSpanClickListener) {
        if (i == 0) {
            return false;
        }
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int length = uRLSpanArr.length - 1; length >= 0; length--) {
            spannable.removeSpan(uRLSpanArr[length]);
        }
        ArrayList arrayList = new ArrayList();
        if ((i & 1) != 0) {
            gatherLinks(arrayList, spannable, Patterns.WEB_URL, new String[]{HttpUriModel.SCHEME, HttpsUriModel.SCHEME, "rtsp://"}, sUrlMatchFilter, null);
        }
        if ((i & 2) != 0) {
            gatherLinks(arrayList, spannable, Patterns.EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        }
        if ((i & 4) != 0) {
            gatherPhoneLinks(arrayList, spannable, WECHAT_PHONE, new Pattern[]{NOT_PHONE}, new String[]{"tel:"}, sPhoneNumberMatchFilter, sPhoneNumberTransformFilter);
        }
        if ((i & 8) != 0) {
            gatherMapLinks(arrayList, spannable);
        }
        pruneOverlaps(arrayList);
        if (arrayList.size() == 0) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LinkSpec linkSpec = (LinkSpec) it.next();
            applyLink(linkSpec.url, linkSpec.start, linkSpec.end, spannable, colorStateList, colorStateList2, qMUIOnSpanClickListener);
        }
        return true;
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String str) {
        return addLinks(spannable, pattern, str, (MatchFilter) null, (TransformFilter) null);
    }

    public static boolean addLinks(Spannable spannable, Pattern pattern, String str, MatchFilter matchFilter, TransformFilter transformFilter) {
        String lowerCase = str == null ? "" : str.toLowerCase(Locale.ROOT);
        Matcher matcher = pattern.matcher(spannable);
        boolean z = false;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (matchFilter != null ? matchFilter.acceptMatch(spannable, start, end) : true) {
                applyLink(makeUrl(matcher.group(0), new String[]{lowerCase}, matcher, transformFilter), start, end, spannable, null, null, null);
                z = true;
            }
        }
        return z;
    }

    public static boolean addLinks(TextView textView, int i, ColorStateList colorStateList, ColorStateList colorStateList2, QMUIOnSpanClickListener qMUIOnSpanClickListener) {
        if (i == 0) {
            return false;
        }
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            if (!addLinks((Spannable) text, i, colorStateList, colorStateList2, qMUIOnSpanClickListener)) {
                return false;
            }
            addLinkMovementMethod(textView);
            return true;
        }
        SpannableString valueOf = SpannableString.valueOf(text);
        if (!addLinks(valueOf, i, colorStateList, colorStateList2, qMUIOnSpanClickListener)) {
            return false;
        }
        addLinkMovementMethod(textView);
        textView.setText(valueOf);
        return true;
    }

    private static void applyLink(String str, int i, int i2, Spannable spannable, final ColorStateList colorStateList, final ColorStateList colorStateList2, QMUIOnSpanClickListener qMUIOnSpanClickListener) {
        spannable.setSpan(new StyleableURLSpan(str, qMUIOnSpanClickListener) { // from class: com.qmuiteam.qmui.link.QMUILinkify.4
            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                if (colorStateList != null) {
                    int colorForState = colorStateList.getColorForState(new int[]{R.attr.state_enabled, -16842919}, 0);
                    int colorForState2 = colorStateList.getColorForState(new int[]{R.attr.state_pressed}, colorForState);
                    if (this.mPressed) {
                        colorForState = colorForState2;
                    }
                    textPaint.linkColor = colorForState;
                }
                if (colorStateList2 != null) {
                    int colorForState3 = colorStateList2.getColorForState(new int[]{R.attr.state_enabled, -16842919}, 0);
                    int colorForState4 = colorStateList2.getColorForState(new int[]{R.attr.state_pressed}, colorForState3);
                    if (this.mPressed) {
                        colorForState3 = colorForState4;
                    }
                    textPaint.bgColor = colorForState3;
                }
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        }, i, i2, 33);
    }

    private static void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher(spannable);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (matchFilter == null || matchFilter.acceptMatch(spannable, start, end)) {
                LinkSpec linkSpec = new LinkSpec();
                linkSpec.url = makeUrl(matcher.group(0), strArr, matcher, transformFilter);
                linkSpec.start = start;
                linkSpec.end = end;
                arrayList.add(linkSpec);
            }
        }
    }

    private static void gatherMapLinks(ArrayList<LinkSpec> arrayList, Spannable spannable) {
        int indexOf;
        String obj = spannable.toString();
        int i = 0;
        while (true) {
            try {
                String findAddress = WebView.findAddress(obj);
                if (findAddress != null && (indexOf = obj.indexOf(findAddress)) >= 0) {
                    LinkSpec linkSpec = new LinkSpec();
                    int length = findAddress.length() + indexOf;
                    linkSpec.start = indexOf + i;
                    i += length;
                    linkSpec.end = i;
                    obj = obj.substring(length);
                    try {
                        linkSpec.url = "geo:0,0?q=" + URLEncoder.encode(findAddress, "UTF-8");
                        arrayList.add(linkSpec);
                    } catch (UnsupportedEncodingException unused) {
                    }
                }
                return;
            } catch (UnsupportedOperationException unused2) {
                return;
            }
        }
    }

    private static void gatherPhoneLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern pattern, Pattern[] patternArr, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher(spannable);
        while (matcher.find()) {
            if (!isInExcepts(matcher.group(), patternArr)) {
                int start = matcher.start();
                int end = matcher.end();
                if (matchFilter == null || matchFilter.acceptMatch(spannable, start, end)) {
                    LinkSpec linkSpec = new LinkSpec();
                    linkSpec.url = makeUrl(matcher.group(0), strArr, matcher, transformFilter);
                    linkSpec.start = start;
                    linkSpec.end = end;
                    arrayList.add(linkSpec);
                }
            }
        }
    }

    private static boolean isInExcepts(CharSequence charSequence, Pattern[] patternArr) {
        for (Pattern pattern : patternArr) {
            if (pattern.matcher(charSequence).find()) {
                return true;
            }
        }
        return isTooLarge(charSequence);
    }

    private static boolean isTooLarge(CharSequence charSequence) {
        if (charSequence.length() <= 21) {
            return false;
        }
        int length = charSequence.length();
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (Character.isDigit(charSequence.charAt(i2)) && (i = i + 1) > 21) {
                return true;
            }
        }
        return false;
    }

    private static String makeUrl(String str, String[] strArr, Matcher matcher, TransformFilter transformFilter) {
        boolean z;
        if (transformFilter != null) {
            str = transformFilter.transformUrl(matcher, str);
        }
        int length = strArr.length;
        int i = 0;
        while (true) {
            z = true;
            if (i >= length) {
                z = false;
                break;
            }
            String str2 = strArr[i];
            if (str.regionMatches(true, 0, str2, 0, str2.length())) {
                if (!str.regionMatches(false, 0, str2, 0, str2.length())) {
                    str = str2 + str.substring(str2.length());
                }
            } else {
                i++;
            }
        }
        if (z) {
            return str;
        }
        return strArr[0] + str;
    }

    private static void pruneOverlaps(ArrayList<LinkSpec> arrayList) {
        Collections.sort(arrayList, new Comparator<LinkSpec>() { // from class: com.qmuiteam.qmui.link.QMUILinkify.5
            @Override // java.util.Comparator
            public final int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start <= linkSpec2.start && linkSpec.end >= linkSpec2.end) {
                    return linkSpec.end > linkSpec2.end ? -1 : 0;
                }
                return 1;
            }
        });
        int size = arrayList.size();
        int i = 0;
        while (i < size - 1) {
            LinkSpec linkSpec = arrayList.get(i);
            int i2 = i + 1;
            LinkSpec linkSpec2 = arrayList.get(i2);
            if (linkSpec.start <= linkSpec2.start && linkSpec.end > linkSpec2.start) {
                int i3 = (linkSpec2.end > linkSpec.end && linkSpec.end - linkSpec.start <= linkSpec2.end - linkSpec2.start) ? linkSpec.end - linkSpec.start < linkSpec2.end - linkSpec2.start ? i : -1 : i2;
                if (i3 != -1) {
                    arrayList.remove(i3);
                    size--;
                }
            }
            i = i2;
        }
    }
}
