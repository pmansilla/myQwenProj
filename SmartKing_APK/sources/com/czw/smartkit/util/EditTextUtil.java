package com.czw.smartkit.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes.dex */
public class EditTextUtil {
    private EditTextUtil() {
    }

    public static void setFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() { // from class: com.czw.smartkit.util.EditTextUtil.1
            @Override // android.text.InputFilter
            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                if (charSequence.equals(SQLBuilder.BLANK) || charSequence.toString().contentEquals("\n")) {
                    return "";
                }
                return null;
            }
        }});
    }
}
