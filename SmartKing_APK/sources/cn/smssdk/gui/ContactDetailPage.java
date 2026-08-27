package cn.smssdk.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.smssdk.gui.layout.ContactDetailPageLayout;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ContactDetailPage extends FakeActivity implements View.OnClickListener {
    private String phoneName = "";
    private ArrayList<String> phoneList = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(String str) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        int stringRes = ResHelper.getStringRes(this.activity, "smssdk_invite_content");
        if (stringRes > 0) {
            intent.putExtra("sms_body", this.activity.getString(stringRes));
        }
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showDialog() {
        String[] strArr = (String[]) this.phoneList.toArray(new String[this.phoneList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        int stringRes = ResHelper.getStringRes(this.activity, "smssdk_invite_content");
        if (stringRes > 0) {
            builder.setTitle(stringRes);
        }
        builder.setCancelable(true);
        int stringRes2 = ResHelper.getStringRes(this.activity, "smssdk_cancel");
        if (stringRes2 > 0) {
            builder.setNegativeButton(stringRes2, new DialogInterface.OnClickListener() { // from class: cn.smssdk.gui.ContactDetailPage.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.setItems(strArr, new DialogInterface.OnClickListener() { // from class: cn.smssdk.gui.ContactDetailPage.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ContactDetailPage.this.sendMsg((String) ContactDetailPage.this.phoneList.get(i));
            }
        });
        builder.create().show();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int idRes = ResHelper.getIdRes(this.activity, "ll_back");
        int idRes2 = ResHelper.getIdRes(this.activity, "btn_invite");
        if (id == idRes) {
            finish();
        } else if (id == idRes2) {
            if (this.phoneList.size() > 1) {
                showDialog();
            } else {
                sendMsg(this.phoneList.size() > 0 ? this.phoneList.get(0) : "");
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        LinearLayout layout = new ContactDetailPageLayout(this.activity).getLayout();
        if (layout != null) {
            this.activity.setContentView(layout);
            this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_back")).setOnClickListener(this);
            ((TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_title"))).setText(ResHelper.getStringRes(this.activity, "smssdk_invite_friend"));
            ((TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_contact_name"))).setText(this.phoneName);
            if (this.phoneList != null && !this.phoneList.isEmpty()) {
                ((TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_phone"))).setText(this.phoneList.get(0));
                if (this.phoneList.size() > 1) {
                    ((LinearLayout) this.activity.findViewById(ResHelper.getIdRes(this.activity, "ll_phone2"))).setVisibility(0);
                    this.activity.findViewById(ResHelper.getIdRes(this.activity, "vw_divider2")).setVisibility(0);
                    ((TextView) this.activity.findViewById(ResHelper.getIdRes(this.activity, "tv_phone2"))).setText(this.phoneList.get(1));
                }
            }
            this.activity.findViewById(ResHelper.getIdRes(this.activity, "btn_invite")).setOnClickListener(this);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        super.onPause();
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        super.onResume();
    }

    public void setContact(HashMap<String, Object> hashMap) {
        ArrayList arrayList;
        if (hashMap.containsKey("displayname")) {
            this.phoneName = String.valueOf(hashMap.get("displayname"));
        } else if (hashMap.containsKey("phones") && (arrayList = (ArrayList) hashMap.get("phones")) != null && arrayList.size() > 0) {
            this.phoneName = (String) ((HashMap) arrayList.get(0)).get("phone");
        }
        ArrayList arrayList2 = (ArrayList) hashMap.get("phones");
        if (arrayList2 == null || arrayList2.size() <= 0) {
            return;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            this.phoneList.add((String) ((HashMap) it.next()).get("phone"));
        }
    }
}
