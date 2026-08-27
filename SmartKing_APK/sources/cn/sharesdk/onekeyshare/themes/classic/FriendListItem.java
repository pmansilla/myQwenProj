package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.themes.classic.FriendAdapter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.BitmapProcessor;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class FriendListItem extends LinearLayout {
    private static final int DESIGN_AVATAR_PADDING = 24;
    private static final int DESIGN_AVATAR_WIDTH = 64;
    private static final int DESIGN_ITEM_HEIGHT = 96;
    private static final int DESIGN_ITEM_PADDING = 20;
    private AsyncImageView aivIcon;
    private Bitmap bmChd;
    private Bitmap bmUnch;
    private ImageView ivCheck;
    private TextView tvName;

    public FriendListItem(Context context, float f) {
        super(context);
        int i = (int) (20.0f * f);
        setPadding(i, 0, i, 0);
        setMinimumHeight((int) (96.0f * f));
        setBackgroundColor(-1);
        this.ivCheck = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        addView(this.ivCheck, layoutParams);
        this.aivIcon = new AsyncImageView(context);
        int i2 = (int) (64.0f * f);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i2, i2);
        layoutParams2.gravity = 16;
        int i3 = (int) (f * 24.0f);
        layoutParams2.setMargins(i3, 0, i3, 0);
        addView(this.aivIcon, layoutParams2);
        this.tvName = new TextView(context);
        this.tvName.setTextColor(-16777216);
        this.tvName.setTextSize(2, 18.0f);
        this.tvName.setSingleLine();
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams3.gravity = 16;
        layoutParams3.weight = 1.0f;
        addView(this.tvName, layoutParams3);
        int bitmapRes = ResHelper.getBitmapRes(context, "ssdk_oks_classic_check_checked");
        if (bitmapRes > 0) {
            this.bmChd = BitmapFactory.decodeResource(context.getResources(), bitmapRes);
        }
        int bitmapRes2 = ResHelper.getBitmapRes(getContext(), "ssdk_oks_classic_check_default");
        if (bitmapRes2 > 0) {
            this.bmUnch = BitmapFactory.decodeResource(context.getResources(), bitmapRes2);
        }
    }

    public void update(FriendAdapter.Following following, boolean z) {
        this.tvName.setText(following.screenName);
        this.ivCheck.setImageBitmap(following.checked ? this.bmChd : this.bmUnch);
        if (this.aivIcon != null) {
            if (!z) {
                this.aivIcon.execute(following.icon, 0);
                return;
            }
            Bitmap bitmapFromCache = BitmapProcessor.getBitmapFromCache(following.icon);
            if (bitmapFromCache == null || bitmapFromCache.isRecycled()) {
                this.aivIcon.execute((String) null, 0);
            } else {
                this.aivIcon.setImageBitmap(bitmapFromCache);
            }
        }
    }
}
