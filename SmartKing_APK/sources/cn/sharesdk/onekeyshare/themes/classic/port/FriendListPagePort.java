package cn.sharesdk.onekeyshare.themes.classic.port;

import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.FriendListPage;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class FriendListPagePort extends FriendListPage {
    private static final int DESIGN_SCREEN_WIDTH = 720;
    private static final int DESIGN_TITLE_HEIGHT = 96;

    public FriendListPagePort(OnekeyShareThemeImpl onekeyShareThemeImpl) {
        super(onekeyShareThemeImpl);
    }

    @Override // cn.sharesdk.onekeyshare.themes.classic.FriendListPage
    protected int getDesignTitleHeight() {
        return 96;
    }

    @Override // cn.sharesdk.onekeyshare.themes.classic.FriendListPage
    protected float getRatio() {
        return ResHelper.getScreenWidth(this.activity) / 720.0f;
    }
}
