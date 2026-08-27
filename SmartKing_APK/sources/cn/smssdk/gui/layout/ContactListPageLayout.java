package cn.smssdk.gui.layout;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import cn.smssdk.gui.ContactsListView;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.PersonalInfoView;
import cn.smssdk.gui.SearchView;
import cn.smssdk.gui.entity.Profile;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class ContactListPageLayout extends BasePageLayout {
    private PersonalInfoView personalInfoView;

    public ContactListPageLayout(Context context) {
        super(context, (String) null);
    }

    public ContactsPage.OnUserInfoSubmitListener getUserInfoSubmitListenerInstance() {
        return new ContactsPage.OnUserInfoSubmitListener() { // from class: cn.smssdk.gui.layout.ContactListPageLayout.1
            @Override // cn.smssdk.gui.ContactsPage.OnUserInfoSubmitListener
            public void onSubmitted(Profile profile) {
                if (ContactListPageLayout.this.personalInfoView != null) {
                    ContactListPageLayout.this.personalInfoView.updateUI(profile);
                }
            }
        };
    }

    @Override // cn.smssdk.gui.layout.BasePageLayout
    protected void onCreateContent(LinearLayout linearLayout) {
        linearLayout.addView(new SearchView(this.context, true));
        this.personalInfoView = new PersonalInfoView(this.context);
        linearLayout.addView(this.personalInfoView.create());
        ContactsListView contactsListView = new ContactsListView(this.context);
        contactsListView.setId(ResHelper.getIdRes(this.context, "clContact"));
        contactsListView.setLayoutParams(new AbsListView.LayoutParams(-1, -2, 1));
        linearLayout.addView(contactsListView);
    }
}
