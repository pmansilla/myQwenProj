package cn.smssdk.gui.layout;

import android.content.Context;
import android.widget.LinearLayout;
import cn.smssdk.gui.CountryListView;
import cn.smssdk.gui.SearchView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class CountryListPageLayout extends BasePageLayout {
    public CountryListPageLayout(Context context) {
        super(context, (String) null);
    }

    @Override // cn.smssdk.gui.layout.BasePageLayout
    protected void onCreateContent(LinearLayout linearLayout) {
        linearLayout.addView(new SearchView(this.context, false));
        CountryListView countryListView = new CountryListView(this.context);
        countryListView.setId(ResHelper.getIdRes(this.context, "clCountry"));
        countryListView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1.0f));
        countryListView.setBackgroundResource(ResHelper.getColorRes(this.context, "smssdk_bg_gray"));
        linearLayout.addView(countryListView);
    }
}
