package cn.smssdk.gui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.GroupListView;
import cn.smssdk.utils.SMSLog;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class CountryAdapter extends GroupListView.GroupAdapter {
    private ArrayList<ArrayList<String[]>> countries;
    private HashMap<Character, ArrayList<String[]>> rawData;
    private SearchEngine sEngine;
    private ArrayList<String> titles;

    public CountryAdapter(GroupListView groupListView) {
        super(groupListView);
        this.rawData = SMSSDK.getGroupedCountryList();
        initSearchEngine();
        search(null);
    }

    private void initSearchEngine() {
        this.sEngine = new SearchEngine();
        ArrayList<String[]> arrayList = new ArrayList<>();
        Iterator<Map.Entry<Character, ArrayList<String[]>>> it = this.rawData.entrySet().iterator();
        while (it.hasNext()) {
            Iterator<String[]> it2 = it.next().getValue().iterator();
            while (it2.hasNext()) {
                String[] next = it2.next();
                String[] strArr = new String[2];
                strArr[0] = next[0];
                if (next.length > 1) {
                    strArr[1] = next[1];
                }
                arrayList.add(strArr);
            }
        }
        this.sEngine.setIndexSet(arrayList);
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public int getCount(int i) {
        ArrayList<String[]> arrayList;
        if (this.countries == null || (arrayList = this.countries.get(i)) == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public int getGroupCount() {
        if (this.titles == null) {
            return 0;
        }
        return this.titles.size();
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public String getGroupTitle(int i) {
        if (this.titles.size() != 0) {
            return this.titles.get(i).toString();
        }
        return null;
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public String[] getItem(int i, int i2) {
        if (this.countries.size() == 0) {
            return null;
        }
        try {
            return this.countries.get(i).get(i2);
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
            return null;
        }
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public View getSubTitleView(View view, ViewGroup viewGroup, String str) {
        View view2 = view;
        if (view == null) {
            LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
            linearLayout.setOrientation(1);
            linearLayout.setGravity(16);
            linearLayout.setBackgroundResource(ResHelper.getColorRes(viewGroup.getContext(), "smssdk_f6f6f6"));
            linearLayout.setPadding(ResHelper.dipToPx(viewGroup.getContext(), 15), ResHelper.dipToPx(viewGroup.getContext(), 5), 0, ResHelper.dipToPx(viewGroup.getContext(), 5));
            TextView textView = new TextView(viewGroup.getContext());
            textView.setTextSize(0, ResHelper.dipToPx(viewGroup.getContext(), 13));
            int colorRes = ResHelper.getColorRes(viewGroup.getContext(), "smssdk_black");
            if (colorRes > 0) {
                textView.setTextColor(viewGroup.getContext().getResources().getColor(colorRes));
            }
            textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            linearLayout.addView(textView);
            view2 = linearLayout;
        }
        ((TextView) ((LinearLayout) view2).getChildAt(0)).setText(str);
        return view2;
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public View getTitleView(View view, ViewGroup viewGroup, String str) {
        View view2 = view;
        if (view == null) {
            LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
            linearLayout.setOrientation(1);
            linearLayout.setGravity(16);
            linearLayout.setBackgroundResource(ResHelper.getColorRes(viewGroup.getContext(), "smssdk_f6f6f6"));
            linearLayout.setPadding(ResHelper.dipToPx(viewGroup.getContext(), 15), ResHelper.dipToPx(viewGroup.getContext(), 5), 0, ResHelper.dipToPx(viewGroup.getContext(), 5));
            TextView textView = new TextView(viewGroup.getContext());
            textView.setTextSize(0, ResHelper.dipToPx(viewGroup.getContext(), 16));
            int colorRes = ResHelper.getColorRes(viewGroup.getContext(), "smssdk_black");
            if (colorRes > 0) {
                textView.setTextColor(viewGroup.getContext().getResources().getColor(colorRes));
            }
            textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            linearLayout.addView(textView);
            view2 = linearLayout;
        }
        ((TextView) ((LinearLayout) view2).getChildAt(0)).setText(str);
        return view2;
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public View getView(View view, ViewGroup viewGroup, String[] strArr) {
        View view2 = view;
        if (view == null) {
            LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
            linearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, ResHelper.dipToPx(viewGroup.getContext(), 50)));
            linearLayout.setBackgroundColor(-1);
            linearLayout.setGravity(16);
            linearLayout.setPadding(ResHelper.dipToPx(viewGroup.getContext(), 15), 0, ResHelper.dipToPx(viewGroup.getContext(), 15), 0);
            TextView textView = new TextView(viewGroup.getContext());
            int colorRes = ResHelper.getColorRes(viewGroup.getContext(), "smssdk_black");
            if (colorRes > 0) {
                textView.setTextColor(viewGroup.getContext().getResources().getColor(colorRes));
            }
            textView.setTextSize(0, ResHelper.dipToPx(viewGroup.getContext(), 16));
            textView.getPaint().setFakeBoldText(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
            layoutParams.weight = 1.0f;
            linearLayout.addView(textView, layoutParams);
            TextView textView2 = new TextView(viewGroup.getContext());
            int colorRes2 = ResHelper.getColorRes(viewGroup.getContext(), "smssdk_686868");
            if (colorRes2 > 0) {
                textView2.setTextColor(viewGroup.getContext().getResources().getColor(colorRes2));
            }
            textView2.setTextSize(0, ResHelper.dipToPx(viewGroup.getContext(), 16));
            linearLayout.addView(textView2, new LinearLayout.LayoutParams(-2, -2));
            view2 = linearLayout;
        }
        if (strArr != null) {
            LinearLayout linearLayout2 = (LinearLayout) view2;
            ((TextView) linearLayout2.getChildAt(0)).setText(strArr[0]);
            ((TextView) linearLayout2.getChildAt(1)).setText("+" + strArr[1]);
        }
        return view2;
    }

    @Override // cn.smssdk.gui.GroupListView.GroupAdapter
    public void onGroupChange(View view, String str) {
        ((TextView) ((LinearLayout) view).getChildAt(0)).setText(str);
    }

    public void search(String str) {
        boolean z;
        ArrayList<String> match = this.sEngine.match(str);
        if (match == null || match.size() <= 0) {
            match = new ArrayList<>();
            z = true;
        } else {
            z = false;
        }
        HashMap hashMap = new HashMap();
        Iterator<String> it = match.iterator();
        while (it.hasNext()) {
            String next = it.next();
            hashMap.put(next, next);
        }
        this.titles = new ArrayList<>();
        this.countries = new ArrayList<>();
        for (Map.Entry<Character, ArrayList<String[]>> entry : this.rawData.entrySet()) {
            ArrayList<String[]> value = entry.getValue();
            ArrayList<String[]> arrayList = new ArrayList<>();
            Iterator<String[]> it2 = value.iterator();
            while (it2.hasNext()) {
                String[] next2 = it2.next();
                if (z || hashMap.containsKey(next2[0])) {
                    arrayList.add(next2);
                }
            }
            if (arrayList.size() > 0) {
                this.titles.add(String.valueOf(entry.getKey()));
                this.countries.add(arrayList);
            }
        }
    }
}
