package cn.smssdk.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.entity.Profile;
import cn.smssdk.gui.layout.ContactListPageLayout;
import cn.smssdk.gui.util.GUISPDB;
import cn.smssdk.utils.SMSLog;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ContactsPage extends FakeActivity implements View.OnClickListener, TextWatcher {
    private ContactsAdapter adapter;
    private ArrayList<HashMap<String, Object>> contactsInMobile;
    private EditText etSearch;
    private ArrayList<HashMap<String, Object>> friendsInApp;
    private EventHandler handler;
    private ContactItemMaker itemMaker;
    private ContactsListView listView;
    private LinearLayout llProfile;
    private OnUserInfoSubmitListener onUserInfoSubmitListener;
    private Dialog pd;
    private TextView tvBind;

    /* loaded from: classes.dex */
    public interface OnUserInfoSubmitListener {
        void onSubmitted(Profile profile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterPrepare() {
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.ContactsPage.2
            @Override // java.lang.Runnable
            public void run() {
                ContactsPage.this.friendsInApp = new ArrayList();
                ContactsPage.this.contactsInMobile = new ArrayList();
                ContactListPageLayout contactListPageLayout = new ContactListPageLayout(ContactsPage.this.activity);
                LinearLayout layout = contactListPageLayout.getLayout();
                ContactsPage.this.onUserInfoSubmitListener = contactListPageLayout.getUserInfoSubmitListenerInstance();
                if (layout != null) {
                    ContactsPage.this.activity.setContentView(layout);
                    ContactsPage.this.initView();
                    ContactsPage.this.initData();
                }
            }
        });
    }

    private void bindProfile() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() { // from class: cn.smssdk.gui.ContactsPage.4
            @Override // cn.smssdk.EventHandler
            public void afterEvent(int i, int i2, Object obj) {
                if (i2 == -1) {
                    HashMap hashMap = (HashMap) obj;
                    String str = (String) hashMap.get("country");
                    String str2 = (String) hashMap.get("phone");
                    Profile profile = GUISPDB.getProfile();
                    if (profile == null) {
                        profile = new Profile(null, str2, null, str, String.valueOf(Math.abs(new Random().nextInt())));
                    } else {
                        profile.setPhoneNum(str2);
                        profile.setCountry(str);
                    }
                    AvatarPage avatarPage = new AvatarPage();
                    Intent intent = new Intent();
                    intent.putExtra(AvatarPage.EXTRA_PROFILE, profile);
                    avatarPage.showForResult(ContactsPage.this.activity, intent, ContactsPage.this);
                    SMSSDK.unregisterEventHandler(ContactsPage.this.handler);
                }
            }
        });
        registerPage.show(this.activity.getApplicationContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initData() {
        this.handler = new EventHandler() { // from class: cn.smssdk.gui.ContactsPage.3
            @Override // cn.smssdk.EventHandler
            public void afterEvent(int i, int i2, final Object obj) {
                if (i2 != -1) {
                    ContactsPage.this.runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.ContactsPage.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ContactsPage.this.pd != null && ContactsPage.this.pd.isShowing()) {
                                ContactsPage.this.pd.dismiss();
                            }
                            try {
                                ((Throwable) obj).printStackTrace();
                                JSONObject jSONObject = new JSONObject(((Throwable) obj).getMessage());
                                String optString = jSONObject.optString("detail");
                                jSONObject.optInt("status");
                                if (!TextUtils.isEmpty(optString)) {
                                    Toast.makeText(ContactsPage.this.activity.getApplicationContext(), optString, 0).show();
                                    return;
                                }
                            } catch (Exception e) {
                                SMSLog.getInstance().w(e);
                            }
                            int stringRes = ResHelper.getStringRes(ContactsPage.this.activity, "smssdk_network_error");
                            if (stringRes > 0) {
                                Toast.makeText(ContactsPage.this.activity.getApplicationContext(), stringRes, 0).show();
                            }
                            ContactsPage.this.llProfile.setVisibility(0);
                        }
                    });
                    return;
                }
                if (i != 4) {
                    if (i == 6) {
                        ContactsPage.this.friendsInApp = (ArrayList) obj;
                        SMSSDK.getContacts(false);
                        return;
                    }
                    return;
                }
                ArrayList arrayList = (ArrayList) obj;
                if (arrayList == null) {
                    ContactsPage.this.contactsInMobile = new ArrayList();
                } else {
                    ContactsPage.this.contactsInMobile = (ArrayList) arrayList.clone();
                }
                ContactsPage.this.refreshContactList();
            }
        };
        SMSSDK.registerEventHandler(this.handler);
        if (this.friendsInApp == null || this.friendsInApp.size() <= 0) {
            SMSSDK.getFriendsInApp();
        } else {
            SMSSDK.getContacts(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initView() {
        this.llProfile = (LinearLayout) this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_profile"));
        this.tvBind = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_profile_rebind"));
        this.listView = (ContactsListView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "clContact"));
        this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_back")).setOnClickListener(this);
        this.activity.findViewById(ResHelper.getIdRes(this.activity, "iv_clear")).setOnClickListener(this);
        this.tvBind.setOnClickListener(this);
        TextView textView = (TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_title"));
        int stringRes = ResHelper.getStringRes(this.activity, "smssdk_search_contact");
        if (stringRes > 0) {
            textView.setText(stringRes);
        }
        this.etSearch = (EditText) this.activity.findViewById(ResHelper.getIdRes(this.activity, "et_put_identify"));
        this.etSearch.addTextChangedListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshContactList() {
        ArrayList arrayList;
        boolean z;
        ArrayList arrayList2 = new ArrayList();
        Iterator<HashMap<String, Object>> it = this.contactsInMobile.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            ArrayList arrayList3 = (ArrayList) next.get("phones");
            if (arrayList3 != null && arrayList3.size() > 0) {
                Iterator it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(new ContactEntry((String) ((HashMap) it2.next()).get("phone"), next));
                }
            }
        }
        ArrayList<HashMap<String, Object>> arrayList4 = new ArrayList<>();
        int size = arrayList2.size();
        Iterator<HashMap<String, Object>> it3 = this.friendsInApp.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            HashMap<String, Object> next2 = it3.next();
            String valueOf = String.valueOf(next2.get("phone"));
            if (valueOf != null) {
                for (int i = 0; i < size; i++) {
                    ContactEntry contactEntry = (ContactEntry) arrayList2.get(i);
                    if (valueOf.equals(contactEntry.getKey())) {
                        next2.put("contact", contactEntry.getValue());
                        next2.put("fia", true);
                        arrayList4.add((HashMap) next2.clone());
                    }
                }
            }
        }
        this.friendsInApp = arrayList4;
        HashSet hashSet = new HashSet();
        Iterator it4 = arrayList2.iterator();
        while (it4.hasNext()) {
            ContactEntry contactEntry2 = (ContactEntry) it4.next();
            String key = contactEntry2.getKey();
            HashMap<String, Object> value = contactEntry2.getValue();
            if (key != null && value != null) {
                Iterator<HashMap<String, Object>> it5 = this.friendsInApp.iterator();
                while (true) {
                    if (it5.hasNext()) {
                        if (key.equals(String.valueOf(it5.next().get("phone")))) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
                if (z) {
                    hashSet.add(value);
                }
            }
        }
        this.contactsInMobile.clear();
        this.contactsInMobile.addAll(hashSet);
        Iterator<HashMap<String, Object>> it6 = this.friendsInApp.iterator();
        while (it6.hasNext()) {
            HashMap<String, Object> next3 = it6.next();
            HashMap hashMap = (HashMap) next3.remove("contact");
            if (hashMap != null) {
                String valueOf2 = String.valueOf(next3.get("phone"));
                if (valueOf2 != null && (arrayList = (ArrayList) hashMap.get("phones")) != null && arrayList.size() > 0) {
                    ArrayList arrayList5 = new ArrayList();
                    Iterator it7 = arrayList.iterator();
                    while (it7.hasNext()) {
                        HashMap hashMap2 = (HashMap) it7.next();
                        if (!valueOf2.equals((String) hashMap2.get("phone"))) {
                            arrayList5.add(hashMap2);
                        }
                    }
                    hashMap.put("phones", arrayList5);
                }
                next3.put("displayname", hashMap.get("displayname"));
            }
        }
        runOnUIThread(new Runnable() { // from class: cn.smssdk.gui.ContactsPage.5
            @Override // java.lang.Runnable
            public void run() {
                if (ContactsPage.this.pd != null && ContactsPage.this.pd.isShowing()) {
                    ContactsPage.this.pd.dismiss();
                }
                ContactsPage.this.adapter = new ContactsAdapter(ContactsPage.this.listView, ContactsPage.this.friendsInApp, ContactsPage.this.contactsInMobile);
                ContactsPage.this.adapter.setContactItemMaker(ContactsPage.this.itemMaker);
                ContactsPage.this.listView.setAdapter(ContactsPage.this.adapter);
                ContactsPage.this.llProfile.setVisibility(0);
            }
        });
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int idRes = ResHelper.getIdRes(this.activity, "ll_back");
        int idRes2 = ResHelper.getIdRes(this.activity, "iv_clear");
        int idRes3 = ResHelper.getIdRes(this.activity, "tv_profile_rebind");
        if (id == idRes) {
            finish();
        } else if (id == idRes2) {
            this.etSearch.getText().clear();
        } else if (id == idRes3) {
            bindProfile();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        if (this.pd != null && this.pd.isShowing()) {
            this.pd.dismiss();
        }
        this.pd = CommonDialog.ProgressDialog(this.activity);
        if (this.pd != null) {
            this.pd.show();
        }
        SearchEngine.prepare(this.activity, new Runnable() { // from class: cn.smssdk.gui.ContactsPage.1
            @Override // java.lang.Runnable
            public void run() {
                ContactsPage.this.afterPrepare();
            }
        });
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(this.handler);
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        try {
            int idRes = ResHelper.getIdRes(this.activity, "llSearch");
            if (i == 4 && keyEvent.getAction() == 0 && this.activity.findViewById(idRes).getVisibility() == 0) {
                this.activity.findViewById(idRes).setVisibility(8);
                this.activity.findViewById(ResHelper.getIdRes(this.activity, "llTitle")).setVisibility(0);
                this.etSearch.setText("");
                return true;
            }
        } catch (Exception e) {
            SMSLog.getInstance().w(e);
        }
        return super.onKeyEvent(i, keyEvent);
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        super.onPause();
    }

    @Override // com.mob.tools.FakeActivity
    public void onResult(HashMap<String, Object> hashMap) {
        Boolean bool;
        super.onResult(hashMap);
        if (hashMap == null || (bool = (Boolean) hashMap.get("res")) == null || !bool.booleanValue() || this.onUserInfoSubmitListener == null) {
            return;
        }
        this.onUserInfoSubmitListener.onSubmitted(GUISPDB.getProfile());
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        super.onResume();
        if (this.handler != null) {
            SMSSDK.registerEventHandler(this.handler);
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (this.adapter != null) {
            this.adapter.search(charSequence.toString());
            this.adapter.notifyDataSetChanged();
        }
        if (TextUtils.isEmpty(charSequence)) {
            this.llProfile.setVisibility(0);
        } else {
            this.llProfile.setVisibility(8);
        }
    }

    public void setOnUserInfoSubmitListener(OnUserInfoSubmitListener onUserInfoSubmitListener) {
        this.onUserInfoSubmitListener = onUserInfoSubmitListener;
    }

    public void show(Context context) {
        show(context, new DefaultContactViewItem());
    }

    public void show(Context context, ContactItemMaker contactItemMaker) {
        this.itemMaker = contactItemMaker;
        super.show(context, (Intent) null);
    }
}
