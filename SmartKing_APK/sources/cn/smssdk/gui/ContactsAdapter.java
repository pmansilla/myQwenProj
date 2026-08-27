package cn.smssdk.gui;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.smssdk.gui.ContactsListView;
import cn.smssdk.gui.layout.SizeHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ContactsAdapter extends ContactsListView.GroupAdapter {
    private ArrayList<ArrayList<HashMap<String, Object>>> contacts;
    private ArrayList<HashMap<String, Object>> contactsOutApp;
    private ArrayList<HashMap<String, Object>> friendsInApp;
    private ContactItemMaker itemMaker;
    private SearchEngine sEngine;
    private ArrayList<String> titles;

    public ContactsAdapter(ContactsListView contactsListView, ArrayList<HashMap<String, Object>> arrayList, ArrayList<HashMap<String, Object>> arrayList2) {
        super(contactsListView);
        this.friendsInApp = new ArrayList<>();
        this.contactsOutApp = new ArrayList<>();
        this.friendsInApp = arrayList;
        this.contactsOutApp = arrayList2;
        initSearchEngine();
        search(null);
    }

    private void initSearchEngine() {
        this.sEngine = new SearchEngine();
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<HashMap<String, Object>> it = this.friendsInApp.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            String valueOf = next.containsKey("displayname") ? String.valueOf(next.get("displayname")) : "";
            if (!TextUtils.isEmpty(valueOf)) {
                arrayList.add(valueOf);
            }
        }
        Iterator<HashMap<String, Object>> it2 = this.contactsOutApp.iterator();
        while (it2.hasNext()) {
            HashMap<String, Object> next2 = it2.next();
            String valueOf2 = next2.containsKey("displayname") ? String.valueOf(next2.get("displayname")) : "";
            if (!TextUtils.isEmpty(valueOf2)) {
                arrayList.add(valueOf2);
            }
        }
        this.sEngine.setIndex(arrayList);
    }

    private void reSortFia(HashMap<String, String> hashMap, boolean z, ArrayList<HashMap<String, Object>> arrayList) {
        ArrayList<HashMap<String, Object>> arrayList2 = new ArrayList<>();
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            String valueOf = next.containsKey("displayname") ? String.valueOf(next.get("displayname")) : "";
            if (!TextUtils.isEmpty(valueOf) && (z || hashMap.containsKey(valueOf))) {
                arrayList2.add(next);
            }
        }
        if (arrayList2.size() > 0) {
            int stringRes = ResHelper.getStringRes(this.view.getContext(), "smssdk_contacts_in_app");
            if (stringRes > 0) {
                this.titles.add(this.view.getContext().getResources().getString(stringRes));
            }
            this.contacts.add(arrayList2);
        }
    }

    private void reSortFoa(HashMap<String, String> hashMap, boolean z, ArrayList<HashMap<String, Object>> arrayList) {
        ArrayList<HashMap<String, Object>> arrayList2 = new ArrayList<>();
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            String valueOf = next.containsKey("displayname") ? String.valueOf(next.get("displayname")) : "";
            if (!TextUtils.isEmpty(valueOf) && (z || hashMap.containsKey(valueOf))) {
                arrayList2.add(next);
            }
        }
        if (arrayList2.size() > 0) {
            int stringRes = ResHelper.getStringRes(this.view.getContext(), "smssdk_contacts_out_app");
            if (stringRes > 0) {
                this.titles.add(this.view.getContext().getResources().getString(stringRes));
            }
            this.contacts.add(arrayList2);
        }
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public int getCount(int i) {
        ArrayList<HashMap<String, Object>> arrayList;
        if (this.contacts == null || (arrayList = this.contacts.get(i)) == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public int getGroupCount() {
        if (this.titles == null) {
            return 0;
        }
        return this.titles.size();
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public String getGroupTitle(int i) {
        if (this.titles.size() > 0) {
            return this.titles.get(i).toString();
        }
        return null;
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public HashMap<String, Object> getItem(int i, int i2) {
        if (this.contacts.size() > 0) {
            return this.contacts.get(i).get(i2);
        }
        return null;
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public TextView getTitleView(int i, TextView textView, ViewGroup viewGroup) {
        if (textView == null) {
            textView = new TextView(viewGroup.getContext());
            textView.setBackgroundResource(ResHelper.getColorRes(viewGroup.getContext(), "smssdk_bg_gray"));
            textView.setTextSize(0, SizeHelper.fromPxWidth(viewGroup.getContext(), 25));
            textView.setTextColor(-16777216);
            textView.setPadding(SizeHelper.fromPxWidth(viewGroup.getContext(), 18), 0, 0, 0);
            textView.setWidth(-1);
            textView.setHeight(SizeHelper.fromPxWidth(viewGroup.getContext(), 40));
            textView.setGravity(16);
        }
        String groupTitle = getGroupTitle(i);
        if (!TextUtils.isEmpty(groupTitle)) {
            textView.setText(groupTitle);
        }
        return textView;
    }

    @Override // cn.smssdk.gui.ContactsListView.GroupAdapter
    public View getView(int i, int i2, View view, ViewGroup viewGroup) {
        return this.itemMaker.getView(getItem(i, i2), view, viewGroup);
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
        HashMap<String, String> hashMap = new HashMap<>();
        Iterator<String> it = match.iterator();
        while (it.hasNext()) {
            String next = it.next();
            hashMap.put(next, next);
        }
        this.titles = new ArrayList<>();
        this.contacts = new ArrayList<>();
        if (this.friendsInApp.size() > 0) {
            reSortFia(hashMap, z, this.friendsInApp);
        }
        if (this.contactsOutApp.size() > 0) {
            reSortFoa(hashMap, z, this.contactsOutApp);
        }
    }

    public void setContactItemMaker(ContactItemMaker contactItemMaker) {
        this.itemMaker = contactItemMaker;
    }
}
