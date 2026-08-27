package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class LayoutFactory {
    static ViewGroup create(Context context, String str) {
        if (str.equals("smssdk_back_verify_dialog")) {
            return BackVerifyDialogLayout.create(context);
        }
        if (str.equals("smssdk_contact_detail_page")) {
            return new ContactDetailPageLayout(context).getLayout();
        }
        if (str.equals("smssdk_contact_list_page")) {
            return new ContactListPageLayout(context).getLayout();
        }
        if (str.equals("smssdk_contacts_listview_item")) {
            return ContactsListviewItemLayout.create(context);
        }
        if (str.equals("smssdk_country_list_page")) {
            return new CountryListPageLayout(context).getLayout();
        }
        if (str.equals("smssdk_input_identify_num_page")) {
            return new IdentifyNumPageLayout(context).getLayout();
        }
        if (str.equals("smssdk_progress_dialog")) {
            return ProgressDialogLayout.create(context);
        }
        if (str.equals("smssdk_register_page")) {
            return new RegisterPageLayout(context).getLayout();
        }
        if (str.equals("smssdk_send_msg_dialog")) {
            return SendMsgDialogLayout.create(context);
        }
        return null;
    }
}
